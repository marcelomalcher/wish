package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.CollectionUtils;

public class Coordinator {

  private static final double K1 = 2.0;
  private static final double B = 0.75;
  private Map<String, Double> averageFieldLengths;

  public Coordinator(Map<String, Double> averageFieldLengths) {
    this.averageFieldLengths = averageFieldLengths;
  }

  public Coordinator(File avgFieldLengthFile) throws NumberFormatException,
    IOException {
    this(Coordinator.getAvgFieldLengthMap(avgFieldLengthFile));
  }

  /**
   * Computes the BM25 weight of the term.
   * 
   * @param term
   * @param tf
   * @param numTerms
   * @param docFreq
   * @return
   */
  public double coordinateBM25(String fieldName, double tf, double numTerms,
    double docFreq, double numDocs) {
    double idf = Math.log((numDocs - docFreq + 0.5) / (docFreq + 0.5));
    if (idf < 0) {
      idf = 0;
    }

    double avgl = averageFieldLengths.get(fieldName);

    double bm = (tf * (K1 + 1)) / (tf + K1 * (1 - B + B * numTerms / avgl));

    double bm25 = idf * bm;

    return bm25;
  }

  /**
   * Computes tfidf
   * 
   * @param term
   * @param tf
   * @param numTerms
   * @param docFreq
   * @return
   */
  public double coordinate(String fieldName, double tf, double numTerms,
    double docFreq, double numDocs) {

    tf = tf / numTerms;
    double idf = Math.log(numDocs / docFreq);
    //double idf = 1;
    double tfidf = tf * idf;

    return tfidf;
  }

  public static Map<String, Double> getAvgFieldLengthMap(File avgFieldLengthFile)
    throws NumberFormatException, IOException {
    Map<String, Double> avgLengths = new HashMap<String, Double>();
    BufferedReader in = new BufferedReader(new FileReader(avgFieldLengthFile));
    String line;
    while (null != (line = in.readLine())) {
      String field = line;
      Double avg = new Double(in.readLine());
      avgLengths.put(field, avg);
    }
    in.close();
    return avgLengths;
  }

  public double[] bidirectionalScore(String fieldName,
    Map<String, Integer> freqVector0, Map<String, Integer> freqVector1,
    DocFreqQuerier docFreqQuerier, double numDocs) throws IOException {
    Collection<String> commonTerms = getCommonTerms(freqVector0, freqVector1);
    double[] scores = new double[2];
    scores[0] =
      score(fieldName, freqVector1, docFreqQuerier, numDocs, commonTerms);
    scores[1] =
      score(fieldName, freqVector0, docFreqQuerier, numDocs, commonTerms);
    return scores;
  }

  public double score(String fieldName, Map<String, Integer> queryFreqVector,
    Map<String, Integer> docFreqVector, DocFreqQuerier docFreqQuerier,
    double numDocs) throws IOException {

    Collection<String> commonTerms =
      getCommonTerms(queryFreqVector, docFreqVector);
    return score(fieldName, docFreqVector, docFreqQuerier, numDocs, commonTerms);
  }

  private Collection<String> getCommonTerms(
    Map<String, Integer> queryFreqVector, Map<String, Integer> docFreqVector) {
    @SuppressWarnings("unchecked")
    Collection<String> commonTerms =
      CollectionUtils.intersection(queryFreqVector.keySet(), docFreqVector
        .keySet());
    return commonTerms;
  }

  private double score(String fieldName, Map<String, Integer> docFreqVector,
    DocFreqQuerier docFreqQuerier, double numDocs,
    Collection<String> commonTerms) throws IOException {
    int numDocTerms = sum(docFreqVector.values());
    double score = 0;
    for (String term : commonTerms) {
      score +=
        coordinate(fieldName, docFreqVector.get(term), numDocTerms,
          docFreqQuerier.docFreq(fieldName, term), numDocs);
    }
    return score;
  }

  private int sum(Collection<Integer> values) {
    int sum = 0;
    for (int i : values) {
      sum += i;
    }
    return sum;
  }

}
