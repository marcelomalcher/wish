package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class InsertIdentificationInContents {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length < 1) {
        System.out
          .println("Usage: InsertIdentificationInContents <file-1> <file-2> ... <file-n>");
        System.exit(-1);
      }

      for (String file : args) {
        System.out.println("Changing file: " + file);

        // input/output file names
        String inputFileName = file;
        String outputFileName = file + ".out";

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);
        FileWriter outputFileWriter = new FileWriter(outputFileName);

        // Create Buffered/PrintWriter Objects
        BufferedReader inputStream = new BufferedReader(inputFileReader);
        PrintWriter outputStream = new PrintWriter(outputFileWriter);

        String inLine = null;
        while ((inLine = inputStream.readLine()) != null) {
          Content content = JSon.getFromJSONString(inLine, Content.class);
          content.setId(UUID.randomUUID().toString());
          outputStream.println(JSon.toJSONString(Content.class, content));
        }

        outputStream.close();
        inputStream.close();
      }
    }
    catch (IOException e) {

      System.out.println("IOException:");
      e.printStackTrace();

    }

  }
}
