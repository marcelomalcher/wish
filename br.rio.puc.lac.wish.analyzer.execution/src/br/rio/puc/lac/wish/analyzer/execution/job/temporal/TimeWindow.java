package br.rio.puc.lac.wish.analyzer.execution.job.temporal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class TimeWindow implements Comparable<TimeWindow> {

  /** */
  private int number;
  /** */
  private long beginTime;
  /** */
  private long endTime;

  /**
   * 
   */
  public TimeWindow() {
    super();
  }

  /**
   * 
   * @param number
   * @param beginTime
   * @param endTime
   */
  public TimeWindow(int number, long beginTime, long endTime) {
    super();
    this.number = number;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * 
   * @return
   */
  public int getNumber() {
    return number;
  }

  /**
   * 
   * @return
   */
  public long getBeginTime() {
    return beginTime;
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
   * @param number
   */
  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * 
   * @param beginTime
   */
  public void setBeginTime(long beginTime) {
    this.beginTime = beginTime;
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
   * @param in
   * @return
   * @throws IOException
   */
  public static TimeWindow getTimeWindow(DataInput in) throws IOException {
    return new TimeWindow(in.readInt(), in.readLong(), in.readLong());
  }

  /**
   * 
   * @param out
   * @throws IOException
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.getNumber());
    out.writeLong(this.getBeginTime());
    out.writeLong(this.getEndTime());
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    SimpleDateFormat format = new SimpleDateFormat();
    return "TW:" + number + "[" + format.format(beginTime) + "-"
      + format.format(endTime) + "]";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (beginTime ^ (beginTime >>> 32));
    result = prime * result + (int) (endTime ^ (endTime >>> 32));
    result = prime * result + number;
    return result;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TimeWindow other = (TimeWindow) obj;
    if (beginTime != other.beginTime) {
      return false;
    }
    if (endTime != other.endTime) {
      return false;
    }
    if (number != other.number) {
      return false;
    }
    return true;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(TimeWindow that) {
    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;

    if (this == that) {
      return EQUAL;
    }

    if (this.number < that.number) {
      return BEFORE;
    }
    if (this.number > that.number) {
      return AFTER;
    }

    if (this.beginTime < that.beginTime) {
      return BEFORE;
    }
    if (this.beginTime > that.beginTime) {
      return AFTER;
    }

    if (this.endTime < that.endTime) {
      return BEFORE;
    }
    if (this.endTime > that.endTime) {
      return AFTER;
    }

    return EQUAL;
  }
}