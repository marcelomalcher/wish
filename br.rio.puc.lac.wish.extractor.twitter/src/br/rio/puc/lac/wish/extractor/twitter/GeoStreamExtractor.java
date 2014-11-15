package br.rio.puc.lac.wish.extractor.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.ws.WebServiceException;

import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.utils.JSon;
import br.rio.puc.lac.wish.service.wscontent.client.ContentClient;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class GeoStreamExtractor {

  private static int counter = 0;

  private static ConcurrentLinkedQueue<Content<String, String>> queue =
    new ConcurrentLinkedQueue<Content<String, String>>();

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      System.out
        .println("Usage: GeoStreamExtractor <properties-file> <output-file>");
      System.out
        .println("The <properties-file> and <output-file> parameters are mandatory.");
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

    //Coordinates
    double swLat =
      Float.parseFloat(props.getProperty("spatial.southwest-latitude"));
    double swLon =
      Float.parseFloat(props.getProperty("spatial.southwest-longitude"));
    double neLat =
      Float.parseFloat(props.getProperty("spatial.northeast-latitude"));
    double neLon =
      Float.parseFloat(props.getProperty("spatial.northeast-longitude"));

    //File_name
    String fileName = args[1];
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(fileName);
    }
    catch (IOException e) {
      System.out.println("Error while creating file for storage: " + fileName);
      e.printStackTrace();
      System.exit(-1);
    }
    final BufferedWriter writer = new BufferedWriter(fileWriter);

    //Keywords
    String[] keywords = null;
    String keyWordList = props.getProperty("filter.keywords");
    if ((keyWordList != null) && (!keyWordList.equals(""))) {
      keywords = keyWordList.split(",");
    }

    //Obtaining TwitterStream object 
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true);
    cb.setOAuthConsumerKey(props.getProperty("twitter.oauth.consumerKey"));
    cb.setOAuthConsumerSecret(props.getProperty("twitter.oauth.consumerSecret"));
    cb.setOAuthAccessToken(props.getProperty("twitter.oauth.accessToken"));
    cb.setOAuthAccessTokenSecret(props
      .getProperty("twitter.oauth.accessTokenSecret"));
    //
    TwitterStream twitterStream =
      new TwitterStreamFactory(cb.build()).getInstance();

    //Creating listener
    StatusListener listener = new StatusListener() {
      @Override
      public void onStatus(Status status) {
        counter++;
        //printing
        System.out.println(counter + " " + status.getText() + "\n");

        //storing
        Content<String, String> content = new Content<String, String>();
        content.setId(String.valueOf(status.getId()));
        content.setCreatorId(status.getUser().getScreenName());
        content.setContent(status.getText());
        //
        HashtagEntity[] hashtags = status.getHashtagEntities();
        if ((hashtags != null) && (hashtags.length > 0)) {
          String[] tags = new String[hashtags.length];
          for (int i = 0; i < hashtags.length; i++) {
            tags[i] = hashtags[i].getText();
          }
          content.setTags(tags);
        }
        Place place = new Place();
        place.setName(status.getPlace().getName());
        place.setId(status.getPlace().getId());
        place.setCountryCode(status.getPlace().getCountryCode());
        place.setStreetAddress(status.getPlace().getStreetAddress());
        place.setPlaceType(status.getPlace().getPlaceType());
        Location location = new Location();
        location.setPlace(place);
        content.setLocation(location);
        if (status.getGeoLocation() != null) {
          content.getLocation().setLatitude(
            status.getGeoLocation().getLatitude());
          content.getLocation().setLongitude(
            status.getGeoLocation().getLongitude());
        }
        content.setTimestamp(status.getCreatedAt().getTime());

        try {
          String jsonString = JSon.toJSONString(Content.class, content);
          writer.write(jsonString + "\n");
        }
        catch (IOException e) {
          System.out.println("Error while writing tweet as Content.");
          e.printStackTrace();
          System.exit(-1);
        }

        //Putting in the queue
        queue.offer(content);
      }

      @Override
      public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
      }

      @Override
      public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
      }

      @Override
      public void onScrubGeo(long userId, long upToStatusId) {
      }

      @Override
      public void onException(Exception ex) {
        ex.printStackTrace();
      }

      @Override
      public void onStallWarning(StallWarning arg0) {

      }
    };
    twitterStream.addListener(listener);

    double[][] locations =
      new double[][] { new double[] { swLon, swLat },
          new double[] { neLon, neLat } };
    twitterStream.filter(new FilterQuery(0, null, keywords, locations));

    //
    boolean wscontent = Boolean.valueOf(props.getProperty("wscontent.active"));

    if (wscontent) {
      long timerInitial =
        Long.valueOf(props.getProperty("wscontent.timer.initial"));
      long timerInterval =
        Long.valueOf(props.getProperty("wscontent.timer.interval"));

      final String address = props.getProperty("wscontent.address");
      final String socialSource = props.getProperty("wscontent.social-source");

      //
      Timer t = new Timer();
      t.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          System.out
            .println("- Starting task to send contents to ContentService...");
          System.out.println("--- Size of queue: " + queue.size());

          ContentClient contentClientService = new ContentClient(address);

          while (!queue.isEmpty()) {
            Content<String, String> content = queue.poll();
            if (content != null) {
              try {
                contentClientService.publishContent(content, socialSource);
              }
              catch (WebServiceException ex) {
                System.err.println("Error while connecting to wscontent: "
                  + ex.getLocalizedMessage());
              }
            }
          }
        }
      }, timerInitial, timerInterval);
    }

    //
    BufferedReader reader =
      new BufferedReader(new InputStreamReader(System.in));

    String end = "";
    while (!end.equalsIgnoreCase("end")) {
      System.out.print("Enter 'end' to finish the extractor: ");
      try {
        end = reader.readLine();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    //
    twitterStream.cleanUp();

    //
    try {
      reader.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    try {
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("GeoStreamExtractor terminated.");

    //
    System.exit(0);
  }
}
