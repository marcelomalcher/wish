package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class FirstAndLastDateTime {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length < 1) {
        System.out
          .println("Usage: FirstAndLastDateTime <file-1> <file-2> ... <file-n>");
        System.exit(-1);
      }

      int totalCounter = 0;

      long init = Long.MAX_VALUE;
      long end = Long.MIN_VALUE;

      System.out.println("### First and Last ###");

      for (int i = 0; i < args.length; i++) {
        String inputFileName = args[i];
        System.out.println("Setting file: " + inputFileName);

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);

        // Create Buffered/PrintWriter Objects
        BufferedReader inputStream = new BufferedReader(inputFileReader);

        int fileCounter = 0;

        long fInit = Long.MAX_VALUE;
        long fEnd = Long.MIN_VALUE;

        String inLine = null;
        while ((inLine = inputStream.readLine()) != null) {
          Content content = null;
          try {
            content = JSon.getFromJSONString(inLine, Content.class);
          }
          catch (Exception e) {
            System.out.println("Error while trying to get from JSON String: "
              + e.getLocalizedMessage());
            fileCounter++;
            totalCounter++;
            continue;
          }

          //All
          if (init > content.getTimestamp()) {
            init = content.getTimestamp();
          }
          if (end < content.getTimestamp()) {
            end = content.getTimestamp();
          }

          //File
          if (fInit > content.getTimestamp()) {
            fInit = content.getTimestamp();
          }
          if (fEnd < content.getTimestamp()) {
            fEnd = content.getTimestamp();
          }

          fileCounter++;
          totalCounter++;
        }

        System.out.println("file " + inputFileName + ": " + fileCounter);
        System.out.println("# -> File init : " + fInit + " - "
          + SimpleDateFormat.getInstance().format(new Date(fInit)));
        System.out.println("# -> File end  : " + fEnd + " - "
          + SimpleDateFormat.getInstance().format(new Date(fEnd)));

        inputStream.close();
      }

      //
      System.out.println("###############################");
      System.out.println("# -> Total: " + totalCounter);
      System.out.println("# -> Init : " + init + " - "
        + SimpleDateFormat.getInstance().format(new Date(init)));
      System.out.println("# -> End  : " + end + " - "
        + SimpleDateFormat.getInstance().format(new Date(end)));
    }
    catch (IOException e) {

      System.out.println("IOException:");
      e.printStackTrace();

    }

  }
}
