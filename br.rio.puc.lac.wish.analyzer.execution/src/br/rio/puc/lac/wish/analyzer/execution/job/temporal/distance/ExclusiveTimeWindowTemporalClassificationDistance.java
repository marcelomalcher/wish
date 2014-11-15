package br.rio.puc.lac.wish.analyzer.execution.job.temporal.distance;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ExclusiveTimeWindowTemporalClassificationDistance implements
  IClassificationDistance {

  /**
   * 
   */
  private long timeWindow;

  /**
   * 
   * @param timeWindow
   */
  public ExclusiveTimeWindowTemporalClassificationDistance(long timeWindow) {
    this.timeWindow = timeWindow;
  }

  public long getTimeWindow() {
    return timeWindow;
  }

  public void setTimeWindow(long timeWindow) {
    this.timeWindow = timeWindow;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getDistance(Content content, Content otherContent, float weight) {
    double distance =
      Math.abs(content.getTimestamp() - otherContent.getTimestamp());
    if (distance <= this.timeWindow) {
      return 0;
    }
    return 1;
  }
}
