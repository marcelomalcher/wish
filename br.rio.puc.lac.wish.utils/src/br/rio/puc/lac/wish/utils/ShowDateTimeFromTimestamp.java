package br.rio.puc.lac.wish.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowDateTimeFromTimestamp {

  public static void main(String[] args) {

    long[] timestamps = new long[] { 1375000000000l, 1377000000000l };

    System.out.println("### TIMESTAMP -> DATETIME ### ");

    DateFormat simpleDateFormat = new SimpleDateFormat();

    for (long timestamp : timestamps) {
      System.out.println("Timestamp = " + timestamp + " -> Datetime = "
        + simpleDateFormat.format(new Date(timestamp)));
    }
  }
}
