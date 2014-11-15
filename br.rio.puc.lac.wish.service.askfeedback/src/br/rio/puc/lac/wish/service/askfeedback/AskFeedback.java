package br.rio.puc.lac.wish.service.askfeedback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 * @email marcelom@inf.puc-rio.br
 */
public class AskFeedback {

  /**
   * 
   */
  private static Twitter twitter;

  /**
   * 
   */
  private static Properties props;

  /**
   * Connection with database
   */
  private static Connection connection = null;

  /**
   * Flag that controls if execution is running or waiting for next cycle
   */
  private static boolean isRunning = false;

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   */
  private static int greetingNumber, messageNumber, askNumber;

  /**
   * 
   */
  private static Random rand = new Random();

  /**
   * 
   */
  private static String feedbackLink;

  /**
   * 
   */
  private static ConfigurationBuilder cb;

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
    props = new Properties();
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
    logger = Logger.getLogger(AskFeedback.class.getSimpleName() + "_" + s);

    //Obtaining TwitterStream object 
    cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true);
    cb.setOAuthConsumerKey(props.getProperty("twitter.oauth.consumerKey"));
    cb.setOAuthConsumerSecret(props.getProperty("twitter.oauth.consumerSecret"));
    cb.setOAuthAccessToken(props.getProperty("twitter.oauth.accessToken"));
    cb.setOAuthAccessTokenSecret(props
      .getProperty("twitter.oauth.accessTokenSecret"));

    //Twitter
    twitter = new TwitterFactory(cb.build()).getInstance();

    //Retrieving timer properties
    long timerInitial = Long.valueOf(props.getProperty("task.timer.initial"));
    long timeInterval = Long.valueOf(props.getProperty("task.timer.interval"));

    //Feedback Message
    greetingNumber =
      Integer.parseInt(props.getProperty("feedback.greeting.number", "0"));
    messageNumber =
      Integer.parseInt(props.getProperty("feedback.message.number", "0"));
    askNumber = Integer.parseInt(props.getProperty("feedback.ask.number", "0"));

    feedbackLink = props.getProperty("feedback.link");

    //Continuously execution of scheduled analysis...
    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        logger.info("Querying feedbacks to ask...");
        if (!isRunning) {
          queryOpenFeedbacks();
        }
      }
    }, timerInitial, timeInterval);
  }

  /**
   * 
   */
  private static void queryOpenFeedbacks() {
    isRunning = true;

    //Creating query for open analysis feedbacks...
    PreparedStatement queryOpenAnalysisFeedbacks = null;
    try {
      queryOpenAnalysisFeedbacks =
        connection
          .prepareStatement("select af.id from analysis_feedback af where af.status = 0 order by af.date");
      ResultSet rs = queryOpenAnalysisFeedbacks.executeQuery();

      //
      int count = 0;
      while (rs.next()) {
        //Identification of analysis feedback
        int id = rs.getInt("id");

        count++;

        logger.info(count + ") Open analysis feedback with ID=" + id
          + ". Asking for feedback...");

        askForFeedback(id);
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
    finally {
      try {
        if ((queryOpenAnalysisFeedbacks != null)
          && (!queryOpenAnalysisFeedbacks.isClosed())) {
          queryOpenAnalysisFeedbacks.close();
        }
      }
      catch (SQLException e) {
        logger.error("Error while trying to close connection to database: "
          + e.getLocalizedMessage());
        e.printStackTrace();
      }
    }

    logger.info("End of cycle. Waiting for next round...");

    isRunning = false;
  }

  /**
   * 
   * 
   * 
   * @author Tecgraf
   */
  private enum UpdateFeedbackOption {
    ANALYSIS_FEEDBACK,
    ANALYSIS_FEEDBACK_EVENT_CONTENT;
  }

  /**
   * 
   * @param updateFeedbackOption
   * @param id
   * @param status
   * @param message
   */
  private static void updateAnalysisFeedback(
    UpdateFeedbackOption updateFeedbackOption, int id, int status,
    String message) {
    String tableName = "";
    if (updateFeedbackOption == UpdateFeedbackOption.ANALYSIS_FEEDBACK) {
      tableName += " analysis_feedback ";
    }
    else if (updateFeedbackOption == UpdateFeedbackOption.ANALYSIS_FEEDBACK_EVENT_CONTENT) {
      tableName += " analysis_feedback_content ";
    }
    try {
      //Query for update analysis_feedback
      String query =
        "UPDATE " + tableName + " SET status = ?, message = ? WHERE id = ?";

      PreparedStatement queryUpdatAnalysisFeedback =
        connection.prepareStatement(query);
      queryUpdatAnalysisFeedback.setInt(1, status);
      queryUpdatAnalysisFeedback.setString(2, message);
      queryUpdatAnalysisFeedback.setInt(3, id);
      queryUpdatAnalysisFeedback.executeUpdate();
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger
        .error("Error while trying to update information about scheduled analysis execution with the following parameters: ");
      logger.error("-- id = " + id + " | status = " + status + " - " + message
        + " | update option = " + updateFeedbackOption.toString());
    }
    logger.info("Information in table " + tableName
      + " updated successfully (id = " + id + ")");
  }

  /**
   * 
   * @param feedbackId
   */
  private static void askForFeedback(int feedbackId) {
    PreparedStatement queryFeedbackStatement = null;
    try {
      //Query for contents
      String queryFeedback =
        "SELECT afec.id, afec.request_feedback_id, afec.idPublicationEventContent, apec.idContent, c.creatorId ";
      queryFeedback +=
        " FROM analysis_feedback_content afec INNER JOIN analysis_publication_event_content apec ON (afec.idPublicationEventContent = apec.id)";
      queryFeedback += " INNER JOIN contents c ON (apec.idContent = c.id)";
      queryFeedback += " WHERE afec.idFeedback = ? AND afec.status <> ?";
      queryFeedbackStatement = connection.prepareStatement(queryFeedback);
      queryFeedbackStatement.setInt(1, feedbackId);
      queryFeedbackStatement.setInt(2, 1); //1 means successfully sent
      ResultSet rs = queryFeedbackStatement.executeQuery();

      //
      while (rs.next()) {
        int id = rs.getInt("id");
        String requestId = rs.getString("request_feedback_id");
        //int idPublicationEventContent = rs.getInt("idPublicationEventContent");
        int idContent = rs.getInt("idContent");
        String creatorId = rs.getString("creatorId");

        if (contentHasFeedback(id, idContent)) {
          continue;
        }

        //
        String creator = "@" + creatorId;
        String link = feedbackLink + requestId;

        //Message
        //1. Greetings
        int greetingSelected = rand.nextInt(greetingNumber);
        String greeting =
          props.getProperty("feedback.greeting." + greetingSelected);
        logger.info("...GREETING: " + greeting);
        greeting = String.format(greeting, creator);
        //2. Message
        int messageSelected = rand.nextInt(messageNumber);
        String message =
          props.getProperty("feedback.message." + messageSelected);
        logger.info("...MESSAGE: " + message);
        //3. Ask
        int askSelected = rand.nextInt(greetingNumber);
        String ask = props.getProperty("feedback.ask." + askSelected);
        logger.info("...ASK: " + ask);
        ask = String.format(ask, link);

        String tweet = greeting + " " + message + " " + ask;

        logger.info("Message to send to user " + creator + " is: " + tweet);

        Status status = null;
        try {
          twitter.createFriendship(creatorId);
          status = twitter.updateStatus(tweet);
        }
        catch (TwitterException e) {
          logger.error("Error while trying to send feedback for " + creatorId
            + ": " + e.getLocalizedMessage());
          e.printStackTrace();
          updateAnalysisFeedback(
            UpdateFeedbackOption.ANALYSIS_FEEDBACK_EVENT_CONTENT, id, 0, e
              .getErrorMessage());
          continue;
        }
        logger.info("The message sent was: " + status.getText());

        //Updating
        updateAnalysisFeedback(
          UpdateFeedbackOption.ANALYSIS_FEEDBACK_EVENT_CONTENT, id, 1, status
            .getText());
      }

      //
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error("Error while trying to ask feedback");
    }
    finally {
      try {
        if ((queryFeedbackStatement != null)
          && (!queryFeedbackStatement.isClosed())) {
          queryFeedbackStatement.close();
        }
      }
      catch (SQLException e) {
        logger.error("Error while trying to close connection to database: "
          + e.getLocalizedMessage());
        e.printStackTrace();
      }
    }
    logger.info("Feedbacks were sent for id " + feedbackId);
  }

  private static boolean contentHasFeedback(int id, int idContent) {
    boolean result = false;
    PreparedStatement queryFeedbackStatement = null;
    try {
      //Query for contents
      String queryFeedback =
        "SELECT afec.id, afec.vote, afec.date, afec.observations ";
      queryFeedback +=
        " FROM analysis_feedback_content afec INNER JOIN analysis_publication_event_content apec ON (afec.idPublicationEventContent = apec.id)";
      queryFeedback += " WHERE apec.idContent = ?";
      queryFeedback += " AND afec.vote is not null AND afec.date is not null";
      queryFeedbackStatement = connection.prepareStatement(queryFeedback);
      queryFeedbackStatement.setInt(1, idContent);
      ResultSet rs = queryFeedbackStatement.executeQuery();

      //
      if (rs.next()) {
        int otherFeedbackId = rs.getInt("id");
        boolean vote = rs.getBoolean("vote");
        Date date = rs.getDate("date");
        String observations = rs.getString("observations");

        //Updating
        String queryUpdateExistingFeedback =
          "UPDATE analysis_feedback_content ";
        queryUpdateExistingFeedback +=
          " SET vote = ?, date = ?, observations = ?, status = ?, message = ? ";
        queryUpdateExistingFeedback += " WHERE id = ?";
        //
        PreparedStatement queryUpdateExistingFeedbackStatement =
          connection.prepareStatement(queryUpdateExistingFeedback);
        queryFeedbackStatement.setBoolean(1, vote);
        queryFeedbackStatement.setDate(2, new java.sql.Date(date.getTime()));
        queryFeedbackStatement.setString(3, observations);
        queryFeedbackStatement.setInt(4, 1);
        queryFeedbackStatement.setString(5,
          "Retrived existing feedback from id " + otherFeedbackId);
        queryFeedbackStatement.setInt(6, id);
        queryFeedbackStatement.executeUpdate();

        result = true;
      }
      //
    }
    catch (SQLException e) {
      e.printStackTrace();
      logger.error("Error while trying to ask feedback");
    }
    finally {
      try {
        if ((queryFeedbackStatement != null)
          && (!queryFeedbackStatement.isClosed())) {
          queryFeedbackStatement.close();
        }
      }
      catch (SQLException e) {
        logger.error("Error while trying to close connection to database: "
          + e.getLocalizedMessage());
        e.printStackTrace();
      }
    }
    return result;
  }
}
