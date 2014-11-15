package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.main;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import weka.core.Instances;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.luceneindexing.CollectionSimilarity;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.CreateBM25ArffFileDemo;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.StopWords;

public class ClassifierWithIndexAsTrainingData {

  public static Instances getTrainingDataFromIndex(String indexLocation)
    throws Exception {
    return ArffMaker.getInstancesFromIndex(indexLocation, 10);
  }

  /**
   * 
   * it adds to the index makes arff only with the documents added last removes
   * the added docs from the index
   * 
   * @param indexLocation
   * @param tweetText
   * @return
   */
  public static Instances getTestingDataUsingIndex(String indexLocation,
    String tweetText, String outputFileName) throws Exception {
    Instances i = null;
    // add to index
    addToIndex(indexLocation, tweetText);
    // make arff file and delete the last entry of the index in the same function
    ArffMaker.makeArffLastEntry(indexLocation, outputFileName, 10);
    return i;
  }

  /**
   * adds the tweetText to the index found at the respective location the
   * document will have idField LASTADDED, and titleField LAST TITLE
   * 
   * @param indexLocation
   * @param tweetText
   * @throws Exception
   */
  public static void addToIndex(String indexLocation, String tweetText)
    throws Exception {

    Directory directory = new SimpleFSDirectory(new File(indexLocation));

    Analyzer analyzer =
      new SnowballAnalyzer(Version.LUCENE_30, "English",
        StopWords.ENGLISH_STOPWORDS);
    IndexWriter indexWriter = null;
    try {
      indexWriter =
        new IndexWriter(directory, analyzer, false, MaxFieldLength.UNLIMITED);
    }
    catch (CorruptIndexException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (LockObtainFailedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      System.out.println("### Making new index! WARNING!");
      insertTweetsToIndexTEST(indexLocation);
      indexWriter =
        new IndexWriter(directory, analyzer, false, MaxFieldLength.UNLIMITED);
      e.printStackTrace();
    }

    CollectionSimilarity collectionSimilarity = new CollectionSimilarity();
    indexWriter.setSimilarity(collectionSimilarity);

    String idField = "id";
    String textField = "body";
    String titleField = "title";

    CreateBM25ArffFileDemo.addDocument(indexWriter, idField, "LASTADDED",
      titleField, "LAST TITLE", textField, tweetText);

    indexWriter.optimize();
    indexWriter.close();
  }

  /**
   * Creates a index in which there is one document
   * 
   * @param indexName
   * @throws IOException
   */
  public static void insertTweetsToIndexTEST(String indexName)
    throws IOException {

    Directory directory = new SimpleFSDirectory(new File(indexName));

    Analyzer analyzer =
      new SnowballAnalyzer(Version.LUCENE_30, "English",
        StopWords.ENGLISH_STOPWORDS);
    IndexWriter indexWriter =
      new IndexWriter(directory, analyzer, true, MaxFieldLength.UNLIMITED);

    CollectionSimilarity collectionSimilarity = new CollectionSimilarity();
    indexWriter.setSimilarity(collectionSimilarity);

    String idField = "id";
    String textField = "body";
    String titleField = "title";

    CreateBM25ArffFileDemo.addDocument(indexWriter, idField, "cococ",
      titleField, "pulamea", textField, "muecucacat");
    indexWriter.optimize();
    indexWriter.close();
  }

  public static void main(String args[]) throws Exception {
    System.out.println("Happy Starting!");
    // try to get the arff for a text that goes like this
    // this is the testing document take it or leave it
    getTestingDataUsingIndex("tweetsIndex",
      "this is the testing document take it or leave it", "test_out.arff");

    System.out.println("Happy Ending!");
  }

}
