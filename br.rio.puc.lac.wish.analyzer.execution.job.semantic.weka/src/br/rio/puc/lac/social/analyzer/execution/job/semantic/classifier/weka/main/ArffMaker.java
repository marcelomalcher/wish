package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveByName;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.luceneindexing.CollectionSimilarity;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.Coordinator;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.LuceneToArff;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.TermSelector;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.ClassifierUtil;

public class ArffMaker {

  public static void addClassAttributes(String fileLocation,
    String outputFileLocation) throws FileNotFoundException, IOException {
    ArrayList<String> nominalAttributeValues = seeHowManyClasses(fileLocation);
    Instances data =
      new Instances(new BufferedReader(new FileReader(fileLocation)));
    data.remove(0);
    data.insertAttributeAt(new Attribute("class", nominalAttributeValues), data
      .numAttributes());

    for (int i = 0; i < data.numInstances(); i++) {
      String idAttributeValue =
        data.instance(i).stringValue(data.attribute("id"));
      String classValue =
        idAttributeValue.substring(0, idAttributeValue.lastIndexOf("_"));
      data.instance(i).setValue(data.attribute("class"), classValue);
    }

    // write te arff for the event into an arff
    ArffSaver s = new ArffSaver();
    s.setInstances(data);
    s.setFile(new File(outputFileLocation));

    s.writeBatch();
    System.out.println("Added class to the arff and wrote it");

  }

  public static void addClassEmptyAttribute(String fileLocation,
    String outputFileLocation) throws FileNotFoundException, IOException {
    ArrayList<String> nominalAttributeValues = seeHowManyClasses(fileLocation);
    Instances data =
      new Instances(new BufferedReader(new FileReader(fileLocation)));
    //data.remove(0);
    data.insertAttributeAt(new Attribute("class", nominalAttributeValues), data
      .numAttributes());

    // write te arff for the event into an arff
    ArffSaver s = new ArffSaver();
    s.setInstances(data);
    s.setFile(new File(outputFileLocation));

    s.writeBatch();
    System.out.println("Added class to the arff and wrote it");

  }

  /**
   * 
   * @param fileLocation
   * @param classValue
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static void addClass(String fileLocation, String classValue)
    throws FileNotFoundException, IOException {

    ArrayList<String> nominalAttributeValues = new ArrayList<String>();
    nominalAttributeValues.add(classValue);
    nominalAttributeValues.add("non_" + classValue);
    // add the class to the positive / negative examples
    Instances data =
      new Instances(new BufferedReader(new FileReader(fileLocation)));

    data.insertAttributeAt(new Attribute("class", nominalAttributeValues), 0);
    for (int i = 0; i < data.numInstances(); i++) {
      // System.out.println(data.instance(i).stringValue(data.attribute("id"))+" "+eventHourDuration.get(data.instance(i).stringValue(data.attribute("id"))));
      // data.instance(i).setValue(data.attribute("class"), 0);
      data.instance(i).setValue(data.attribute("class"), classValue);
    }
    // System.out.println(data);

    // write te arff for the event into an arff
    ArffSaver s = new ArffSaver();
    s.setInstances(data);
    s.setFile(new File(fileLocation));

    s.writeBatch();
    System.out.println("Added class to the arff and wrote it");

  }

  /**
   * 
   * @param fileLocation
   * @throws Exception
   */
  private static void removeIdAttribute(String fileLocation) throws Exception {
    // remove the id attribute
    Instances data =
      new Instances(new BufferedReader(new FileReader(fileLocation)));

    RemoveByName remove = new RemoveByName();
    remove.setExpression("id");
    remove.setInputFormat(data);
    Instances dataNew = Filter.useFilter(data, remove);

    ArffSaver s = new ArffSaver();
    s.setInstances(dataNew);
    s.setFile(new File(fileLocation));

    s.writeBatch();
    System.out.println("Removed id attribute from the arff and wrote it");

  }

