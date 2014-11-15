package br.rio.puc.lac.wish.analyzer.commons;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class Region implements Comparable<Region> {

  /**
   * 
   */
  private Location southWest;

  /**
   * 
   */
  private Location northEast;

  /**
   * 
   */
  public Region() {
    super();
  }

  /**
   * 
   * @param southWest
   * @param northEast
   */
  public Region(Location southWest, Location northEast) {
    if (southWest.getLatitude() >= northEast.getLatitude()) {
      throw new IllegalArgumentException(
        "Northeast latitude must be GREATER than southwest latitude.");
    }
    if (southWest.getLongitude() >= northEast.getLongitude()) {
      throw new IllegalArgumentException(
        "Northeast longitude must be GREATER than southwest longitude.");
    }
    this.southWest = southWest;
    this.northEast = northEast;
  }

  /**
   * 
   * @return
   */
  public Location getSouthWest() {
    return southWest;
  }

  /**
   * 
   * @param southWest
   */
  public void setSouthWest(Location southWest) {
    this.southWest = southWest;
  }

  /**
   * 
   * @return
   */
  public Location getNorthEast() {
    return northEast;
  }

  /**
   * 
   * @param northEast
   */
  public void setNorthEast(Location northEast) {
    this.northEast = northEast;
  }

  /**
   * 
   * @param out
   * @throws IOException
   */
  public void write(DataOutput out) throws IOException {
    this.getSouthWest().write(out);
    this.getNorthEast().write(out);
  }

  /**
   * 
   * @param in
   * @return
   * @throws IOException
   */
  public void read(DataInput in) throws IOException {
    Location southWestLocation = new Location();
    southWestLocation.read(in);
    Location northEastLocation = new Location();
    northEastLocation.read(in);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Region [northEast=" + northEast + ", southWest=" + southWest + "]";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((northEast == null) ? 0 : northEast.hashCode());
    result = prime * result + ((southWest == null) ? 0 : southWest.hashCode());
    return result;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Region other = (Region) obj;
    if (northEast == null) {
      if (other.northEast != null)
        return false;
    }
    else if (!northEast.equals(other.northEast))
      return false;
    if (southWest == null) {
      if (other.southWest != null)
        return false;
    }
    else if (!southWest.equals(other.southWest))
      return false;
    return true;
  }

  /**
   * 
   * @param boundingBox
   * @return
   */
  public static Location getRandomLocationInsideRegion(Region boundingBox) {
    double latitude =
      boundingBox.getSouthWest().getLatitude()
        + (Math.random()
          * (boundingBox.getNorthEast().getLatitude() - boundingBox
            .getSouthWest().getLatitude()) + 1);
    double longitude =
      boundingBox.getSouthWest().getLongitude()
        + (Math.random()
          * (boundingBox.getNorthEast().getLongitude() - boundingBox
            .getSouthWest().getLongitude()) + 1);
    ;
    return new Location(latitude, longitude);
  }

  /**
   * 
   * @param Location
   */
  public void validateLocation(Location Location) {
    if (Location.getLatitude() < this.getSouthWest().getLatitude())
      Location.setLatitude(this.getSouthWest().getLatitude());
    if (Location.getLatitude() > this.getNorthEast().getLatitude())
      Location.setLatitude(this.getNorthEast().getLatitude());
    if (Location.getLongitude() < this.getSouthWest().getLongitude())
      Location.setLongitude(this.getSouthWest().getLongitude());
    if (Location.getLongitude() > this.getNorthEast().getLongitude())
      Location.setLongitude(this.getNorthEast().getLongitude());
  }

  /**
   * 
   * @param location
   * @return
   */
  public boolean isLocationInside(Location location) {
    return location.getLatitude() >= getSouthWest().getLatitude()
      && location.getLatitude() <= getNorthEast().getLatitude()
      && location.getLongitude() >= getSouthWest().getLongitude()
      && location.getLongitude() <= getNorthEast().getLongitude();
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Region that) {
    final int EQUAL = 0;
    final int AFTER = 1;

    if (this == that)
      return EQUAL;

    if (that == null) {
      return AFTER;
    }

    int result = this.southWest.compareTo(that.southWest);
    if (result != EQUAL)
      return result;

    result = this.northEast.compareTo(that.northEast);
    if (result != EQUAL)
      return result;

    return EQUAL;
  }

}
