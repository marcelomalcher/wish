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
public class Location implements Comparable<Location> {

  /**
   * 
   */
  private Double latitude;

  /**
   * 
   */
  private Double longitude;

  /**
   * 
   */
  private Place place;

  /**
   * 
   */
  public Location() {
    super();
  }

  /**
   * 
   * @param latitude
   * @param longitude
   */
  public Location(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * 
   * @param latitude
   * @param longitude
   * @param place
   */
  public Location(Double latitude, Double longitude, Place place) {
    this(latitude, longitude);
    this.place = place;
  }

  /**
   * 
   * @return
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * 
   * @return
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * 
   * @param latitude
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * 
   * @param longitude
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  /**
   * 
   * @return
   */
  public Place getPlace() {
    return place;
  }

  /**
   * 
   * @param place
   */
  public void setPlace(Place place) {
    this.place = place;
  }

  /**
   * 
   * @param out
   * @throws IOException
   */
  public void write(DataOutput out) throws IOException {
    out.writeDouble(this.getLatitude());
    out.writeDouble(this.getLongitude());
    if (place != null) {
      out.writeBoolean(true);
      out.writeUTF(place.getId());
      out.writeUTF(place.getName());
      out.writeUTF(place.getPlaceType());
      out.writeUTF(place.getStreetAddress());
      out.writeUTF(place.getCountryCode());
    }
    else {
      out.writeBoolean(false);
    }
  }

  /**
   * 
   * @param in
   * @return
   * @throws IOException
   */
  public void read(DataInput in) throws IOException {
    this.setLatitude(in.readDouble());
    this.setLongitude(in.readDouble());
    if (in.readBoolean()) {
      Place place = new Place();
      place.setId(in.readUTF());
      place.setName(in.readUTF());
      place.setPlaceType(in.readUTF());
      place.setStreetAddress(in.readUTF());
      place.setCountryCode(in.readUTF());
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Location [latitude=" + latitude + ", longitude=" + longitude
      + ", place=" + place + "]";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Location that) {
    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;

    if (this == that)
      return EQUAL;

    if (that == null) {
      return AFTER;
    }

    if (this.latitude < that.latitude)
      return BEFORE;
    if (this.latitude > that.latitude)
      return AFTER;

    if (this.longitude < that.longitude)
      return BEFORE;
    if (this.longitude > that.longitude)
      return AFTER;

    return EQUAL;
  }
}
