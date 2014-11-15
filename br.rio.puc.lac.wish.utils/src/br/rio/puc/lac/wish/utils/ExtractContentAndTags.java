package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ExtractContentAndTags {

  private static boolean REPLACE_CHARACTERS = false;
  private static String REPLACE_CHARACTERS_REGEX;
  private static String REPLACE_CHARACTERS_REPLACEMENT;

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length < 3) {
        System.out
          .println("Usage: ExtractContentAndTags <props-file> <file-new> <file-1> ... <file-n>");
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

      //Replace
      REPLACE_CHARACTERS =
        Boolean.valueOf(props
          .getProperty("replace-special-characters", "false"));
      REPLACE_CHARACTERS_REGEX =
        props.getProperty("replace-special-characters-regex", "[-+.^~:,!?]");
      REPLACE_CHARACTERS_REPLACEMENT =
        props.getProperty("replace-special-characters-replacement", " ");

      int totalCounter = 0;

      String outputFileName = args[1];
      FileWriter outputFileWriter = new FileWriter(outputFileName);
      PrintWriter outputStream = new PrintWriter(outputFileWriter);

      for (int i = 2; i < args.length; i++) {
        String inputFileName = args[i];
        System.out.println("Setting file: " + inputFileName);

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);
        BufferedReader inputStream = new BufferedReader(inputFileReader);

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
          //Replace special characters
          if (REPLACE_CHARACTERS) {
            contentLine =
              contentLine.replaceAll(REPLACE_CHARACTERS_REGEX,
                REPLACE_CHARACTERS_REPLACEMENT);
          }

          contentLine += getTagsFromContent(content);
          outputStream.println(contentLine);
          fileCounter++;
          totalCounter++;
        }

        System.out.println("file " + inputFileName + ": " + fileCounter);

        inputStream.close();
      }

      outputStream.close();
    }
    catch (IOException e) {
      System.out.println("IOException:");
      e.printStackTrace();
    }
  }

  private static String getTagsFromContent(Content content) {
    String tags = " ";
    for (String tag : content.getTags()) {
      tags += tag + " ";
    }
    return tags;
  }

  private static String replaceSpecialCharacters(String message) {
    String result = message.replaceAll("", " ");
    return result;
  }
}
