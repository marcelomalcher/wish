package br.rio.puc.lac.wish.analyzer.execution.job;

import br.rio.puc.lac.wish.analyzer.commons.Content;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public interface IClassificationDistance {

  /**
   * 
   * @param content
   * @param otherContent
   * @param weight
   * @return
   */
  double getDistance(Content content, Content otherContent, float weight);

}
