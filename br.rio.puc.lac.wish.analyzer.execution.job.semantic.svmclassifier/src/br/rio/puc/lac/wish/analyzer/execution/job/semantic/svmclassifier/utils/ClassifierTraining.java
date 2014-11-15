package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.AllInstancesInClass;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.DocumentToClassify;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ClassifierTraining {

  /**
   * The logger of this class
   */
  private Logger logger;
  {
    this.logger = Logger.getLogger(ClassifierTraining.class.getSimpleName());
  }

  /**
   * a vector holding the position, or id of the feature
   */
  private Hashtable<String, Integer> featurePosition;

  /**
   * a vector holding the number of times the feature appears in the collection
   */
  private Hashtable<String, Integer> featureCollectionCount;

  /**
   * the number of documents in which the feature appears
   */
  private Hashtable<String, Integer> featureDocumentCount;

  /**
   * the vector containg features ordered by their positions
   */
  private Vector<String> featureVector;

  /**
   * 
   */
  private int numberOfDocuments;

  /**
   * 
   */
  public static int Teta = 0;

  /**
   * 
   */
  private List<AllInstancesInClass> allClasses;

  /**
   * 
   * @param allClasses
   */
  public ClassifierTraining(List<AllInstancesInClass> allClasses) {
    this.allClasses = allClasses;
    this.buildAll();
  }

  /**
   * 
   */
  public void buildAll() {
    //Initialize all data structures
    this.featurePosition = new Hashtable<String, Integer>();
    this.featureCollectionCount = new Hashtable<String, Integer>();
    this.featureDocumentCount = new Hashtable<String, Integer>();
    this.featureVector = new Vector<String>();

    //Setting number of documents to zero
    this.numberOfDocuments = 0;

    //
    this.buildInstances();
  }

  /**
   * Builds the instances and feature vectors as tfidfs of the features
   */
  private void buildInstances() {
    // builds the array holding the ids / positions of the features
    this.buildPositionCount();
    // builds the array that counts the global number of appearances of features
    this.buildFeatureCount();
    logger.info("Number of documents:" + this.numberOfDocuments);
    for (AllInstancesInClass aic : allClasses) {
      buildInstancesClass(aic);
    }
  }

  /**
   * Compute how many times a term appears in the whole collection and in how
   * many documents it appears
   * 
   * @throws SQLException
   * @throws IOException
   */
  private void buildFeatureCount() {
    // Take each class and build the features collection frequency and document frequency arrays
    for (AllInstancesInClass aic : this.allClasses) {
      buildFeatureCountForClass(aic);
    }
  }

  /**
   * builds the ids/ positions of the features by adding them at the end of a
   * vector and associating the position to them
   * 
   * @param allQueriesClass
   */
  private void buildFeatureCountForClass(AllInstancesInClass allQueriesClass) {
    // Take each Document and build the featurepos vector
    for (DocumentToClassify document : allQueriesClass.getInstances()) {
      String allTerms[] = document.getBagOfFeatures();
      for (String term : allTerms) {
        Integer freq = featureCollectionCount.get(term);
        if (freq == null || freq < Teta) {
          continue;
        }
        Integer position = featurePosition.get(term);
        if (position == null) {
          int pos = featureVector.size();
          featureVector.add(term);
          featurePosition.put(term, pos);
        }
      }
    }
  }

  /**
   * builds the array containing the ids/ positions of the features
   */
  private void buildPositionCount() {
    for (AllInstancesInClass aic : this.allClasses) {
      buildFeaturePositionsForClass(aic);
    }
  }

  /**
   * counts the number of times the feature appears in the collection and the
   * number of documents it appears in
   * 
   * @param allQueriesClass
   */
  private void buildFeaturePositionsForClass(AllInstancesInClass allQueriesClass) {
    for (DocumentToClassify document : allQueriesClass.getInstances()) {
      String allTerms[] = document.getBagOfFeatures();
      ArrayList<String> termsAddedToIdf = new ArrayList<String>();
      for (String term : allTerms) {
        //update the global feature collection count
        Integer countfrq = featureCollectionCount.get(term);
        if (countfrq == null) {
          countfrq = 0;
        }
        featureCollectionCount.put(term, countfrq + 1);
        //update the feature document count
        // idf=log(|D|/N) - first compute N = total number of
        // documents in which a feature appears
        if (!termsAddedToIdf.contains(term)) {
          // a feature in a document is investigated only once and then it's document frequency is updated
          termsAddedToIdf.add(term);
          Integer countN = featureDocumentCount.get(term);
          if (countN == null) {
            countN = 0;
          }
          featureDocumentCount.put(term, countN + 1);

        }

      }
      this.numberOfDocuments++;
    }
  }

  /**
   * 
   * @param allQueriesClass
   */
  public void buildInstancesClass(AllInstancesInClass allQueriesClass) {
    // for each document compute the tf score and then compute the idf score
    for (DocumentToClassify document : allQueriesClass.getInstances()) {
      this.buildDocument(document);
    }
  }

  /**
   * 
   * @param document
   */
  public void buildDocument(DocumentToClassify document) {
    double n_score = document.getValue();
    String allTerms[] = document.getBagOfFeatures();
    //initialize them with zero
    int featureTF[] = new int[featureVector.size()];
    double featureTFIDF[] = new double[featureVector.size()];

    Arrays.fill(featureTF, 0);
    Arrays.fill(featureTFIDF, 0);

    for (String term : allTerms) {
      // Number of times the term appears in the collection
      Integer globalfreq = featureCollectionCount.get(term);
      if (globalfreq == null) {
        globalfreq = 1;
      }
      // Number of Documents in which term appears
      Integer idfcnt_N = featureDocumentCount.get(term);
      if (idfcnt_N == null) {
        idfcnt_N = 1;
      }
      //
      Integer ppos = featurePosition.get(term);
      if (ppos != null) {
        //featureTF
        int freq = featureTF[ppos];
        featureTF[ppos] = freq + 1;
        //featureTFIDF
        featureTFIDF[ppos] =
          featureTF[ppos] * Math.log(this.numberOfDocuments / idfcnt_N);
      }
    }
    String line = n_score + "\t";
    for (int i = 0; i < featureTFIDF.length; i++) {
      if (featureTFIDF[i] != 0) {
        line = line + (i + 1) + ":" + featureTFIDF[i] + "\t";
      }
    }
    // update the items
    document.setSVMFeatureVector(line);
  }

  /**
   * 
   * @return
   */
  public List<AllInstancesInClass> getAllInstancesInClasses() {
    return this.allClasses;
  }
}