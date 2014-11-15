package br.rio.puc.lac.wish.service.contentsdb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ContentsFromFileToDB {

  private static Connection connection;

  /**
   * @param args
   */
  public static void main(String[] args) {
    //Checking parameters
    if (args.length < 2) {
      System.out
        .println("Usage: ContentsFromFileToDB <property-file> <file-1> <file-2> <file-3> ... <file-n>");
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

    //Database
    String driver = props.getProperty("database.jdbc-driver");
    String connectionString = props.getProperty("database.connection-string");

    int startNumber = Integer.parseInt(props.getProperty("start-at", "1"));

    //
    try {
      Class.forName(driver);
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }

    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(connectionString);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    for (int i = 1; i < args.length; i++) {

      String inputFileName = args[i];

      System.out.println("### - Setting file: " + inputFileName);

      BufferedReader inputStream = null;

      try {

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);

        // Create Buffered/PrintWriter Objects
        inputStream = new BufferedReader(inputFileReader);

        int totalCounter = 0;

        String inLine = null;

        String socialSource = props.getProperty("social-source");

        while ((inLine = inputStream.readLine()) != null) {
          totalCounter++;

          if (totalCounter <= startNumber) {
            System.out.print(".");
            continue;
          }
          else {
            System.out.println("!");
          }

          Content content = JSon.getFromJSONString(inLine, Content.class);

          if (content != null) {

            System.out.println(totalCounter + " > " + content.getId() + " - "
              + content.getContent().toString());

            //Deleting...
            try {
              String deleteStatement =
                "delete from contents where socialId = ?";
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
                preparedStatement.setDouble(5, content.getLocation()
                  .getLatitude());
                preparedStatement.setDouble(6, content.getLocation()
                  .getLongitude());
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
                for (int j = 0; j < content.getTags().length; j++) {
                  String tag = content.getTags()[j];
                  if (j < content.getTags().length - 1) {
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
            }
            catch (SQLException e) {
              System.err.println("SQL Error while inserting content: "
                + e.getErrorCode() + " - " + e.getLocalizedMessage());
              e.printStackTrace();
            }
          }
        }

        System.out.println("- Total number of contents: " + totalCounter
          + " in file " + inputFileName);
      }
      catch (Exception e) {
        System.out.println("Exception:" + e.getLocalizedMessage());
        e.printStackTrace();
        continue;
      }
      finally {
        if (inputStream != null) {
          try {
            inputStream.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
