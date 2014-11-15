package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GenerateHTMLOutput {
  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //Checking parameters
      if (args.length < 3) {
        System.out
          .println("Usage: GenerateHTMLOutput <output-file> <output-template-file> <file-1> <file-2> ... <file-n>");
        System.exit(-1);
      }

      Set<String> results = new HashSet<String>();

      for (int i = 2; i < args.length; i++) {

        String inputFileName = args[i];
        System.out.println("Setting file: " + inputFileName);

        // Create FileReader Object
        FileReader inputFileReader = new FileReader(inputFileName);

        // Create Buffered/PrintWriter Objects
        BufferedReader inputStream = new BufferedReader(inputFileReader);

        int fileCounter = 0;

        String inLine = null;
        while ((inLine = inputStream.readLine()) != null) {
          //
          results.add(inLine);
          //
          fileCounter++;
        }

        System.out.println("# File " + inputFileName + ": " + fileCounter);

        inputStream.close();
      }

      //
      String compositeResults = "";
      int count = 0;
      for (String r : results) {
        r = r.replaceAll("\n", " ");

        r = r.replaceAll("\\\\", "\\\\\\\\");
        r = r.replaceAll("\"", "\\\\\"");

        compositeResults += "results[" + count + "] = \"" + r + "\"; \n";
        count++;
      }

      generateHTMLOutputFile(args[0], args[1], compositeResults);

    }
    catch (IOException e) {

      System.out.println("IOException:");
      e.printStackTrace();

    }

  }

  public static void generateHTMLOutputFile(String outputFileName,
    String templateFileName, String results) throws IOException {
    System.out.println("Starting...");
    System.out.println(results);

    File outputFile = new File(outputFileName);
    File templateFile = new File(templateFileName);

    BufferedReader br = new BufferedReader(new FileReader(templateFile));
    String line = null;
    String sb = "";
    while ((line = br.readLine()) != null) {
      if (line.indexOf("XXX_RESULTS_XXX") != -1) {
        line = line.replace("XXX_RESULTS_XXX", results);
      }
      sb += line + "\r";
    }
    br.close();

    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
    bw.write(sb.toString());
    bw.close();
    System.out.println("Done!");
  }
}
