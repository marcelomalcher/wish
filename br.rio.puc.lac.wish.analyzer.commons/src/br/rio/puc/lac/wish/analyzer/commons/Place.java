package br.rio.puc.lac.wish.analyzer.commons;


/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class Place implements Comparable<Place> {

  /** */
  private String name;

  /** */
  private String streetAddress;

  /** */
  private String countryCode;

  /** */
  private String id;

  /** */
  private String placeType;

  /**
   * 
   */
  public Place() {

  }

  /**
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 
   * @return
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * 
   * @param streetAddress
   */
  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  /**
   * 
   * @return
   */
  public String getCountryCode() {
    return countryCode;
  }

  /**
   * 
   * @param countryCode
   */
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  /**
   * 
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * 
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 
   * @return
   */
  public String getPlaceType() {
    return placeType;
  }

  /**
   * 
   * @param placeType
   */
  public void setPlaceType(String placeType) {
    this.placeType = placeType;
  }

  /**
   * 
   */
  @Override
  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
			+ ((countryCode == null) ? 0 : countryCode.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((placeType == null) ? 0 : placeType.hashCode());
	result = prime * result
			+ ((streetAddress == null) ? 0 : streetAddress.hashCode());
	return result;
  }

  /**
   * 
   */
  @Override
  public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Place other = (Place) obj;
	if (countryCode == null) {
		if (other.countryCode != null)
			return false;
	} else if (!countryCode.equals(other.countryCode))
		return false;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (placeType == null) {
		if (other.placeType != null)
			return false;
	} else if (!placeType.equals(other.placeType))
		return false;
	if (streetAddress == null) {
		if (other.streetAddress != null)
			return false;
	} else if (!streetAddress.equals(other.streetAddress))
		return false;
	return true;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Place that) {
    return this.id.compareTo(that.getId());
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Place [countryCode=" + countryCode + ", id=" + id + ", name="
      + name + ", placeType=" + placeType + ", streetAddress=" + streetAddress
      + "]";
  }

}
