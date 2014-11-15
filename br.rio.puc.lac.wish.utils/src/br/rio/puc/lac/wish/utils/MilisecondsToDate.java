package br.rio.puc.lac.wish.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class MilisecondsToDate {

  /**
   * @param args
   */
  public static void main(String[] args) {

    Long[] times = new Long[] { 1376963359000l };

    SimpleDateFormat formatter = new SimpleDateFormat();
    for (Long time : times) {
      System.out.println(time.toString() + " - "
        + formatter.format(new Date(time)));
    }
  }
}
