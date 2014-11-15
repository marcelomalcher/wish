package br.rio.puc.lac.wish.analyzer.commons;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.WritableComparable;

import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class Classification implements WritableComparable<Classification> {

  /**
   * 
   */
  private Map<String, String> classificationMap;

  /**
   * 
   */
  private Map<String, String> informationMap;

  /**
   * 
   */
  public Classification() {
    this.classificationMap = new HashMap<String, String>();
    this.informationMap = new HashMap<String, String>();
  }

  /**
   * 
   * @param otherClassification
   */
  public Classification(Classification otherClassification) {
    this();
    if (otherClassification != null) {
      for (String key : otherClassification.classificationMap.keySet()) {
        String classificationStr =
          otherClassification.classificationMap.get(key);
        this.putClassification(key, classificationStr);
      }
      for (String key : otherClassification.informationMap.keySet()) {
        String informationStr = otherClassification.informationMap.get(key);
        this.putInformation(key, informationStr);
      }
    }
  }

  /**
   * 
   * @param key
   * @param classification
   */
  public void putClassification(String key, String classification) {
    this.classificationMap.put(key, classification);
  }

  /**
   * 
   * @param key
   * @return
   */
  public String getClassification(String key) {
    return this.classificationMap.get(key);
  }

  /**
   * 
   * @param key
   * @return
   */
  public String removeClassification(String key) {
    return this.classificationMap.remove(key);
  }

  /**
   * 
   */
  public void clearClassificationMap() {
    this.classificationMap.clear();
  }

  /**
   * 
   * @return
   */
  public Map<String, String> getClassificationMap() {
    return classificationMap;
  }

  /**
   * 
   * @param classificationMap
   */
  public void setClassificationMap(Map<String, String> classificationMap) {
    this.classificationMap = classificationMap;
  }

  /**
   * 
   * @param key
   * @param information
   */
  public void putInformation(String key, String information) {
    this.informationMap.put(key, information);
  }

  /**
   * 
   * @param key
   * @return
   */
  public String getInformation(String key) {
    return this.informationMap.get(key);
  }

  /**
   * 
   * @param key
   * @return
   */
  public String removeInformation(String key) {
    return this.informationMap.remove(key);
  }

  /**
   * 
   * @return
   */
  public Map<String, String> getInformationMap() {
    return informationMap;
  }

  /**
   * 
   * @param informationMap
   */
  public void setInformationMap(Map<String, String> informationMap) {
    this.informationMap = informationMap;
  }

  /**
   * 
   */
  public void clearInformationMap() {
    this.informationMap.clear();
  }

  /**
   * 
   */
  public void clear() {
    this.clearClassificationMap();
    this.clearInformationMap();
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void readFields(DataInput in) throws IOException {
    int classificationMapSize = in.readInt();
    for (int i = 0; i < classificationMapSize; i++) {
      String key = in.readUTF();
      String classification = in.readUTF();
      this.putClassification(key, classification);
    }
    int informationMapSize = in.readInt();
    for (int i = 0; i < informationMapSize; i++) {
      String key = in.readUTF();
      String information = in.readUTF();
      this.putInformation(key, information);
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutput out) throws IOException {
    if (this.classificationMap != null) {
      out.writeInt(this.classificationMap.size());
      for (String key : this.classificationMap.keySet()) {
        out.writeUTF(key);
        String classification = this.classificationMap.get(key);
        out.writeUTF(classification);
      }
    }
    else {
      out.writeInt(0);
    }
    if (this.informationMap != null) {
      out.writeInt(this.informationMap.size());
      for (String key : this.informationMap.keySet()) {
        out.writeUTF(key);
        String classification = this.informationMap.get(key);
        out.writeUTF(classification);
      }
    }
    else {
      out.writeInt(0);
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Classification that) {
    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;

    if (this == that) {
      return EQUAL;
    }

    if (this.classificationMap.size() < that.classificationMap.size()) {
      return BEFORE;
    }
    if (this.classificationMap.size() > that.classificationMap.size()) {
      return AFTER;
    }

    for (String key : this.classificationMap.keySet()) {
      String thisValue = this.classificationMap.get(key);
      String thatValue = that.classificationMap.get(key);
      if (thisValue != null || thatValue != null) {
        if (thisValue == null && thatValue != null) {
          return BEFORE;
        }
        if (thisValue != null && thatValue == null) {
          return AFTER;
        }
        int result = thisValue.compareTo(thatValue);
        if (result != EQUAL) {
          return result;
        }
      }
    }

    //When comparing, if equal, update information
    //this.informationMap.putAll(that.informationMap);
    //that.informationMap.putAll(this.informationMap);

    return EQUAL;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String description = "Classification (" + classificationMap.size() + ")\n";
    for (Entry<String, String> entry : classificationMap.entrySet()) {
      description += "cla = " + entry.getKey() + ": " + entry.getValue() + "\n";
    }
    for (Entry<String, String> entry : informationMap.entrySet()) {
      description +=
        "info = " + entry.getKey() + ": " + entry.getValue() + "\n";
    }
    return description;
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    String theJson =
      "{\"classificationMap\":{\"SEMANTIC\":\"basket\",\"SPATIAL\":\"3283\",\"TEMPORAL\":\"1342\"},\"informationMap\":{\"SEMANTIC\":\"1\",\"SPATIAL\":\"Region [northEast=Location [latitude=40.76499998290092, longitude=-73.97000000067055, place=null], southWest=Location [latitude=40.73499998357147, longitude=-74.0, place=null]]\",\"TEMPORAL\":\"TW:1342[4/1/13 11:46 AM-4/1/13 3:46 PM]\"}}";
    System.out.println(theJson);
    Classification c = JSon.getFromJSONString(theJson, Classification.class);
    System.out.println(c);
    String newJson = JSon.toJSONString(Classification.class, c);
    System.out.println(newJson);
    Classification c2 = JSon.getFromJSONString(newJson, Classification.class);
    System.out.println(c2);

  }
}