  /**
	 * 
	 */
  private static void addNegativeExamples() {
    // concatenate with file about other query
  }

  /*
   * makes the initial arff file from the lucene index
   */
  public static void makeArffs(String indexLocation, String outputFile,
    int threshold) throws IOException {

    String idField = "id";
    String textField = "body";
    String titleField = "title";

    Directory directory = new SimpleFSDirectory(new File(indexLocation));
    File outputArfFile = new File(outputFile);
    Writer fileWriter = new BufferedWriter(new FileWriter(outputArfFile));
    IndexReader indexReader = IndexReader.open(directory);
    Map<String, Double> normalizedFieldLengths =
      CollectionSimilarity.getNormalizedFieldLengths(indexReader.numDocs());
    Coordinator coordinator = new Coordinator(normalizedFieldLengths);
    TermSelector termSelector = new TermSelector(threshold, Integer.MAX_VALUE);

    LuceneToArff.writeArff(fileWriter, indexReader, coordinator, termSelector,
      idField, titleField, textField);

    System.out.format("Wrote bag of word file to %s\n", outputArfFile);

    indexReader.close();
    fileWriter.close();
  }

  /**
   * 
   * makes an arff just with the last document added to the index and then
   * removes the last document from there
   * 
   * @param indexLocatoin
   * @param outputfile
   * @param threshold
   */
  public static void makeArffLastEntry(String indexLocation, String outputFile,
    int threshold) throws Exception {
    String idField = "id";
    String textField = "body";
    String titleField = "title";

    Directory directory = new SimpleFSDirectory(new File(indexLocation));
    File outputArfFile = new File(outputFile);
    Writer fileWriter = new BufferedWriter(new FileWriter(outputArfFile));
    IndexReader indexReader = IndexReader.open(directory, false);
    Map<String, Double> normalizedFieldLengths =
      CollectionSimilarity.getNormalizedFieldLengths(indexReader.numDocs());
    Coordinator coordinator = new Coordinator(normalizedFieldLengths);
    TermSelector termSelector = new TermSelector(threshold, Integer.MAX_VALUE);

    // make a list of the docNos that will get written to the arff file
    // it will now contain just the id of the last document in the index
    ArrayList<Integer> docnoList = new ArrayList<Integer>();
    docnoList.add(indexReader.numDocs() - 1);

    // write the selected documents(just the last one in the index) to the arff
    LuceneToArff.writeArffSelectedDocs(docnoList, fileWriter, indexReader,
      coordinator, termSelector, idField, titleField, textField);

    System.out.format("Wrote bag of word file to %s\n", outputArfFile);

    // try to delete the last document which was just written to the arff
    indexReader.deleteDocument(indexReader.numDocs() - 1);
    indexReader.close();
    fileWriter.close();
  }

  /**
   * 
   * @param arffFileLocation
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static ArrayList<String> seeHowManyClasses(String arffFileLocation)
    throws FileNotFoundException, IOException {
    ArrayList<String> listOfClasses = new ArrayList<String>();
    Instances data =
      new Instances(new BufferedReader(new FileReader(arffFileLocation)));
    data.remove(0);
    for (int i = 0; i < data.numInstances(); i++) {
      String idAttributeValue =
        data.instance(i).stringValue(data.attribute("id"));
      String classValue =
        idAttributeValue.substring(0, idAttributeValue.lastIndexOf("_"));
      if (!listOfClasses.contains(classValue)) {
        listOfClasses.add(classValue);
      }
    }
    System.out.println("the identified classes are:" + listOfClasses);
    return listOfClasses;

  }

  /**
   * 
   * @param inputArffLocation
   * @param outputArffLocation
   * @throws Exception
   */
  private static void removeTitleAttributes(String inputArffLocation,
    String outputArffLocation) throws Exception {

    Instances data =
      new Instances(new BufferedReader(new FileReader(inputArffLocation)));

    RemoveByName removeTitle = new RemoveByName();
    removeTitle.setExpression("title:.*");
    removeTitle.setInputFormat(data);
    Instances dataNew = Filter.useFilter(data, removeTitle);

    ArffSaver s = new ArffSaver();
    s.setInstances(dataNew);
    s.setFile(new File(outputArffLocation));

    s.writeBatch();
    System.out.println("Removed title attributes from the arff and wrote it");
  }

