package br.rio.puc.lac.wish.analyzer.execution.job.temporal.distance;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class RelativeTemporalClassificationDistance implements
  IClassificationDistance {

  /**
   * 
   */
  private long initialTime;

  /**
   * 
   */
  private long endTime;

  /**
   * 
   * @param initialTime
   * @param endTime
   */
  public RelativeTemporalClassificationDistance(long initialTime, long endTime) {
    this.initialTime = initialTime;
    this.endTime = endTime;
  }

  /**
   * 
   * @return
   */
  public long getInitialTime() {
    return initialTime;
  }

  /**
   * 
   * @param initialTime
   */
  public void setInitialTime(long initialTime) {
    this.initialTime = initialTime;
  }

  /**
   * 
   * @return
   */
  public long getEndTime() {
    return endTime;
  }

  /**
   * 
   * @param endTime
   */
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getDistance(Content content, Content otherContent, float weight) {
    long thisResult = content.getTimestamp();
    long thatResult = otherContent.getTimestamp();
    double timePow =
      Math.pow(Math.max(thisResult, thatResult)
        - Math.min(thisResult, thatResult), 2);
    double maxPow = Math.pow(endTime - initialTime, 2);
    return (timePow / maxPow) * Math.pow(weight, 2);

  }

}
