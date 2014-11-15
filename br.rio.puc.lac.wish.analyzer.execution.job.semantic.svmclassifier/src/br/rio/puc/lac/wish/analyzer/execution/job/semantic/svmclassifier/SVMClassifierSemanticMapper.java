package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.AllInstancesInClass;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.DocumentToClassify;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.ClassifierTraining;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SVMClassifier;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SentenceAnalysis;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SentenceAnalysis.Language;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme.Theme;
import br.rio.puc.lac.wish.analyzer.utils.HadoopUtils;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * @author Marcelo Malcher
 */
public class SVMClassifierSemanticMapper implements
  Mapper<Object, Text, Classification, Text> {

  public final static String CLASSIFIER_LANGUAGE = "classifier.language";
  public final static String CLASSIFIER_STOPWORDS_FILE =
    "classifier.stopwords.file";

  public final static String CLASSIFIER_NUMBER = "classifier.number";
  public final static String CLASSIFIER_NAME = "classifier.name_";
  public final static String CLASSIFIER_FILE = "classifier.file_";

  private SentenceAnalysis sentenceAnalysis;

  private SVMClassifier classifier;

  private Map<Double, String> mapClassValue;

  /**
   * The logger of this class
   */
  private static Logger logger;

  public SVMClassifierSemanticMapper() {
    logger = Logger.getLogger(SVMClassifierSemanticJob.class.getSimpleName());
  }

  /**
     * 
     */
  private List<Theme> themes;

  /** */
  private Text contentResult = new Text();

  /**
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
        Content content = JSon.getFromJSONString(contentStr, Content.class);
        Classification classification = content.getClassification();

        //Do some preparation to content?
        String contentMessage = content.getContent().toString();

        List<String> contentToClassifyList = new ArrayList<String>();
        contentToClassifyList.add(contentMessage);

        List<DocumentToClassify> documentToClassifyList =
          sentenceAnalysis.getListOfDocumentsToClassify("unseen", 0,
            contentToClassifyList);

        double classValue = -1;

        //TODO Documento-To-Classify list always will have size equals to 1! No need to use list here!
        for (DocumentToClassify document : documentToClassifyList) {
          classValue = classifier.classifyUnseenDocument(document);
          logger.info("Content: " + contentMessage + " >>> classification = "
            + classValue);
        }

        String className = this.mapClassValue.get(classValue);
        if (className != null) {
          Classification newClassification = new Classification(classification);
          newClassification.putClassification(AnalysisResult.Type.SEMANTIC
            .name(), className);
          newClassification.putInformation(AnalysisResult.Type.SEMANTIC.name(),
            String.valueOf(classValue));
          content.setClassification(newClassification);
          contentResult.set(JSon.toJSONString(Content.class, content));
          output.collect(newClassification, contentResult);
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
    //Sentence Analysis
    String language = job.get(CLASSIFIER_LANGUAGE);
    logger.info("# SVM Classifier Mapper language: " + language);
    String stopWordFileName = job.get(CLASSIFIER_STOPWORDS_FILE);
    logger.info("# SVM Classifier Mapper stop word file: " + stopWordFileName);
    //
    List<String> stopWordList =
      HadoopUtils.readFileAndReturnListOfLines(job, new Path(stopWordFileName));
    //
    Language lang = Language.ENGLISH;
    if (language.equals("PB")) {
      lang = Language.BRAZILIAN_PORTUGUESE;
    }
    this.sentenceAnalysis = new SentenceAnalysis(lang, stopWordList);

    //Map
    this.mapClassValue = new HashMap<Double, String>();

    //Training sets
    List<AllInstancesInClass> trainingClasses =
      new ArrayList<AllInstancesInClass>();
    int number = job.getInt(CLASSIFIER_NUMBER, 0);
    for (int i = 0; i < number; i++) {
      //Name and filename
      String name = job.get(CLASSIFIER_NAME + i);
      String fileName = job.get(CLASSIFIER_FILE + i);
      //Reading training set
      List<String> lines =
        HadoopUtils.readFileAndReturnListOfLines(job, new Path(fileName));
      //                
      AllInstancesInClass allInstancesInClass =
        new AllInstancesInClass(name, i + 1, sentenceAnalysis
          .getListOfDocumentsToClassify(name, i + 1, lines));
      trainingClasses.add(allInstancesInClass);
      //
      this.mapClassValue.put(new Double(i + 1), name);
    }
    //
    ClassifierTraining training = new ClassifierTraining(trainingClasses);
    this.classifier = new SVMClassifier(training);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }
}
