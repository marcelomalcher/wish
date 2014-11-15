package br.rio.puc.lac.wish.extractor.f4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExtractVenuesFromDBLocations {

  private static Connection connection = null;

  private static boolean RUNNING = false;

  private static String F4_CLIENT_ID;
  private static String F4_CLIENT_SECRET;

  private static long TIMER_INTERVAL;
  private static long TASK_WAIT;

  private static int SELECT_CHECK_DIFF_DAYS;

  private static String F4_TASK_RADIUS;
  private static long F4_TASK_EXCEEDED_QUOTA_WAIT;

  //Select locations to search in F4...
  private static String strQuerySelectLocation =
    "SELECT id, concat(latitude,',',longitude) as coord FROM contents_location"
      + " where not exists (select * from contents_location_f4_locations clfl where contents_location.id = clfl.locationId)"
      + " and (checkDate is null or DATEDIFF(now(), checkDate) > ?) ";
  private static PreparedStatement querySelectLocation;

  //Insert statement of F4 venues...
  private static String strQueryInsertF4Location =
    "REPLACE into f4_locations (id, name, latitude, longitude, category_name, category_pluralname, category_shortname, json_info) values (?, ?, ?, ?, ?, ?, ?, ?)";
  private static PreparedStatement queryInsertF4Location;

  //Insert relationship between venues and locations 
  private static String strQueryUpdateLocationF4Location =
    "REPLACE into contents_location_f4_locations (locationId, f4LocationId, distance) values (?, ?, ?)";
  private static PreparedStatement queryUpdateLocationF4Location;

  //Update contents location with check 
  private static String strQueryUpdateLocations =
    "UPDATE contents_location SET checkDate = ? WHERE id = ?";
  private static PreparedStatement queryUpdateLocations;

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

    // Query statements 
    querySelectLocation = connection.prepareStatement(strQuerySelectLocation);
    queryInsertF4Location =
      connection.prepareStatement(strQueryInsertF4Location);
    queryUpdateLocationF4Location =
      connection.prepareStatement(strQueryUpdateLocationF4Location);
    queryUpdateLocations = connection.prepareStatement(strQueryUpdateLocations);

    //
    F4_CLIENT_ID = props.getProperty("f4.clientId");
    F4_CLIENT_SECRET = props.getProperty("f4.clientSecret");

    //
    TIMER_INTERVAL = Long.valueOf(props.getProperty("task.timer.interval"));

    //
    TASK_WAIT = Long.valueOf(props.getProperty("task.wait", "10000"));

    //
    SELECT_CHECK_DIFF_DAYS =
      Integer.valueOf(props.getProperty("select.check.diffdays", "7"));

    //
    F4_TASK_RADIUS = props.getProperty("f4.radius", "30");
    F4_TASK_EXCEEDED_QUOTA_WAIT =
      Long.valueOf(props.getProperty("f4.exceed-quota.wait", "600000"));

    //
    Timer t = new Timer();
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.println("- Starting new task...");
        if (!RUNNING) {
          startNewTask();
        }
      }
    }, 0, TIMER_INTERVAL);
  }

  /**
   * 
   * @param waitingTime
   * @param radius
   */
  private static void startNewTask() {

    try {
      querySelectLocation.setInt(1, SELECT_CHECK_DIFF_DAYS);
      ResultSet rs = querySelectLocation.executeQuery();
      //
      int count = 0;
      boolean check = true;
      while (rs.next()) {
        int locationId = rs.getInt("id");
        String coord = rs.getString("coord");
        System.out
          .println(++count + "# Retrieving for location_id: " + locationId
            + " at coordinates: " + coord + ". Please wait a bit...");
        Thread.sleep(TASK_WAIT);
        getCoordInfo(locationId, coord);
      }

    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param location_id
   * @param coord
   * @throws Exception
   */
  public static void getCoordInfo(int location_id, String coord) {
    String timeStamp =
      new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

    String f4Query =
      "https://api.foursquare.com/v2/venues/search?ll=" + coord
        + "&lllAcc=10&limit=10&radius=" + F4_TASK_RADIUS
        + "&intent=browse&&client_id=" + F4_CLIENT_ID + "&client_secret="
        + F4_CLIENT_SECRET + "&v=" + timeStamp;

    try {
      String jsonResponse = readUrl(f4Query, null);

      JSONObject jsonObject = new JSONObject(jsonResponse);

      JSONObject response = jsonObject.getJSONObject("response");

      JSONObject meta = jsonObject.getJSONObject("meta");

      int code = meta.getInt("code");

      //if (code.compareTo("403") == 0) {
      if (code == 403) {
        System.out.println("!!! F4 - Exceeded quota... sleeping");
        Thread.sleep(F4_TASK_EXCEEDED_QUOTA_WAIT);
        getCoordInfo(location_id, coord);
        return;
      }

      //
      JSONArray venues;
      try {
        venues = response.getJSONArray("venues");
      }
      catch (Exception e) {
        return;
      }

      for (int i = 0; i < venues.length(); i++) {
        JSONObject json_info = venues.getJSONObject(i);
        String venueId = venues.getJSONObject(i).getString("id");
        String venueName = venues.getJSONObject(i).getString("name");

        System.out.println(venueName);

        JSONObject location = venues.getJSONObject(i).getJSONObject("location");
        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        double distance = location.getDouble("distance");

        String category_name = "", category_pluralname = "", category_shortname =
          "";
        JSONArray categories;
        try {
          categories = venues.getJSONObject(i).getJSONArray("categories");
          System.out.println(venues);
          for (int j = 0; j < categories.length(); j++) {
            String primary = categories.getJSONObject(j).getString("primary");
            if (primary.compareTo("true") == 0) {
              category_name = categories.getJSONObject(j).getString("name");
              category_pluralname =
                categories.getJSONObject(j).getString("pluralName");
              category_shortname =
                categories.getJSONObject(j).getString("shortName");
            }
          }
        }
        catch (Exception e) {
          System.out.println("No categories!");
        }

        //Insertion of F4Location
        queryInsertF4Location.setString(1, venueId);
        queryInsertF4Location.setString(2, venueName);
        queryInsertF4Location.setString(3, String.valueOf(lat));
        queryInsertF4Location.setString(4, String.valueOf(lng));
        queryInsertF4Location.setString(5, category_name);
        queryInsertF4Location.setString(6, category_pluralname);
        queryInsertF4Location.setString(7, category_shortname);
        queryInsertF4Location.setString(8, json_info.toString());
        queryInsertF4Location.executeUpdate();

        //Update of relationship between F4 venues and locations
        queryUpdateLocationF4Location.setInt(1, location_id);
        queryUpdateLocationF4Location.setString(2, venueId);
        queryUpdateLocationF4Location.setString(3, String.valueOf(distance));
        queryUpdateLocationF4Location.executeUpdate();
      }

      //Update contents
      queryUpdateLocations.setDate(1, new Date(System.currentTimeMillis()));
      queryUpdateLocations.setInt(2, location_id);
      queryUpdateLocations.executeUpdate();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method reads the URL contents
   * 
   * @param url_address
   * @param userId
   * @return
   * @throws Exception
   */
  public static String readUrl(String url_address, String userId)
    throws Exception {
    HttpClient client = new HttpClient();
    HttpMethod method = new GetMethod(url_address);
    try {
      int statusCode = client.executeMethod(method);
      byte[] responseBody = method.getResponseBody();
      String response = new String(responseBody);
      return response;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;

    }
    finally {
      method.releaseConnection();
    }
  }
}
