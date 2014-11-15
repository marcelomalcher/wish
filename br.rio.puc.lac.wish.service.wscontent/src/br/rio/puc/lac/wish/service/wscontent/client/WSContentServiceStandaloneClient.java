package br.rio.puc.lac.wish.service.wscontent.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;

/**
 * @author siva
 * 
 */
public class WSContentServiceStandaloneClient {

  public static void main(String[] args) throws Exception {

    if (args.length != 1) {
      System.out
        .println("Usage: WSContentServiceStandaloneClient <properties-file>");
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

    ContentClient contentClientService = new ContentClient(address);

    //Content
    Content<String, String> content = new Content<String, String>();
    content.setId("WSTestId-X");
    content.setContent("The content of this content...what?!?");
    content.setCreatorId("lac");
    content.setTags(new String[] { "test", "contentservice", "lac", "wish" });
    content.setTimestamp(System.currentTimeMillis());
    content.setLocation(new Location(-1.526919d, -48.515625d));

    contentClientService.publishContent(content, "WSTEST");
  }
}