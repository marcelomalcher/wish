package br.rio.puc.lac.wish.service.wscontent;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jws.WebService;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Place;

@WebService(endpointInterface = "br.rio.puc.lac.wish.service.wscontent.IContentService", serviceName = "ContentService")
public class ContentService implements IContentService {

  private static final String SOCIAL_SOURCE_KEY = "socialSource";

  private static ConcurrentLinkedQueue<Content<String, String>> queue =
    new ConcurrentLinkedQueue<Content<String, String>>();

  private static Connection connection;

  /**
   * Flag that controls if persists contents in database or waiting for next
   * cycle
   */
  private static boolean isRunning = false;

  private String driver;
  private String connectionString;

  private long timerInitial;
  private long timerInterval;

  /**
   * 
   * @param driver
   * @param connectionString
   * @param timerInitial
   * @param timerInterval
   */
  public ContentService(String driver, String connectionString,
    long timerInitial, long timerInterval) {
    super();
    //Database
    this.driver = driver;
    this.connectionString = connectionString;
    //Timer
    this.timerInitial = timerInitial;
    this.timerInterval = timerInterval;

    init();
  }

  /**
   * 
   */
  private void init() {
    //
    try {
      Class.forName(driver);
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }

    //
    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Starting task to persist contents in database...");
        if (!isRunning) {
          persistContents();
        }
      }
    }, timerInitial, timerInterval);
  }

  /**
   * 
   */
  private void persistContents() {
    isRunning = true;

    System.out.println("--- Size of queue: " + queue.size());
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(connectionString);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    while (!queue.isEmpty()) {
      Content<String, String> content = queue.poll();
      if (content != null) {
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
          insertStatement +=
            " values (?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

          String socialSource =
            content.getClassification().getInformation(SOCIAL_SOURCE_KEY);

          //            
          preparedStatement = connection.prepareStatement(insertStatement);
          preparedStatement.setString(1, content.getContent().toString());
          preparedStatement.setString(2, content.getCreatorId().toString());
          preparedStatement.setString(3, content.getId());
          preparedStatement.setString(4, socialSource);

          //Location
          if (content.getLocation() != null
            && content.getLocation().getLatitude() != null
            && content.getLocation().getLongitude() != null) {
            preparedStatement.setDouble(5, content.getLocation().getLatitude());
            preparedStatement
              .setDouble(6, content.getLocation().getLongitude());
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
        }
        catch (SQLException e) {
          System.err.println("SQL Error while inserting content: "
            + e.getErrorCode() + " - " + e.getLocalizedMessage());
          e.printStackTrace();
        }
      }
    }

    try {
      connection.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    isRunning = false;
  }

  /**
   * 
   * @param content
   * @param socialSource
   * @return
   */
  @Override
  public boolean create(Content<String, String> content, String socialSource) {
    try {
      content.getContent().getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      return false;
    }

    System.out.println("--- content arrived : " + content.getId() + " - "
      + content.getContent() + " from [" + socialSource + "]");

    //SocialSource in the informationMap

    Classification classification = new Classification();
    classification.putInformation(SOCIAL_SOURCE_KEY, socialSource);
    content.setClassification(classification);

    //Putting in the queue
    return queue.offer(content);
  }
}
