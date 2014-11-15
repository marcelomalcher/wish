package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.SVMClassifierStandaloneExecution;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.DocumentToClassify;

/**
 * 
 * 
 * 
 * @author Mihai
 */
public class SentenceAnalysis {

  public enum Language {
    BRAZILIAN_PORTUGUESE,
    ENGLISH;
  }

  private QueryParser theParser;

  public SentenceAnalysis(Language language, List<String> stopWords) {
    CharArraySet stopWordSet =
      new CharArraySet(Version.LUCENE_44, Arrays.asList(stopWords), true);
    Analyzer analyzer = null;
    switch (language) {
      case BRAZILIAN_PORTUGUESE:
        analyzer = new BrazilianAnalyzer(Version.LUCENE_44, stopWordSet);
        break;

      case ENGLISH:
      default:
        analyzer = new BrazilianAnalyzer(Version.LUCENE_44, stopWordSet);
        break;
    }

    this.theParser = new QueryParser(Version.LUCENE_44, "", analyzer);
  }

  public String[] analyse(String sentence) throws IOException {
    Query query = null;
    try {
      query = theParser.parse(sentence);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }

    if (query == null) {
      return null;
    }

    List<String> list = new ArrayList<String>();
    StringTokenizer tokenizer = new StringTokenizer(query.toString());
    while (tokenizer.hasMoreElements()) {
      list.add(tokenizer.nextElement().toString());
    }

    String[] textArray = new String[list.size()];
    textArray = list.toArray(textArray);

    return textArray;
  }

  /**
   * 
   * @param name
   * @param value
   * @param sentences
   * @return
   */
  public List<DocumentToClassify> getListOfDocumentsToClassify(String name,
    double value, List<String> sentences) {
    List<DocumentToClassify> toRead = new ArrayList<DocumentToClassify>();
    int count = 0;
    try {
      for (String sentence : sentences) {
        //fill the terms with the tokenized representation of the line (bag of words representation)
        String terms[] = analyse(sentence);
        //
        if (terms != null) {
          count++;
          DocumentToClassify q =
            new DocumentToClassify(name + "_" + count, value, terms, -1);
          toRead.add(q);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return toRead;
  }

  public static void main(String[] args) {
    List<String> stopWordList =
      SVMClassifierStandaloneExecution
        .getLinesFromFile("res//stopword//en-stopwords-default.txt");

    CharArraySet stopWordSet =
      new CharArraySet(Version.LUCENE_44, stopWordList, true);

    Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_44, stopWordSet);
    QueryParser parser = new QueryParser(Version.LUCENE_44, "", analyzer);

    try {
      Query query =
        parser
          .parse("politic should be the ones who we are proud of obama is the president");
      List<String> list = new ArrayList<String>();
      StringTokenizer tokenizer = new StringTokenizer(query.toString());
      while (tokenizer.hasMoreElements()) {
        list.add(tokenizer.nextElement().toString());
      }

      String[] textArray = new String[list.size()];
      textArray = list.toArray(textArray);

      System.out.println(textArray.toString());
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
