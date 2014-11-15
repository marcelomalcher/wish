package br.rio.puc.lac.wish.service.contentsfile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.WebServiceException;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;
import br.rio.puc.lac.wish.service.wscontent.client.ContentClient;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ContentsFromFileToWSContentService {

  /**
   * @param args
   */
  public static void main(String[] args) {
    //Checking parameters
    if (args.length < 2) {
      System.out
        .println("Usage: ContentsFromFileToWSContentService <property-file> <file-1> <file-2> <file-3> ... <file-n>");
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

        String address = props.getProperty("wscontent.address");

        String socialSource = props.getProperty("wscontent.social-source");

        ContentClient contentClientService = new ContentClient(address);

        while ((inLine = inputStream.readLine()) != null) {
          totalCounter++;

          Content content = JSon.getFromJSONString(inLine, Content.class);

          System.out.println(totalCounter + " > " + content.getId() + " - "
            + content.getContent().toString());

          try {
            contentClientService.publishContent(content, socialSource);
          }
          catch (WebServiceException e) {
            System.out.println("Error while trying to publish content: "
              + e.getLocalizedMessage());
            continue;
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
