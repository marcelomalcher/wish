package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import weka.classifiers.Classifier;
import weka.core.Instances;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.main.ClassifierWithIndexAsTrainingData;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.ArffUtil;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils.ClassifierUtil;
import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ClassifierSemanticMapper implements
  Mapper<Object, Text, Classification, Text> {

  public enum Counter {
    ARFF_FILE_NAME;
  }

  public static final String TRAINING_DATA_ARFF = "training.data.arff";
  public static final String TRAINING_INDEX_FOLDER = "training.index.folder";
  public static final String INTERESTED_CLASSES = "interested.classes";

  private String[] interestedClasses;

  /**
   * 
   */
  private static final String INDEX_LOCATION = "tweetsIndex";

  /**
   * 
   */
  private static final String FILE_NAME = "testing-data_";

  /**
   * 
   */
  private Classifier classifier;

  /**
   * 
   */
  private Instances trainingData;

  private Random generator;

  /** */
  private Text contentResult = new Text();

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void map(Object key, Text value,
    OutputCollector<Classification, Text> output, Reporter reporter)
    throws IOException {
    String contents = value.toString();
    StringTokenizer tokenizer = new StringTokenizer(contents, "\n");
    while (tokenizer.hasMoreTokens()) {
      String contentStr = tokenizer.nextToken();
      try {
        //Content
        Content content = JSon.getFromJSONString(contentStr, Content.class);
        Classification classification = content.getClassification();

        System.out.println("# content: " + content.getContent().toString());

        //
        long counter = reporter.getCounter(Counter.ARFF_FILE_NAME).getCounter();
        String fileName = FILE_NAME + counter + ".arff";

        ClassifierWithIndexAsTrainingData.getTestingDataUsingIndex(
          INDEX_LOCATION, content.getContent().toString(), fileName);

        //
        reporter.incrCounter(Counter.ARFF_FILE_NAME, 1);

        //Recovering instanceData
        Instances testingData = ClassifierUtil.loadArffFile(FILE_NAME);

        //Copying attributes
        testingData =
          ArffUtil.copyClassAttributes(this.trainingData, testingData);

        //
        Instances labeled = new Instances(testingData);
        // copy class values from training data to the labeled
        for (int i = 0; i < testingData.numInstances(); i++) {
          double clsLabel =
            this.classifier.classifyInstance(testingData.instance(i));
          labeled.instance(i).setClassValue(clsLabel);
        }

        for (int i = 0; i < labeled.numInstances(); i++) {
          String theClass =
            labeled.get(i).stringValue(labeled.attribute("class"));

          System.out.println("## class: " + theClass);

          //
          boolean collect = false;
          if (interestedClasses != null && interestedClasses.length > 0) {
            for (String interestedClass : interestedClasses) {
              if (interestedClass.equalsIgnoreCase(theClass)) {
                collect = true;
                break;
              }
            }
          }
          else {
            collect = true;
          }

          //
          if (collect) {
            Classification newClassification =
              new Classification(classification);
            newClassification.putClassification(AnalysisResult.Type.SEMANTIC
              .name(), theClass);
            content.setClassification(newClassification);
            contentResult.set(JSon.toJSONString(Content.class, content));
            output.collect(newClassification, contentResult);
          }
        }
      }
      catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void configure(JobConf job) {
    String trainingDataArff = job.get(TRAINING_DATA_ARFF);
    String trainingIndexFolder = job.get(TRAINING_INDEX_FOLDER);
    //Creating instances from training data     
    FSDataInputStream in = null;
    try {
      FileSystem fileSystem = FileSystem.get(URI.create(trainingDataArff), job);
      in = fileSystem.open(new Path(trainingDataArff));
      this.trainingData = ClassifierUtil.loadArffFileInputStream(in);
      this.classifier = ClassifierUtil.trainClassifier(this.trainingData);

      System.out.println("trainingData size: " + this.trainingData.size());

      Path indexPath = new Path(trainingIndexFolder);
      fileSystem = FileSystem.get(indexPath.toUri(), job);
      if (fileSystem.isDirectory(indexPath)) {
        FileStatus[] files = fileSystem.listStatus(indexPath);
        System.out.println("# files: " + files.length);
        for (FileStatus fs : files) {
          if (!fs.isDir()) {
            Path dst = new Path(INDEX_LOCATION + "/" + fs.getPath().getName());
            System.out.println("#-> " + dst.toUri());
            fileSystem.copyToLocalFile(fs.getPath(), dst);
          }
        }
        //fileSystem.close();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (in != null) {
        IOUtils.closeStream(in);
      }
    }
    //
    this.interestedClasses = job.getStrings(INTERESTED_CLASSES);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }
}
