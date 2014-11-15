package br.rio.puc.lac.wish.service.wscontent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.Endpoint;

/**
 * 
 * 
 * 
 * @author Tecgraf
 */
public class WSContentEndpointPublisher {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: WSContentEndpointPublisher <properties-file>");
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

    //Address
    String address = props.getProperty("wscontent.address");

    //Database
    String driver = props.getProperty("database.jdbc-driver");
    String connectionString = props.getProperty("database.connection-string");

    //Timer
    long timerInitial = Long.valueOf(props.getProperty("timer.initial"));
    long timerInterval = Long.valueOf(props.getProperty("timer.interval"));

    Endpoint.publish(address, new ContentService(driver, connectionString,
      timerInitial, timerInterval));

    //
    System.out.println("Content Service is running at: " + address);
  }
}
