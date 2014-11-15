package br.rio.puc.lac.wish.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

public class GenerateAnalysisResultListFromFile {

  public static void main(String[] args) throws Exception {
    //Checking parameters
    if (args.length != 1) {
      System.out.println("The <input-file> parameter is mandatory.");
      System.exit(-1);
    }

    String inputFileName = args[0];

    // Create FileReader Object
    FileReader inputFileReader = new FileReader(inputFileName);

    // Create Buffered/PrintWriter Objects
    BufferedReader inputStream = new BufferedReader(inputFileReader);

    List<AnalysisResult> results = new ArrayList<AnalysisResult>();

    HashMap<String, Class> classMap = new HashMap<String, Class>();
    classMap.put("contents", Content.class);

    String inLine = null;
    while ((inLine = inputStream.readLine()) != null) {

      AnalysisResult result =
        JSon.getFromJSONString(inLine, AnalysisResult.class, classMap);

      //
      results.add(result);

      //
      System.out.println("Analysis Result: " + result.getId() + " - "
        + result.getSemantics().toString());

      for (Content c : result.getContents()) {
        System.out.println("---> Content: " + c.getId() + " - "
          + c.getContent());
      }
    }

    System.out.println("# Results list size: " + results.size());

    inputStream.close();
  }

}
