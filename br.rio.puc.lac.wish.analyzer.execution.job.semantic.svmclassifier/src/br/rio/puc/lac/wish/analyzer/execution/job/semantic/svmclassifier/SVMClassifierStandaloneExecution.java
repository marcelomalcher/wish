package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;

import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.AllInstancesInClass;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.DocumentToClassify;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.ClassifierTraining;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SVMClassifier;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SentenceAnalysis;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils.SentenceAnalysis.Language;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 * @email marcelom@inf.puc-rio.br
 */
public class SVMClassifierStandaloneExecution {

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    //Checking parameters
    if (args.length != 1) {
      System.out.println("The <properties-file> parameter is mandatory.");
      System.exit(-1);
    }
    Properties props = new Properties();
    try {
      props.load(new FileInputStream(args[0]));
    }
    catch (FileNotFoundException e) {
      System.out.println("Properties file does not exists: " + args[0]);
      e.printStackTrace();
      System.exit(-1);
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    //Setting logger configuration
    PropertyConfigurator.configure(props);
    DateFormat df = new SimpleDateFormat("yyyyMMdd");
    String s = df.format(Calendar.getInstance().getTime());
    logger =
      Logger.getLogger(SVMClassifierStandaloneExecution.class.getSimpleName() + "_" + s);

    //training    
    int numberOfTrainingSets =
      Integer.valueOf(props.getProperty("classifier.trainingset.number")); //e.g.: 3;
    String[] trainingSets = new String[numberOfTrainingSets];
    logger.info("# Number of training sets: " + numberOfTrainingSets);
    for (int i = 0; i < numberOfTrainingSets; i++) {
      String trainingSetFileName =
        props.getProperty("classifier.trainingset.file." + i);
      logger.info("# Training set [" + i + "] - " + trainingSetFileName);
      trainingSets[i] = trainingSetFileName;
    }

    //Sentence
    String language = props.getProperty("sentence.analysis.language");
    logger.info("# Sentence Analysis Language - " + language);
    String stopWordFileName =
      props.getProperty("sentence.analysis.stopwords.file");
    logger
      .info("# Sentence Analysis Stop Word File Name - " + stopWordFileName);

    //
    List<String> stopWordList = getLinesFromFile(stopWordFileName);

    SentenceAnalysis analysis =
      new SentenceAnalysis(Language.ENGLISH, stopWordList);

    List<AllInstancesInClass> trainingClasses =
      new ArrayList<AllInstancesInClass>();
    int i = 1;
    for (String trainingSet : trainingSets) {

      //
      AllInstancesInClass allInstancesInClass =
        new AllInstancesInClass(trainingSet, i, analysis
          .getListOfDocumentsToClassify(trainingSet, i,
            getLinesFromFile(trainingSet)));
      trainingClasses.add(allInstancesInClass);

      //
      i++;
    }

    //
    ClassifierTraining training = new ClassifierTraining(trainingClasses);

    //
    SVMClassifier classifier = new SVMClassifier(training);

    //Unseen
    String sentenceToClassify = props.getProperty("classify.sentence");
    List<String> sentenceToClassifyList = new ArrayList<String>();
    sentenceToClassifyList.add(sentenceToClassify);

    List<DocumentToClassify> documentToClassifyList =
      analysis
        .getListOfDocumentsToClassify("unseen", 0, sentenceToClassifyList);

    for (DocumentToClassify document : documentToClassifyList) {
      double value = classifier.classifyUnseenDocument(document);
      logger.info("Class value = " + value);
    }
  }

  /**
   * 
   * @param fileName
   * @return
   */
  public static List<String> getLinesFromFile(String fileName) {
    List<String> lines = new ArrayList<String>();
    ;
    try {
      BufferedReader input = new BufferedReader(new FileReader(fileName));
      String line = null;
      while ((line = input.readLine()) != null) {
        lines.add(line);
      }
      input.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

}
