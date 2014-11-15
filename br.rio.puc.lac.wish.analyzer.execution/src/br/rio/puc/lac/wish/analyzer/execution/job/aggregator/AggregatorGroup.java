package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class AggregatorGroup implements WritableComparable<AggregatorGroup> {

  /**
   * 
   */
  private String id;

  /**
   * 
   */
  private AggregatorGroup() {
  }

  /**
   * @param id
   * 
   */
  public AggregatorGroup(String id) {
    this();
    this.id = id;
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
    if (id.compareTo(this.id) < 0) {
      this.id = id;
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "AggregatorGroup [id=" + id + "]";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void readFields(DataInput in) throws IOException {
    String id = in.readUTF();
    this.id = id;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(this.getId());
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(AggregatorGroup that) {
    final int EQUAL = 0;

    if (this == that) {
      return EQUAL;
    }

    return this.getId().compareTo(that.getId());
  }
}
