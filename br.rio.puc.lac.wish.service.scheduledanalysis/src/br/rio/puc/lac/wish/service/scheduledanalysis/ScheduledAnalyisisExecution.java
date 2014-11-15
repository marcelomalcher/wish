package br.rio.puc.lac.wish.service.scheduledanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisExecution;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.execution.Constants;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;
import br.rio.puc.lac.wish.analyzer.execution.job.aggregator.IAggregatorJob;
import br.rio.puc.lac.wish.analyzer.utils.HadoopUtils;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 * @email marcelom@inf.puc-rio.br
 */
public class ScheduledAnalyisisExecution {

  /**
   * Connection with database
   */
  private static Connection connection = null;

  /**
   * Flag that controls if execution is running or waiting for next cycle
   */
  private static boolean isRunning = false;

  /**
   * The temporary folder where the files will be located
   */
  private static String tempFolder;

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    //Checking parameters
    if (args.length != 1) {
      System.out.println("The <properties-file> parameter is mandatory.");
      System.exit(-1);
    }
    Properties props = new Properties();
    try {
      props.load(new FileInputStream(args[0]));
    }
    catch (FileNotFoundException e) {
      System.out.println("Properties file does not exists: " + args[0]);
      e.printStackTrace();
      System.exit(-1);
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    try {
      Class.forName(props.getProperty("database.jdbc-driver",
        "com.mysql.jdbc.Driver"));
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }
    try {
      connection =
        DriverManager.getConnection(props
          .getProperty("database.connection-string"));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }

    //Setting logger configuration
    PropertyConfigurator.configure(props);
    DateFormat df = new SimpleDateFormat("yyyyMMdd");
    String s = df.format(Calendar.getInstance().getTime());
    logger =
      Logger.getLogger(ScheduledAnalyisisExecution.class.getSimpleName() + "_"
        + s);

    //Retrieving properties
    long timeInterval = Long.valueOf(props.getProperty("task.timer.interval"));
    tempFolder = props.getProperty("temp.file-folder");

    //Continuously execution of scheduled analysis...
    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        logger.info("Querying scheduled analysis...");
        if (!isRunning) {
          searchForScheduledAnalysis();
        }
      }
    }, 0, timeInterval);
  }

  /**
   * 
   */
  private static void searchForScheduledAnalysis() {
    isRunning = true;

    //Creating query for open scheduled analysis...
    PreparedStatement queryScheduledAnalysis;
    try {
      queryScheduledAnalysis =
        connection
          .prepareStatement("select sa.id, chi.hdfsPath, sa.initialInputPath, ac.hdfsName, ac.mapRedJobTracker, sa.numberReduceTasks, sa.numberMapTasks, sa.initialOutputPath, sa.idFile,"
            + " sa.aggregatorJobClass, sa.maxDistance, sa.temporalWeight, sa.spatialWeight, sa.semanticWeight"
            + " from scheduled_analysis sa left outer join contents_hdfs_import chi on (sa.idContentsHDFSImport = chi.id) inner join analysis_configuration ac on (sa.idConfiguration = ac.id)"
            + " where sa.executionStatus = 0 order by sa.insertDate");
      ResultSet rs = queryScheduledAnalysis.executeQuery();

      //
      int count = 0;
      while (rs.next()) {
        //Identification of scheduled analysis
        int id = rs.getInt("id");
        String hdfsPath = rs.getString("hdfsPath");
        String hdfsName = rs.getString("hdfsName");
        String inputPath = rs.getString("initialInputPath");
        String mapRedJobTracker = rs.getString("mapRedJobTracker");
        int numberReduceTasks = rs.getInt("numberReduceTasks");
        Integer numberMapTasks = rs.getInt("numberMapTasks");
        String initialOutputPath = rs.getString("initialOutputPath");
        int idFile = rs.getInt("idFile");
        String aggregatorJobClass = rs.getString("aggregatorJobClass");
        float maxDistance = rs.getFloat("maxDistance");
        float temporalWeight = rs.getFloat("temporalWeight");
        float spatialWeight = rs.getFloat("spatialWeight");
        float semanticWeight = rs.getFloat("semanticWeight");

        count++;

        logger.info(count + ") Scheduled analysis found with ID=" + id
          + ". Please wait a bit...");

        //Create properties object for new analysis execution
        Properties executionProperties = new Properties();

        //- Paths
        if ((hdfsPath != null) && (!hdfsPath.equals(""))) {
          inputPath = hdfsPath;
        }
        if (inputPath == null && inputPath.equals("")) {
          logger.error("Not a valid input path!!! " + inputPath);
          continue;
        }
        executionProperties.put(Constants.INPUT_PATH, inputPath);
        executionProperties.put(Constants.INITIAL_OUTPUT_PATH,
          initialOutputPath);
        //- Configuration
        executionProperties.put(Constants.HDFS_NAME, hdfsName);
        executionProperties.put(Constants.MAP_JOB_TRACKER, mapRedJobTracker);
        //- # of tasks
        executionProperties.put(Constants.JOB_MAP_TASKS, String
          .valueOf(numberMapTasks));
        executionProperties.put(Constants.JOB_REDUCE_TASKS, String
          .valueOf(numberReduceTasks));

        //- Generate Jar file from db field, store in temporary folder, and direct its path in the properties file
        String jarFile = getStoreTempJarFile(idFile);
        executionProperties.put(Constants.JAR_FILE, jarFile);

        //Adding jar to classpath
        addJarToClassPath(jarFile);

        //- Aggregation properties
        executionProperties.put(Constants.AGGREGATOR_MAXIMUM_DISTANCE, String
          .valueOf(maxDistance));
        executionProperties.put(Constants.AGGREGATOR_TEMPORAL_WEIGHT, String
          .valueOf(temporalWeight));
        executionProperties.put(Constants.AGGREGATOR_SPATIAL_WEIGHT, String
          .valueOf(spatialWeight));
        executionProperties.put(Constants.AGGREGATOR_SEMANTIC_WEIGHT, String
          .valueOf(semanticWeight));

        //AggregatorJob        
        IAggregatorJob aggregatorJob =
          (IAggregatorJob) createInstance(aggregatorJobClass);
        if (aggregatorJob == null) {
          logger
            .error("Aggregator job is null - it was not possible to instantiate the object");

          //Log error in database in this execution
          updateScheduledAnalysis(id, -1,
            "Error while trying to instantiate jobs.", "");

          continue;
        }

        //Creating job list
        List<IJob> jobs = createExecutionJobList(id);
        if (jobs == null) {
          //Log error in database in this execution
          updateScheduledAnalysis(id, -2,
            "Error while trying to instantiate jobs.", "");

          continue;
        }

        //Recovering jobs from table...
        PreparedStatement queryScheduledAnalysisProperties =
          connection
            .prepareStatement("select sap.propertyKey, sap.propertyValue"
              + " from scheduled_analysis sa inner join scheduled_analysis_properties sap on (sa.id = sap.idScheduledAnalysis)"
              + " where sa.id = ? order by sap.propertyKey");
        queryScheduledAnalysisProperties.setInt(1, id);
        ResultSet rsProperties =
          queryScheduledAnalysisProperties.executeQuery();
        while (rsProperties.next()) {
          String propertyKey = rsProperties.getString("propertyKey");
          String propertyValue = rsProperties.getString("propertyValue");

          logger.info("---> Property: " + propertyKey + " - " + propertyValue);

          //Adding property to execution properties object
          executionProperties.put(propertyKey, propertyValue);
        }

        //Create analysis execution object
        AnalysisExecution execution =
          new AnalysisExecution(jobs, aggregatorJob, executionProperties);

        //Thread...
        new Thread(new ScheduledExecutionRunnable(id, execution)).start();
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    logger.info("End of execution cycle. Waiting for next round...");

    isRunning = false;
  }

  /**
   * 
   * @param idFile
   * @return
   */
  private static String getStoreTempJarFile(int idFile) {
    //Creating query for file...
    PreparedStatement queryFileStatement;
    try {
      queryFileStatement =
        connection
          .prepareStatement("select bc.fileName, bc.mimeType, bc.content"
            + " from analysis_execution_file aef inner join tecgenbinarycontent bc on (aef.idBinaryContent = bc.id)"
            + " where aef.id = ?");
      queryFileStatement.setInt(1, idFile);
      ResultSet rs = queryFileStatement.executeQuery();
      //
      int count = 0;
      while (rs.next()) {
        String fileName = rs.getString("fileName");
        String mimeType = rs.getString("mimeType");
        Blob blob = rs.getBlob("content");

        logger.info("Jar file received: " + fileName + " (" + mimeType
          + ") length: " + blob.length());

        String filePath = tempFolder + fileName;

        //Persisting file in temporary storage
        byte[] array = blob.getBytes(1, (int) blob.length());
        FileOutputStream out = new FileOutputStream(new File(filePath));
        out.write(array);
        out.close();

        return filePath;
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    //
    return null;
  }

  private static Object createInstance(String className) {
    Object object = null;
    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> ctor = clazz.getConstructor();
      object = ctor.newInstance();
    }
    catch (ClassNotFoundException e) {
      logger.error("Class not found exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (SecurityException e) {
      logger.error("Security exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (NoSuchMethodException e) {
      logger.error("No such method exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (IllegalArgumentException e) {
      logger.error("Illegal argument exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (InstantiationException e) {
      logger.error("Instantiation exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      logger.error("Illegal access exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      logger.error("Invocation targe exception: " + className + " > message: "
        + e.getLocalizedMessage());
      e.printStackTrace();
    }
    return object;
  }

  private static void addJarToClassPath(String s) throws IOException {
    File f = new File(s);

    URLClassLoader sysloader =
      (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class sysclass = URLClassLoader.class;

    try {
      Method method =
        sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(sysloader, new Object[] { f.toURL() });
    }
    catch (Throwable t) {
      t.printStackTrace();
      throw new IOException("Error, could not add URL to system classloader");
    }
  }

  /**
   * 
   * @param idScheduledAnalysis
   * @return
   */
  private static List<IJob> createExecutionJobList(int idScheduledAnalysis) {
    List<IJob> jobs = new ArrayList<IJob>();
    try {
      //Recovering jobs from table...
      PreparedStatement queryScheduledAnalysisJobs =
        connection
          .prepareStatement("select saj.ordination, saj.jobClass"
            + " from scheduled_analysis sa inner join scheduled_analysis_jobs saj on (sa.id = saj.idScheduledAnalysis)"
            + " where sa.id = ? order by saj.ordination");

      queryScheduledAnalysisJobs.setInt(1, idScheduledAnalysis);
      ResultSet rsJobs = queryScheduledAnalysisJobs.executeQuery();
      while (rsJobs.next()) {
        int ordination = rsJobs.getInt("ordination");
        String jobClass = rsJobs.getString("jobClass");

        //
        logger.info(ordination + ") Job class defined: " + jobClass);

        //Creating instance of IJob and adding to list
        IJob job = (IJob) createInstance(jobClass);
        if (job == null) {
          logger
            .error("IJob is null - it was not possible to instantiate the object");
          return null;
        }
        jobs.add(job);
      }
    }
    catch (SQLException e) {
      logger.error("Error while trying to create execution job list: "
        + e.getLocalizedMessage());
      e.printStackTrace();
      return null;
    }
    return jobs;
  }

  /**
   * 
   * @param id
   * @param status
   * @param message
   * @param outputPath
   */
  private static void updateScheduledAnalysis(int id, int status,
    String message, String outputPath) {
    try {
      //Query for update scheduled analyis
      String query =
        "UPDATE scheduled_analysis SET executionStatus = ?, executionMessage = ?, finalOutputPath = ? WHERE id = ?";

      PreparedStatement queryUpdatScheduledAnalysis =
        connection.prepareStatement(query);
      queryUpdatScheduledAnalysis.setInt(1, status);
      queryUpdatScheduledAnalysis.setString(2, message);
      queryUpdatScheduledAnalysis.setString(3, outputPath);
      queryUpdatScheduledAnalysis.setInt(4, id);
      queryUpdatScheduledAnalysis.executeUpdate();
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger
        .error("Error while trying to update information about scheduled analysis execution with the following parameters: ");
      logger.error("-- id = " + id + " | status = " + status + " - " + message
        + " | outputPath = " + outputPath);
    }
    logger
      .info("Information about scheduled analysis execution updated successfully (id = "
        + id + ")");
  }

  /**
   * 
   * 
   * 
   * @author Marcelo Malcher
   * @email marcelom@inf.puc-rio.br
   */
  static class ScheduledExecutionRunnable implements Runnable {

    private int id;

    private AnalysisExecution execution;

    private Logger logger;

    /**
     * 
     * @param id the identification of the analysis execution to be performed in
     *        the db
     * @param execution
     */
    public ScheduledExecutionRunnable(int id, AnalysisExecution execution) {
      this.id = id;
      this.execution = execution;
      //Logger
      DateFormat df = new SimpleDateFormat("yyyyMMdd");
      String s = df.format(Calendar.getInstance().getTime());
      logger =
        Logger.getLogger(ScheduledExecutionRunnable.class.getSimpleName() + "_"
          + s);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        int res =
          ToolRunner.run(new Configuration(), execution, new String[] {});
      }
      catch (Exception e) {
        logger.error("Error while trying to execute analysis: "
          + e.getLocalizedMessage());
        e.printStackTrace();
        //Log error in database
        updateScheduledAnalysis(id, -1,
          "Error while executing scheduled analysis. \nError: "
            + e.getLocalizedMessage(), "");
        return;
      }

      //
      logger.info("End of execution of analysis: " + id);
      updateScheduledAnalysis(id, execution.getJobCounter(),
        "Scheduled analysis id " + id
          + " executed successfully with jobCounter = "
          + execution.getJobCounter(), execution.getCurrentOutputPath()
          .toString());

      //Read lines in search of Analysis Result's objects in JSon format (string lines)
      List<String> results =
        HadoopUtils.readFileAndReturnListOfLines(execution.getCurrentJobConf(),
          execution.getCurrentOutputPath());

      //Creating analysis result list
      List<AnalysisResult> analysisResults = new ArrayList<AnalysisResult>();

      for (String analysisResultJSonString : results) {
        //
        HashMap<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("contents", Content.class);
        AnalysisResult analysisResult =
          JSon.getFromJSONString(analysisResultJSonString,
            AnalysisResult.class, classMap);
        analysisResults.add(analysisResult);
        //
        logger.info("Analysis Result instantiated: " + analysisResult.getId()
          + " - " + analysisResult.getSemantics().toString());
      }

      logger.info("# of analysis result's objects created: " + results.size());

      //Inserting publication
      int idPublication = publishAnalysisExecutionResults(analysisResults);

      if (idPublication > 0) {
        //Updating scheduled analysis
        updateScheduledAnalysisWithPublication(id, idPublication);
      }
    }
  }

  /**
   * 
   * @param analysisResults
   */
  private static int publishAnalysisExecutionResults(
    List<AnalysisResult> analysisResults) {
    int idPublication = -1;
    try {
      //Query for inserting analyis publication
      String queryPublication =
        "INSERT INTO analysis_publication (date) VALUES (?)";
      PreparedStatement queryInsertAnalysisPublication =
        connection.prepareStatement(queryPublication);
      queryInsertAnalysisPublication.setTimestamp(1, new Timestamp(Calendar
        .getInstance().getTimeInMillis()));
      queryInsertAnalysisPublication.executeUpdate();

      //Selecting ai value
      idPublication = getLastIdFromDatabase();
      if (idPublication < 0) {
        logger
          .error("Error while retrieving analysis_publication.id (last_id()) from database.");
        return -1;
      }

      //Iterating through analysis result's objects
      for (AnalysisResult ar : analysisResults) {

        String queryEvent =
          "INSERT INTO analysis_publication_event (idPublication, semantics, latitude, longitude, timestamp) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement queryInsertEvent =
          connection.prepareStatement(queryEvent);
        queryInsertEvent.setInt(1, idPublication);
        queryInsertEvent.setString(2, ar.getSemantics().toString());
        queryInsertEvent.setDouble(3, ar.getSpatial().getLatitude());
        queryInsertEvent.setDouble(4, ar.getSpatial().getLongitude());
        queryInsertEvent.setLong(5, ar.getTemporal());
        queryInsertEvent.executeUpdate();

        //Selecting ai value
        int idEvent = getLastIdFromDatabase();
        if (idEvent < 0) {
          logger
            .error("Error while retrieving analysis_publication_event.id (last_id()) from database.");
          return -1;
        }

        //Iterating through contents
        for (Content content : ar.getContents()) {
          //Looking for content id in database...
          int contentId = -1;
          String queryContentIdFromSocialId =
            "SELECT id FROM contents WHERE socialId = ?";
          PreparedStatement queryContentIdStatement =
            connection.prepareCall(queryContentIdFromSocialId);
          queryContentIdStatement.setString(1, content.getId());
          ResultSet rs = queryContentIdStatement.executeQuery();
          if (rs.next()) {
            contentId = rs.getInt("id");
          }
          if (contentId < 0) {
            //
            contentId = insertContent(content);
          }
          //
          String queryContent =
            "INSERT INTO analysis_publication_event_content (idPublicationEvent, idContent, classificationJson) VALUES (?, ?, ?)";

          //Content's classification 
          Classification classification = content.getClassification();
          String classificationJSon =
            JSon.toJSONString(Classification.class, classification);

          //Inserting event's content
          PreparedStatement queryInsertContent =
            connection.prepareStatement(queryContent);
          queryInsertContent.setInt(1, idEvent);
          queryInsertContent.setInt(2, contentId);
          queryInsertContent.setString(3, classificationJSon);
          queryInsertContent.executeUpdate();
        }
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error("Error while trying to insert analysis publication");
    }

    logger.info("Publication event inserted: " + idPublication);

    return idPublication;
  }

  public static int insertContent(Content content) {
    int contentId = 0;

    //
    String socialSource = "ANALYSIS";

    //Deleting...
    try {
      String deleteStatement = "delete from contents where socialId = ?";
      PreparedStatement preparedStatement =
        connection.prepareStatement(deleteStatement);
      preparedStatement.setString(1, content.getId());

      //Executing
      int ret = preparedStatement.executeUpdate();

      //Inserting...
      String insertStatement =
        "insert into contents (content, creatorId, socialId, socialSource, location_latitude, location_longitude, location_place_countryCode, location_place_id, location_place_name, location_place_placeType, location_place_streetAddress, tags, timestamp, timestamp_date)";
      insertStatement += " values (?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      //            
      preparedStatement = connection.prepareStatement(insertStatement);
      preparedStatement.setString(1, content.getContent().toString());
      preparedStatement.setString(2, content.getCreatorId().toString());
      preparedStatement.setString(3, content.getId());
      preparedStatement.setString(4, "ANALYSIS");

      //Location
      if (content.getLocation() != null
        && content.getLocation().getLatitude() != null
        && content.getLocation().getLongitude() != null) {
        preparedStatement.setDouble(5, content.getLocation().getLatitude());
        preparedStatement.setDouble(6, content.getLocation().getLongitude());
      }
      else {
        preparedStatement.setDouble(5, 0d);
        preparedStatement.setDouble(6, 0d);
      }

      //Place Info
      preparedStatement.setString(7, null);
      preparedStatement.setString(8, null);
      preparedStatement.setString(9, null);
      preparedStatement.setString(10, null);
      preparedStatement.setString(11, null);
      Place place = content.getLocation().getPlace();
      if (place != null) {
        if (place.getCountryCode() != null) {
          preparedStatement.setString(7, place.getCountryCode());
        }
        if (place.getId() != null) {
          preparedStatement.setString(8, place.getId());
        }
        if (place.getName() != null) {
          preparedStatement.setString(9, place.getName());
        }
        if (place.getPlaceType() != null) {
          preparedStatement.setString(10, place.getPlaceType());
        }
        if (place.getStreetAddress() != null) {
          preparedStatement.setString(11, place.getStreetAddress());
        }
      }

      //Tags
      String tags = null;
      if (content.getTags() != null) {
        tags = "";
        for (int i = 0; i < content.getTags().length; i++) {
          String tag = content.getTags()[i];
          if (i < content.getTags().length - 1) {
            tags += tag + ", ";
          }
          else {
            tags += tag;
          }
        }
      }
      preparedStatement.setString(12, tags);

      //Date
      preparedStatement.setLong(13, content.getTimestamp());
      Timestamp timestamp = new Timestamp(content.getTimestamp());
      preparedStatement.setTimestamp(14, timestamp);

      //Executing
      ret = preparedStatement.executeUpdate();

      System.out.println("-> create content " + content.getId() + " - "
        + content.getContent() + "[" + socialSource + "]");

      contentId = getLastIdFromDatabase();
    }
    catch (SQLException e) {
      System.err.println("SQL Error while inserting content: "
        + e.getErrorCode() + " - " + e.getLocalizedMessage());
      e.printStackTrace();
    }

    return contentId;
  }

  /**
   * 
   * @return
   */
  private static int getLastIdFromDatabase() {
    int id = -1;
    try {
      //Selecting ai value
      String queryLastId = "SELECT LAST_INSERT_ID()";
      PreparedStatement queryLastIdStatement =
        connection.prepareCall(queryLastId);
      ResultSet rs = queryLastIdStatement.executeQuery();
      if (rs.next()) {
        id = rs.getInt(1); //First column
      }
      rs.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * 
   * @param id
   * @param idPublication
   */
  private static void updateScheduledAnalysisWithPublication(int id,
    int idPublication) {
    try {
      //Query for update scheduled analyis
      String query =
        "UPDATE scheduled_analysis SET idPublication = ? WHERE id = ?";

      PreparedStatement queryUpdateScheduledAnalysis =
        connection.prepareStatement(query);
      queryUpdateScheduledAnalysis.setInt(1, idPublication);
      queryUpdateScheduledAnalysis.setInt(2, id);
      queryUpdateScheduledAnalysis.executeUpdate();
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger
        .error("Error while trying to update publication's information from scheduled analysis execution.");
    }
    logger
      .info("Publication information from scheduled analysis execution updated successfully (id = "
        + id + ")");
  }
}