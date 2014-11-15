package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class CountContents {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length != 1) {
        System.out
          .println("Usage: CountContents <file-1> <file-2> ... <file-n>");
        System.exit(-1);
      }

      int totalCounter = 0;

      for (int i = 0; i < args.length; i++) {
        String inputFileName = args[i];
        System.out.println("Setting file: " + inputFileName);

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);

        // Create Buffered/PrintWriter Objects
        BufferedReader inputStream = new BufferedReader(inputFileReader);

        int fileCounter = 0;

        String inLine = null;
        while ((inLine = inputStream.readLine()) != null) {
          //Content content = JSon.getFromJSONString(inLine, Content.class);
          fileCounter++;
          totalCounter++;
        }

        System.out.println("file " + inputFileName + ": " + fileCounter);

        inputStream.close();
      }

      System.out.println("total: " + totalCounter);
    }
    catch (IOException e) {

      System.out.println("IOException:");
      e.printStackTrace();

    }

  }
}
