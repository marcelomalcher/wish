package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model;

import java.util.List;

public class AllInstancesInClass {

  private String classLabel;
  private int classId;
  private List<DocumentToClassify> instances;

  /**
   * \ constructor it is nice if the class id is the same one as the parameter
   * for the ReadFromFile call
   * 
   * @param classLabel the textual label of the class
   * @param classId the id that will be used to represent the class that will
   *        also appear in the output of the classifier
   * @param instances a hashtable containing the instances of the class
   */
  public AllInstancesInClass(String classLabel, int classId,
    List<DocumentToClassify> instances) {
    super();
    this.classLabel = classLabel;
    this.classId = classId;
    this.instances = instances;
  }

  public String getClassLabel() {
    return classLabel;
  }

  public void setClassLabel(String classLabel) {
    this.classLabel = classLabel;
  }

  public int getClassId() {
    return classId;
  }

  public void setClassId(int classId) {
    this.classId = classId;
  }

  public List<DocumentToClassify> getInstances() {
    return instances;
  }

  public void setInstances(List<DocumentToClassify> instances) {
    this.instances = instances;
  }

}
