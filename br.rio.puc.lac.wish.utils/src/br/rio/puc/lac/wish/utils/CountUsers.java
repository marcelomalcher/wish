package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class CountUsers {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length < 1) {
        System.out.println("Usage: CountUsers <file-1> ... <file-n>");
        System.exit(-1);
      }

      Set<String> totalUsers = new HashSet<String>();

      int totalCounter = 0;

      for (int i = 0; i < args.length; i++) {
        String inputFileName = args[i];
        System.out.println("Setting file: " + inputFileName);

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);
        BufferedReader inputStream = new BufferedReader(inputFileReader);

        Set<String> fileUsers = new HashSet<String>();

        int fileCounter = 0;

        String inLine = null;
        while ((inLine = inputStream.readLine()) != null) {
          Content content = null;
          try {
            content = JSon.getFromJSONString(inLine, Content.class);
          }
          catch (Exception e) {
            System.out.println("Error while trying to get content from JSON: "
              + e.getLocalizedMessage());
            continue;
          }
          String contentLine = content.getContent().toString();

          fileUsers.add(content.getCreatorId().toString());

          fileCounter++;
          totalCounter++;
        }

        System.out.println("file " + inputFileName + ": " + fileCounter
          + " - users: " + fileUsers.size());

        totalUsers.addAll(fileUsers);

        inputStream.close();
      }

      System.out.println("total: " + totalCounter + " - users: "
        + totalUsers.size());

    }
    catch (IOException e) {
      System.out.println("IOException:");
      e.printStackTrace();
    }
  }
}
