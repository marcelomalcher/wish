package br.rio.puc.lac.wish.service.semantic.svmclassifier.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import br.rio.puc.lac.wish.service.semantic.svmclassifier.model.DocumentToClassify;

/**
 * 
 * 
 * 
 * @author Mihai
 */
public class SentenceAnalysis {

  private String languageName;

  /**
    * 
    */
  private String[] stopWords;

  public SentenceAnalysis(String languageName, String[] stopWords) {
    this.languageName = languageName;
    this.stopWords = stopWords;
  }

  public String[] analyse(String sentence) throws IOException {
    //Creating SnowballAnalyzer object
    SnowballAnalyzer sbanalyzer =
      new SnowballAnalyzer(Version.LUCENE_33, languageName, stopWords);

    // 
    final StringReader sr = new StringReader(sentence);
    final TokenStream ts = sbanalyzer.tokenStream("English", sr);
    ts.addAttribute(TermAttribute.class);

    List<String> list = new ArrayList<String>();
    while (ts.incrementToken()) {
      TermAttribute ta = ts.getAttribute(TermAttribute.class);
      if (ta.term() != null) {
        list.add(ta.term().toString());
      }
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
}
