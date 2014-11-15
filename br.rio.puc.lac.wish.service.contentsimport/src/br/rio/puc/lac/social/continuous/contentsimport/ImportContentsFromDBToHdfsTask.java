package br.rio.puc.lac.social.continuous.contentsimport;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

public class ImportContentsFromDBToHdfsTask {

  private static Connection connection = null;

  private static boolean isRunning = false;

  private static Logger logger;

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
      Logger.getLogger(ImportContentsFromDBToHdfsTask.class.getSimpleName()
        + "_" + s);

    //Retrieving properties...
    long intervalTime = Long.valueOf(props.getProperty("task.timer.interval"));
    final String tempFileName = props.getProperty("temp.file-name");
    final String tempFileExtension = props.getProperty("temp.file-extension");

    //Continuously execution of imports...
    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        logger.info("- Starting new import task...");
        if (!isRunning) {
          startNewTask(tempFileName, tempFileExtension);
        }
      }
    }, 0, intervalTime);
  }

  /**
   * 
   * @param tempFileName
   * @param tempFileExtension
   */
  private static void startNewTask(String tempFileName, String tempFileExtension) {
    isRunning = true;
    try {
      //Querying for imports to perform...
      PreparedStatement queryImports =
        connection
          .prepareStatement("select ch.id, ac.hdfsName, ac.mapRedJobTracker, queryBeginDate, queryEndDate, hdfsPath, querySocialSource"
            + " from contents_hdfs_import ch inner join analysis_configuration ac on (ch.idConfiguration = ac.id)"
            + " where ch.importStatus = 0 order by ch.insertDate");
      ResultSet rs = queryImports.executeQuery();
      //
      int count = 0;
      while (rs.next()) {
        int id = rs.getInt("id");
        String hdfsName = rs.getString("hdfsName");
        String mapRedJobTracker = rs.getString("mapRedJobTracker");
        Date queryBeginDate = rs.getDate("queryBeginDate");
        Date queryEndDate = rs.getDate("queryEndDate");
        String hdfsPath = rs.getString("hdfsPath");
        String querySocialSource = rs.getString("querySocialSource");

        //
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName =
          tempFileName + "_"
            + formatter.format(Calendar.getInstance().getTime())
            + tempFileExtension;

        logger.info(++count + "# Importing contents to : " + hdfsPath
          + " into " + hdfsName + " for file: " + fileName
          + ". Please wait a bit...");

        //
        importContents(id, hdfsName, mapRedJobTracker, queryBeginDate,
          queryEndDate, hdfsPath, querySocialSource, fileName);
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

    isRunning = false;
  }

  /**
   * 
   * @param id
   * @param hdfsName
   * @param mapRedJobTracker
   * @param queryBeginDate
   * @param queryEndDate
   * @param hdfsPath
   * @param querySocialSource
   * @param fileName
   */
  private static void importContents(int id, String hdfsName,
    String mapRedJobTracker, Date queryBeginDate, Date queryEndDate,
    String hdfsPath, String querySocialSource, String fileName) {

    //Query
    String strQueryContents =
      "SELECT content, creatorId, socialId, location_latitude, location_longitude, location_place_countryCode,"
        + " location_place_id, location_place_name, location_place_placeType, location_place_streetAddress, tags, timestamp"
        + " FROM contents" + " WHERE timestamp BETWEEN ? AND ?";
    if ((querySocialSource != null) && (!querySocialSource.equals(""))) {
      strQueryContents += " AND socialSource = ?";
    }
    logger.info("# Query used for contents: " + strQueryContents);

    //
    Status status = new Status();
    int count = 0;

    PreparedStatement queryContents;
    try {
      queryContents = connection.prepareStatement(strQueryContents);

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      logger.info("### Begin date: " + formatter.format(queryBeginDate) + " - "
        + queryBeginDate.getTime());
      logger.info("### End date: " + formatter.format(queryEndDate) + " - "
        + queryEndDate.getTime());

      //Passing parameters...
      queryContents.setLong(1, queryBeginDate.getTime());
      queryContents.setLong(2, queryEndDate.getTime());

      //Checking if social source is specified
      if ((querySocialSource != null) && (!querySocialSource.equals(""))) {
        queryContents.setString(3, querySocialSource);
        System.out.println("### Social source: " + querySocialSource);
      }

      try {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ResultSet rs = queryContents.executeQuery();
        rs.first();
        //
        while (rs.next()) {
          String contentMessage = rs.getString("content");
          String creatorId = rs.getString("creatorId");
          String socialId = rs.getString("socialId");
          double latitude = rs.getDouble("location_latitude");
          double longitude = rs.getDouble("location_longitude");
          String placeCountryCode = rs.getString("location_place_countryCode");
          String placeId = rs.getString("location_place_id");
          String placeName = rs.getString("location_place_name");
          String placeType = rs.getString("location_place_placeType");
          String placeStreetAddress =
            rs.getString("location_place_streetAddress");
          String tags = rs.getString("tags");
          long timestamp = rs.getLong("timestamp");

          //TODO recover F4 locations...

          //Creatging content object
          Content<String, String> content = new Content<String, String>();
          content.setContent(contentMessage);
          content.setCreatorId(creatorId);
          content.setId(socialId);

          if (tags != null) {
            String[] arrayTags = tags.split(",");
            content.setTags(arrayTags);
          }

          Location location = new Location(latitude, longitude);
          Place place = new Place();
          place.setCountryCode(placeCountryCode);
          place.setId(placeId);
          place.setName(placeName);
          place.setPlaceType(placeType);
          place.setStreetAddress(placeStreetAddress);
          content.setLocation(location);
          content.setTimestamp(timestamp);

          //Writing content object to JSON string
          bufferedWriter.write(JSon.toJSONString(Content.class, content));
          bufferedWriter.newLine();

          count++;
        }

        bufferedWriter.close();
      }
      catch (IOException ex) {
        status.id = -1;
        status.message = "# Error writing to file '" + fileName + "'";
        logger.info(status.message);
      }

      if (count <= 0) {
        status.id = -2;
        status.message = "# No contents were found with import: " + id + ".";
      }
      else {
        //Attempt to import
        String moveStr =
          moveFileToHdfs(hdfsName, fileName, hdfsPath + "//" + fileName);
        if (moveStr == null) {
          status.id = 1;
          status.message =
            "# Total number of contents imported to HDFS: " + count;
        }
        else {
          status.id = -3;
          status.message = moveStr;
        }
      }

      //Query for update import
      PreparedStatement queryUpdateImport =
        connection
          .prepareStatement("UPDATE contents_hdfs_import SET importStatus = ?, importMessage = ? WHERE id = ?");
      queryUpdateImport.setInt(1, status.id);
      queryUpdateImport.setString(2, status.message);
      queryUpdateImport.setInt(3, id);
      queryUpdateImport.executeUpdate();

      logger.info(status.message);
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }

  /**
   * 
   * @param hdfsName
   * @param source
   * @param dest
   * @return
   */
  private static String moveFileToHdfs(String hdfsName, String source,
    String dest) {
    try {
      Configuration conf = new Configuration();
      conf.set("fs.default.name", hdfsName);

      FileSystem fs = FileSystem.get(conf);
      fs.moveFromLocalFile(new Path(source), new Path(dest));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return "Error while importing local file to HDFS: "
        + ex.getLocalizedMessage();
    }
    return null;
  }

  /**
   * Class to help in move file to Hdfs
   * 
   * 
   * @author Marcelo Malcher
   */
  private static class Status {
    int id;
    String message;
  }
}
