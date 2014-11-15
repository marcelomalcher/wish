package br.rio.puc.lac.wish.extractor.twitter;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class GetAccessToken {
  /**
   * Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key]
   * [consumer secret]
   * 
   * @param args message
   */
  public static void main(String[] args) {
    String fileName = args[0];
    File file = new File(fileName);
    Properties prop = new Properties();
    InputStream is = null;
    OutputStream os = null;
    try {
      if (file.exists()) {
        is = new FileInputStream(file);
        prop.load(is);
      }
      if (args.length < 3) {
        if (null == prop.getProperty("file.name")
          && null == prop.getProperty("oauth.consumerKey")
          && null == prop.getProperty("oauth.consumerSecret")) {
          // consumer key/secret are not set in twitter4j.properties
          System.out
            .println("Usage: java twitter4j.examples.oauth.GetAccessToken [file name] [consumer key] [consumer secret]");
          System.exit(-1);
        }
      }
      else {
        prop.setProperty("oauth.consumerKey", args[1]);
        prop.setProperty("oauth.consumerSecret", args[2]);
        os = new FileOutputStream(fileName);
        prop.store(os, fileName);
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
      System.exit(-1);
    }
    finally {
      if (null != is) {
        try {
          is.close();
        }
        catch (IOException ignore) {
        }
      }
      if (null != os) {
        try {
          os.close();
        }
        catch (IOException ignore) {
        }
      }
    }
    try {
      Twitter twitter = new TwitterFactory().getInstance();
      RequestToken requestToken = twitter.getOAuthRequestToken();
      System.out.println("Got request token.");
      System.out.println("Request token: " + requestToken.getToken());
      System.out.println("Request token secret: "
        + requestToken.getTokenSecret());
      AccessToken accessToken = null;

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while (null == accessToken) {
        System.out
          .println("Open the following URL and grant access to your account:");
        System.out.println(requestToken.getAuthorizationURL());
        try {
          Desktop.getDesktop().browse(
            new URI(requestToken.getAuthorizationURL()));
        }
        catch (IOException ignore) {
        }
        catch (URISyntaxException e) {
          throw new AssertionError(e);
        }
        System.out
          .print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
        String pin = br.readLine();
        try {
          if (pin.length() > 0) {
            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
          }
          else {
            accessToken = twitter.getOAuthAccessToken(requestToken);
          }
        }
        catch (TwitterException te) {
          if (401 == te.getStatusCode()) {
            System.out.println("Unable to get the access token.");
          }
          else {
            te.printStackTrace();
          }
        }
      }
      System.out.println("Got access token.");
      System.out.println("Access token: " + accessToken.getToken());
      System.out
        .println("Access token secret: " + accessToken.getTokenSecret());

      try {
        prop.setProperty("oauth.accessToken", accessToken.getToken());
        prop.setProperty("oauth.accessTokenSecret", accessToken
          .getTokenSecret());
        os = new FileOutputStream(file);
        prop.store(os, "twitter4j.properties");
        os.close();
      }
      catch (IOException ioe) {
        ioe.printStackTrace();
        System.exit(-1);
      }
      finally {
        if (null != os) {
          try {
            os.close();
          }
          catch (IOException ignore) {
          }
        }
      }
      System.out.println("Successfully stored access token to "
        + file.getAbsolutePath() + ".");
      System.exit(0);
    }
    catch (TwitterException te) {
      te.printStackTrace();
      System.out.println("Failed to get accessToken: " + te.getMessage());
      System.exit(-1);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
      System.out.println("Failed to read the system input.");
      System.exit(-1);
    }
  }
}
