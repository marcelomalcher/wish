package br.rio.puc.lac.wish.utils;

public class CalcDistanceKm {

  static double haversine_km(double lat1, double long1, double lat2,
    double long2) {
    double d2r = Math.PI / 180.0;
    double dlong = (long2 - long1) * d2r;
    double dlat = (lat2 - lat1) * d2r;
    double a =
      Math.pow(Math.sin(dlat / 2.0), 2) + Math.cos(lat1 * d2r)
        * Math.cos(lat2 * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double d = 6367 * c;

    return d;
  }

  public static void main(String[] args) {
    /*
     * Rio de Janeiro spatial.southwest-latitude= -23.10
     * spatial.southwest-longitude= -43.6 spatial.northeast-latitude= -22.75
     * spatial.northeast-longitude= -43.15
     */

    double latA = -23.10;
    double lonA = -43.6;
    double latB = -22.75;
    double lonB = -43.6;

    System.out.println("Distance in km between coordinates is = "
      + haversine_km(latA, lonA, latB, lonB));
  }
}