  /**
   * 
   * @param inputArffLocation
   * @param outputArffLocation
   * @throws Exception
   */
  private static void removeAttributesWithClassInThem(String inputArffLocation,
    String outputArffLocation) throws Exception {
    // remove the id attribute
    Instances data =
      new Instances(new BufferedReader(new FileReader(inputArffLocation)));
    ArrayList<String> classes = new ArrayList<String>();
    for (int i = 0; i < data.attribute("class").numValues(); i++) {
      String className = data.attribute("class").value(i);
      for (String part : className.split("[+]")) {
        classes.add(part);
      }
    }
    System.out.println("words to be removed = " + classes);
    String regularExp = "body:(";
    for (String cl : classes) {
      regularExp = regularExp + cl + "|";
    }
    regularExp = regularExp.substring(0, regularExp.length() - 1);
    regularExp = regularExp + ")+.*";
    System.out.println(regularExp);

    RemoveByName removeClassesLikeAttrs = new RemoveByName();
    removeClassesLikeAttrs.setExpression(regularExp);
    removeClassesLikeAttrs.setInputFormat(data);
    Instances dataNew = Filter.useFilter(data, removeClassesLikeAttrs);

    ArffSaver s = new ArffSaver();
    s.setInstances(dataNew);
    s.setFile(new File(outputArffLocation));

    s.writeBatch();
    System.out
      .println("Removed class like attributes from the arff and wrote it");

  }

  public static void alterTestData(String inputFileName) throws Exception {
    addClassEmptyAttribute(inputFileName, "tmp1.arff");
    removeIdAttribute("tmp1.arff");
    removeTitleAttributes("tmp1.arff", inputFileName);
    removeAttributesWithClassInThem("tmp2.arff", inputFileName);
  }

  public static void wholePipe(int threshold) throws Exception {
    makeArffs("tweetsIndex", "coco.arff", threshold);
    addClassAttributes("coco.arff", "coco1.arff");
    removeIdAttribute("coco1.arff");
    removeTitleAttributes("coco1.arff", "coco2.arff");
    removeAttributesWithClassInThem("coco2.arff", "cocoLast.arff");

  }

  public static Instances getInstancesFromIndex(String indexLocation,
    int threshold) throws Exception {
    makeArffs(indexLocation, "coco.arff", threshold);
    addClassAttributes("coco.arff", "coco1.arff");
    removeIdAttribute("coco1.arff");
    removeTitleAttributes("coco1.arff", "coco2.arff");
    removeAttributesWithClassInThem("coco2.arff", "finalData.arff");
    return ClassifierUtil.loadArffFile("finalData.arff");
  }

  public static void main(String args[]) throws Exception {
    // makeArffs();
    // addClass("plm.out", "traffic_jam");
    //		removeAttribute("plm.out");

    //		makeArffs("tweetsIndex2", "plm2.arff");
    //		removeAttribute("plm2.arff");
    //		addClass("plm2.arff", "rain");

    //		mergeTwoArffs("plm1.arff", "plm2.arff", "plmMerge.arff");

    //		makeArffs("tweetsIndex", "plm.arff");
    //		seeHowManyClasses("loloz.arff","");
    //		addClassAttributes("plm.arff", "plmClass.arff");
    //		removeIdAttribute("plmClass.arff");
    //		removeAttributesWithClassInThem("plmClass.arff", "plmNou.arff");

    //		removeTitleAttributes("plmClass.arff", "plmLast.arff");
    //		removeAttributesWithClassInThem("plmLast.arff", "plmLaster.arff");

    wholePipe(10);
  }
}
