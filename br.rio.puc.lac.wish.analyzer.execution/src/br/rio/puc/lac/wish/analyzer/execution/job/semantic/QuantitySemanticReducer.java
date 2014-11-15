package br.rio.puc.lac.wish.analyzer.execution.job.semantic;

import java.util.Set;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.execution.job.ContentMinimumNumberReducer;

/**
 * 
 * 
 * @author Marcelo Malcher
 */
public class QuantitySemanticReducer extends ContentMinimumNumberReducer {

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getRelatedContentValue(Set<Content> contents) {
    int value = 0;
    for (Content content : contents) {
      int contentValue =
        Integer.parseInt(content.getClassification().getInformation(
          AnalysisResult.Type.SEMANTIC.name()));
      //
      value += Math.pow(contentValue, 2);
    }
    return value;
  }
}