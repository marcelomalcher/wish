package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.distance;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ExclusiveBoxSpatialClassificationDistance implements
  IClassificationDistance {

  /**
   * 
   */
  private double width;

  private double height;

  /**
   * 
   * @param width
   * @param height
   */
  public ExclusiveBoxSpatialClassificationDistance(double width, double height) {
    this.width = width;
    this.height = height;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getDistance(Content content, Content otherContent, float weight) {
    if ((Math.abs(content.getLocation().getLatitude()
      - otherContent.getLocation().getLatitude()) <= width)
      && (Math.abs(content.getLocation().getLongitude()
        - otherContent.getLocation().getLongitude()) <= height)) {
      return 0;
    }
    return 1;
  }
}
