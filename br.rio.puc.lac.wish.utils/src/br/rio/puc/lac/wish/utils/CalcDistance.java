package br.rio.puc.lac.wish.utils;

import java.util.ArrayList;
import java.util.List;

public class CalcDistance {

  private double latitude_1;
  private double longitude_1;

  private double latitude_2;
  private double longitude_2;

  public CalcDistance(double latitude_1, double longitude_1, double latitude_2,
    double longitude_2) {
    super();
    this.latitude_1 = latitude_1;
    this.longitude_1 = longitude_1;
    this.latitude_2 = latitude_2;
    this.longitude_2 = longitude_2;
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {

    List<CalcDistance> dists = new ArrayList<CalcDistance>();
    //
    CalcDistance dist =
      new CalcDistance(157.2844251084727, 95.42667645997031,
        158.89805101252074, 96.16434091273189);
    dists.add(dist);
    dist =
      new CalcDistance(149.3182468517609, 22.018465370212546,
        149.0181361176084, 22.737508590121926);
    dists.add(dist);
    dist =
      new CalcDistance(24.209118651439308, 42.294447100429174,
        25.870876254483093, 42.31893207922264);
    dists.add(dist);
    dist =
      new CalcDistance(118.54137712719377, 44.35525672391118,
        119.60890438137355, 44.57028690712987);
    dists.add(dist);
    dist =
      new CalcDistance(45.04362857867634, 83.4592141712625, 45.507768407618066,
        83.6947886906948);
    dists.add(dist);

    for (CalcDistance d : dists) {
      double latResult = Math.pow(Math.abs(d.latitude_1 - d.latitude_2), 2);
      double lonResult = Math.pow(Math.abs(d.longitude_1 - d.longitude_2), 2);
      double result = Math.sqrt(latResult + lonResult);
      System.out.println("Distance: " + result);
    }
  }
}
