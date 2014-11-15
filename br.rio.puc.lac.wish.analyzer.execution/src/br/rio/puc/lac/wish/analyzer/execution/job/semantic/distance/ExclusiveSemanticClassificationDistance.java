package br.rio.puc.lac.wish.analyzer.execution.job.semantic.distance;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ExclusiveSemanticClassificationDistance implements
  IClassificationDistance {

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getDistance(Content content, Content otherContent, float weight) {
    String thisSemantic =
      content.getClassification().getClassification(
        AnalysisResult.Type.SEMANTIC.name());
    String thatSemantic =
      otherContent.getClassification().getClassification(
        AnalysisResult.Type.SEMANTIC.name());
    if (thisSemantic.contains(thatSemantic)) {
      return 0;
    }
    return 1;
  }

}
