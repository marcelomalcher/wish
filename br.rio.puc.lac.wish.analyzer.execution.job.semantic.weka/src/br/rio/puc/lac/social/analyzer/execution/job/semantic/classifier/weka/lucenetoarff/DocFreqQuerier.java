package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff;

import java.io.IOException;

public interface DocFreqQuerier {

  public double docFreq(String fieldName, String term) throws IOException;
}
