package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka;

import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.execution.job.ContentMinimumNumberReducer;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

public class ClassifierSemanticJob implements IJob {

  @Override
  public String getName() {
    return "SemanticClassifier";
  }

  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {
    String trainingData =
      props.getProperty("semantic.classifier.training.data");
    job.set(ClassifierSemanticMapper.TRAINING_DATA_ARFF, trainingData);
    String indexFolder =
      props.getProperty("semantic.classifier.training.index.folder");
    job.set(ClassifierSemanticMapper.TRAINING_INDEX_FOLDER, indexFolder);

    //Interested classes
    String interestedClasses =
      props.getProperty("semantic.classifier.interested-classes");
    job.setStrings(ClassifierSemanticMapper.INTERESTED_CLASSES,
      interestedClasses);

    System.out.println("# SemanticMapper attributes - training: "
      + trainingData + " - classes: " + interestedClasses);

    return ClassifierSemanticMapper.class;
  }

  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {
    int minimumValue =
      Integer.valueOf(props.getProperty("semantic.minimum-number")); //e.g.: 5;
    job.setInt(ContentMinimumNumberReducer.MINIMUM_VALUE, minimumValue);

    System.out.println("# SemanticReducer attribute - number: " + minimumValue);

    return ContentMinimumNumberReducer.class;
  }

  @Override
  public Class<?> getOutputKeyClass() {
    return Classification.class;
  }

  @Override
  public Class<?> getOutputValueClass() {
    return Text.class;
  }

}
