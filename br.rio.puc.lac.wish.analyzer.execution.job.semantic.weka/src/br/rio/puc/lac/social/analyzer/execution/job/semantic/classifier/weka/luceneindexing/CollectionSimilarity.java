package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.luceneindexing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.search.DefaultSimilarity;

public class CollectionSimilarity extends DefaultSimilarity {
  private static final long serialVersionUID = 8216309055856685638L;
  private static Map<String, Long> fieldLengths = new HashMap<String, Long>();

  @Override
  public float lengthNorm(String fieldName, int numTokens) {
    Long aux = CollectionSimilarity.fieldLengths.get(fieldName);
    if (aux == null) {
      aux = new Long(0);
    }
    aux += numTokens;
    CollectionSimilarity.fieldLengths.put(fieldName, aux);
    return super.lengthNorm(fieldName, numTokens);
  }

  public static Map<String, Long> getFieldLengths() {
    return CollectionSimilarity.fieldLengths;
  }

  public static long getLength(String field) {
    return CollectionSimilarity.fieldLengths.get(field);
  }

  public static Map<String, Double> getNormalizedFieldLengths(int numDocs) {
    double norm = numDocs;
    Map<String, Long> fl = getFieldLengths();
    Map<String, Double> nfl = new HashMap<String, Double>(fl.size());
    for (Entry<String, Long> fieldLength : fl.entrySet()) {
      nfl.put(fieldLength.getKey(), fieldLength.getValue() / norm);
    }
    return nfl;
  }

}
