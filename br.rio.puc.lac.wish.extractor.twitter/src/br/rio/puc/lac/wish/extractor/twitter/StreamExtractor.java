package br.rio.puc.lac.wish.extractor.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class StreamExtractor {

  static int counter;

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {

    if (args.length < 1) {
      System.out
        .println("Usage: StreamExtractor <output-file> keywords (optional)");
      System.exit(-1);
    }

    //File_name
    String fileName = args[0];
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
    if (args.length > 1) {
      keywords = new String[args.length - 5];
      for (int i = 5; i < args.length; i++) {
        keywords[i - 5] = args[i];
      }
    }

    counter = 0;

    //Obtaining TwitterStream object 
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

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
        if (status.getPlace() != null) {
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

    twitterStream.sample();

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

    System.out.println("StreamExtractor terminated.");

    //
    System.exit(0);
  }
}
