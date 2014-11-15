package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.main;

import java.io.File;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import weka.core.Instances;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.ArffUtil;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.ClassifierUtil;

public class TestClassification {

  public static void testLuceneIndex() throws Exception {
    Directory directory = new SimpleFSDirectory(new File("tweetsIndex"));
    IndexReader indexReader = IndexReader.open(directory, false);
    //indexReader.deleteDocument(indexReader.numDocs()-1);
    System.out.println(indexReader.numDocs());
    System.out.println(indexReader.document(indexReader.numDocs() - 1));
    indexReader.close();

  }

  public static void testClassification() throws Exception {
    System.out.println("Start!");
    // make an arff with the text of the new test document
    ClassifierWithIndexAsTrainingData.getTestingDataUsingIndex("tweetsIndex",
      "http://t.co/BLF1avx @ithinkbuzz @rhnnntrtmnn @buddymedia nba jam!",
      "testing_ura.arff");
    // train a classifier with the training arff and output the classification of the test document
    ClassifierUtil clu = new ClassifierUtil();
    // load the training data file
    Instances trainingData = clu.loadArffFile("cocoLast.arff");
    Instances testingData = clu.loadArffFile("testing_ura.arff");
    testingData = ArffUtil.copyClassAttributes(trainingData, testingData);
    //display the class that the classifier says the thing belongs to
    clu.classifyUnseenData(trainingData, testingData);
    System.out.println("End!");
  }

  public static void main(String args[]) throws Exception {
    testClassification();

  }

}
