package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class TermSelector {

  private int minDF;
  private int maxDF;

  public TermSelector() {
    this(2, Integer.MAX_VALUE);
  }

  public TermSelector(int minDF, int maxDF) {
    super();
    this.minDF = minDF;
    this.maxDF = maxDF;
  }

  public boolean acceptTerm(Term term, IndexReader reader) throws IOException {
    int df = reader.docFreq(term);
    return df >= minDF && df <= maxDF;
  }
}
