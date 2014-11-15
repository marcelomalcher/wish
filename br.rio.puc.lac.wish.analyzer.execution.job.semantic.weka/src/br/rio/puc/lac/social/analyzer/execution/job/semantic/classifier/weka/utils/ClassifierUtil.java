package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils;

import java.io.InputStream;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClassifierUtil {

  public Classifier classifier;

  public static Instances loadArffFile(String trainingFileName)
    throws Exception {
    DataSource source = new DataSource(trainingFileName);
    Instances data = source.getDataSet();
    if (data.classIndex() == -1) {
      data.setClassIndex(data.numAttributes() - 1);
    }
    return data;
  }

  public static Instances loadArffFileInputStream(InputStream in)
    throws Exception {
    DataSource source = new DataSource(in);
    Instances data = source.getDataSet();
    if (data.classIndex() == -1) {
      data.setClassIndex(data.numAttributes() - 1);
    }
    return data;
  }

  public static Classifier trainClassifier(Instances data) throws Exception {
    //build classifier
    Classifier classifier = new NaiveBayesMultinomial();
    classifier.buildClassifier(data);
    return classifier;
  }

  public static void evaluateXFoldCrossValidation(Instances data, int folds)
    throws Exception {
    //evaluate classifier doing the 10 fold crossvalidation
    Classifier cls = trainClassifier(data);
    Evaluation eval = new Evaluation(data);
    eval.crossValidateModel(cls, data, folds, new Random(1));
    System.out.println(eval.toSummaryString("\nResults\n======\n", false));
  }

  public static void evaluateWithTestingData(Instances trainingData,
    Instances testingData) throws Exception {
    Classifier cls = trainClassifier(trainingData);
    Evaluation eval = new Evaluation(trainingData);
    eval.evaluateModel(cls, testingData);
    System.out.println(eval.toSummaryString("\nResults\n======\n", false));
  }

  public static void classifyUnseenData(Instances trainingData,
    Instances newData) throws Exception {
    Classifier cls = trainClassifier(trainingData);

    Instances labeled = new Instances(newData);
    // copy class values from training data to the labeled

    for (int i = 0; i < newData.numInstances(); i++) {
      double clsLabel = cls.classifyInstance(newData.instance(i));
      labeled.instance(i).setClassValue(clsLabel);
    }

    for (int i = 0; i < labeled.numInstances(); i++) {
      System.out.println("Instance " + i + " belongs to class "
        + labeled.get(i).stringValue(labeled.attribute("class")));
    }

  }

  public String whichClass(Instances data) {
    String returnedClass = null;
    return returnedClass;
  }

  public static void main(String args[]) throws Exception {
    ClassifierUtil clu = new ClassifierUtil();
    // load the training data file
    Instances data = clu.loadArffFile("cocoLast.arff");

    // split into training and testing
    Instances trainingData = new Instances(data, 0, data.size() - 4);
    Instances testingData = new Instances(data, data.size() - 3, 3);

    clu.evaluateWithTestingData(trainingData, testingData);
    clu.classifyUnseenData(trainingData, testingData);

  }

}
