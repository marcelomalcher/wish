package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.distance;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class RelativeSpatialClassificationDistance implements
  IClassificationDistance {

  private double northEastLatitude;
  private double northEastLongitude;
  private double southWestLatitude;
  private double southWestLongitude;

  /**
   * 
   */
  public RelativeSpatialClassificationDistance(double northEastLatitude,
    double northEastLongitude, double southWestLatitude,
    double southWestLongitude) {
    this.northEastLatitude = northEastLatitude;
    this.northEastLongitude = northEastLongitude;
    this.southWestLatitude = southWestLatitude;
    this.southWestLongitude = southWestLongitude;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public double getDistance(Content content, Content otherContent, float weight) {
    Location thisLocation = content.getLocation();
    Location thatLocation = otherContent.getLocation();
    double latPow =
      Math.pow((Math.abs(thisLocation.getLatitude()
        - thatLocation.getLatitude()))
        / (northEastLatitude - southWestLatitude), 2);
    double lonPow =
      Math.pow((Math.abs(thisLocation.getLongitude()
        - thatLocation.getLongitude()))
        / (northEastLongitude - southWestLongitude), 2);
    double latResult = latPow * Math.pow(weight, 2);
    double lonResult = lonPow * Math.pow(weight, 2);
    double value = latResult + lonResult;
    return value;
  }
}
