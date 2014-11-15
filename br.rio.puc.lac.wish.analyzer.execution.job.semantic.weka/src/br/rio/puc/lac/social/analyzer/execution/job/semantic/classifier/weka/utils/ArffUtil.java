package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveByName;

public class ArffUtil {

  public static void mergeTwoArffs(String arffLocation1, String arffLocation2,
    String outputFileLocation) throws FileNotFoundException, IOException {
    Instances data1 =
      new Instances(new BufferedReader(new FileReader(arffLocation1)));
    Instances data2 =
      new Instances(new BufferedReader(new FileReader(arffLocation2)));
    Instances mergedData = Instances.mergeInstances(data1, data2);
    ArffSaver s = new ArffSaver();
    s.setInstances(mergedData);
    s.setFile(new File(outputFileLocation));

    s.writeBatch();
    System.out.println("Added class to the arff and wrote it");
  }

  /**
   * add the class attribute to the destination data having the values of the
   * class attribute in the source data and also do a little more cleaning of
   * the destination data such as 1. removing id 2. removing title 3. removing
   * attributes that are like the class names TODO do not do step3 anymore for
   * the training and for the testing data
   * 
   * @param sourceData
   * @param destinationData
   * @return
   * @throws Exception
   */
  public static Instances copyClassAttributes(Instances sourceData,
    Instances destinationData) throws Exception {

    System.out.println("Processing test data");
    //add the class attribute to the destiation data
    Enumeration<String> classValues =
      sourceData.attribute("class").enumerateValues();
    ArrayList<String> nominalAttributeValues = new ArrayList<String>();
    while (classValues.hasMoreElements()) {
      String classVal = classValues.nextElement();
      nominalAttributeValues.add(classVal);
    }

    destinationData.insertAttributeAt(new Attribute("class",
      nominalAttributeValues), destinationData.numAttributes());
    destinationData.setClass(destinationData.attribute("class"));

    // remove id
    RemoveByName remove = new RemoveByName();
    remove.setExpression("id");
    remove.setInputFormat(destinationData);
    destinationData = Filter.useFilter(destinationData, remove);

    //remove title
    RemoveByName removeTitle = new RemoveByName();
    removeTitle.setExpression("title:.*");
    removeTitle.setInputFormat(destinationData);
    destinationData = Filter.useFilter(destinationData, removeTitle);

    // remove classlike attributes

    String regularExp = "body:(";
    for (String cl : nominalAttributeValues) {
      regularExp = regularExp + cl + "|";
    }
    regularExp = regularExp.substring(0, regularExp.length() - 1);
    regularExp = regularExp + ")+.*";

    RemoveByName removeClassesLikeAttrs = new RemoveByName();
    removeClassesLikeAttrs.setExpression(regularExp);
    removeClassesLikeAttrs.setInputFormat(destinationData);
    destinationData = Filter.useFilter(destinationData, removeClassesLikeAttrs);

    //TODO:
    // remove all attributes that are not in the sourceData attributesList

    return destinationData;
  }

  public static void main(String args[]) throws Exception {
    ClassifierUtil clu = new ClassifierUtil();
    Instances unprocessedData = clu.loadArffFile("coco.arff");
    // load the training data file
    Instances trainingData = clu.loadArffFile("cocoLast.arff");
    Instances testingData = clu.loadArffFile("test_out.arff");

    testingData = ArffUtil.copyClassAttributes(trainingData, testingData);
    //clu.evaluateWithTestingData(trainingData, testingData);

    clu.classifyUnseenData(trainingData, testingData);
  }

}
