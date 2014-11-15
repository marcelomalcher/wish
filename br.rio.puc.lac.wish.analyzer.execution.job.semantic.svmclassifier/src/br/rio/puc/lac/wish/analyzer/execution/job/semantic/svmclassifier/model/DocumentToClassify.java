package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model;

import java.util.Arrays;
import java.util.StringTokenizer;

import libsvm.svm_node;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class DocumentToClassify {

  // the text of the document
  String query;

  // the class value to which it belongs
  double value;

  // an id to differentiate between instances
  int templateId;

  // the bag of words terms 
  String text[];

  // the svm feature vector representation
  svm_node[] svm_fv;

  /**
   * 
   * @param query
   * @param value
   * @param text
   * @param templateId
   */
  public DocumentToClassify(String query, double value, String text[],
    int templateId) {
    this.query = query;
    this.value = value;
    this.text = text;
    this.templateId = templateId;
  }

  public void addText(String text[]) {
    String result[] = new String[this.text.length + text.length];

    int i = 0;
    for (; i < this.text.length; i++) {
      result[i] = this.text[i];
    }
    for (int k = 0; k < text.length; i++, k++) {
      result[i] = text[k];
    }
    this.text = result;
  }

  public String getQuery() {
    return this.query;
  }

  public String[] getBagOfFeatures() {
    return this.text;
  }

  public double getValue() {
    return this.value;
  }

  public svm_node[] getSVMFeatureVector() {
    return this.svm_fv;
  }

  public void setSVMFeatureVector(String line) {
    StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
    double target = Double.valueOf(st.nextToken()).doubleValue();
    int m = st.countTokens() / 2;
    svm_node[] x = new svm_node[m];
    for (int j = 0; j < m; j++) {
      x[j] = new svm_node();
      x[j].index = Integer.parseInt(st.nextToken());
      x[j].value = Double.valueOf(st.nextToken()).doubleValue();
    }
    this.svm_fv = x;
  }

  @Override
  public String toString() {
    return "Query [query=" + query + ", svm_fv=" + Arrays.toString(svm_fv)
      + ", templateId=" + templateId + ", text=" + Arrays.toString(text)
      + ", value=" + value + "]";
  }
}