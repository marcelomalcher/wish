package br.rio.puc.lac.wish.utils.keyworddbpedia;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * App for testing access to DBPedia-SpotLight API
 * 
 * @author Marcelo Malcher
 */
public class EntityExtractionUsingDBPediaSpotlight {

  /**
   * Main method for app
   * 
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: KeywordExtraction <properties-file>");
      System.out.println("The <properties-file> parameters is mandatory.");
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

    //Setting parameters for DBPedia-Spotlight WS
    String address = props.getProperty("dbpedia.spotlight.address");
    String confidence = props.getProperty("dbpedia.spotlight.confidence", "0");
    String support = props.getProperty("dbpedia.spotlight.support", "0");

    //Data to analyze
    String expression = props.getProperty("analysis.expression");

    try {

      String url =
        address + "text=" + URLEncoder.encode(expression, "utf-8")
          + "&confidence=" + confidence + "&support=" + support;

      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpGet getRequest = new HttpGet(url);
      getRequest.addHeader("Accept", "application/json");

      HttpResponse response = httpClient.execute(getRequest);

      if (response.getStatusLine().getStatusCode() != 200) { //Checking HTTP ERROR
        throw new RuntimeException("Failed : HTTP error code : "
          + response.getStatusLine().getStatusCode());
      }

      BufferedReader br =
        new BufferedReader(new InputStreamReader((response.getEntity()
          .getContent())));

      String responseBody = "";
      String output;
      System.out.println("Output from Server .... \n");
      while ((output = br.readLine()) != null) {
        responseBody += output;
        System.out.println(output);
      }

      httpClient.getConnectionManager().shutdown();

      LinkedList<DBpediaResource> resources = getResources(responseBody);

      for (DBpediaResource res : resources) {
        System.out.println("Surface: " + res.toString());
      }

    }
    catch (MalformedURLException e) {

      e.printStackTrace();

    }
    catch (IOException e) {

      e.printStackTrace();

    }
    catch (AnnotationException e) {

      e.printStackTrace();
    }
  }

  /**
   * Dealing with response from DBPedia Spotlight API as shown in webpage
   * 
   * @param spotlightResponse
   * @return a list of resources from JSON response
   * @throws AnnotationException
   */
  private static LinkedList<DBpediaResource> getResources(
    String spotlightResponse) throws AnnotationException {
    JSONObject resultJSON = null;
    JSONArray entities = null;

    try {
      resultJSON = new JSONObject(spotlightResponse);
      entities = resultJSON.getJSONArray("Resources");
    }
    catch (JSONException e) {
      throw new AnnotationException(
        "Received invalid response from DBpedia Spotlight API.");
    }

    LinkedList<DBpediaResource> resources = new LinkedList<DBpediaResource>();
    for (int i = 0; i < entities.length(); i++) {
      try {
        JSONObject entity = entities.getJSONObject(i);
        resources.add(new DBpediaResource(entity.getString("@surfaceForm"),
          Integer.parseInt(entity.getString("@support"))));

      }
      catch (JSONException e) {
        System.out.println("JSON exception " + e);
      }

    }

    return resources;
  }
}
