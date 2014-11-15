package br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.utils;

import java.util.List;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.AllInstancesInClass;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.svmclassifier.model.DocumentToClassify;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class SVMClassifier {

  private svm_parameter param; // set by parse_command_line
  private svm_problem prob; // set by read_problem
  private svm_model model; //svm_model
  private String input_file_name; // set by parse_command_line
  private String model_file_name; // set by parse_command_line
  private String error_msg;
  private int cross_validation;

  private int nr_fold;

  private static svm_print_interface svm_print_null =
    new svm_print_interface() {
      @Override
      public void print(String s) {
      }
    };

  private ClassifierTraining classifierTraining;

  /**
   * 
   * @param classifierTraining
   */
  public SVMClassifier(ClassifierTraining classifierTraining) {
    this.classifierTraining = classifierTraining;
    this.train();
  }

  /**
   * 
   */
  private void train() {
    int max_index =
      getMaxIndex(this.classifierTraining.getAllInstancesInClasses());
    //building the train files
    Vector<Double> vy_train = new Vector<Double>();
    Vector<svm_node[]> vx_train = new Vector<svm_node[]>();
    //Training
    //use all documents in the allClass as training
    for (AllInstancesInClass aic : this.classifierTraining
      .getAllInstancesInClasses()) {
      for (DocumentToClassify document : aic.getInstances()) {
        svm_node[] x = document.getSVMFeatureVector();
        vx_train.addElement(x);
        vy_train.addElement(document.getValue());
      }
    }

    //training  the model 
    setParameters();
    read_problem(vy_train, vx_train, max_index);
    model = svm.svm_train(prob, param);
  }

  /**
   * 
   * it takes the training data, it trains with it and then it outputs labels
   * for each of the documents in the unseen data
   * 
   * @param unseenData the unseen data should be here
   */
  public void classifyUnseenData(AllInstancesInClass unseenData) {
    //
    this.classifierTraining.buildInstancesClass(unseenData);
    //the test documents are the unseen things
    for (DocumentToClassify document : unseenData.getInstances()) {
      double v = classifyUnseenDocument(document);
    }
  }

  /**
   * 
   * @param document
   * @return
   */
  public double classifyUnseenDocument(DocumentToClassify document) {
    //
    this.classifierTraining.buildDocument(document);
    // returns predicted class value
    return svm.svm_predict(model, document.getSVMFeatureVector());
  }

  /**
   * 
   * @param allClasses
   * @return
   */
  private int getMaxIndex(List<AllInstancesInClass> allClasses) {
    int max_index = 0;
    for (AllInstancesInClass aic : allClasses) {
      if (max_index <= get_max_index(aic.getInstances())) {
        max_index = get_max_index(aic.getInstances());
      }
    }

    return max_index;
  }

  private int get_max_index(List<DocumentToClassify> allQueryTemplatesItems) {
    int max_index = 0;
    for (DocumentToClassify document : allQueryTemplatesItems) {
      svm_node[] x = document.getSVMFeatureVector();
      max_index = Math.max(max_index, x[x.length - 1].index);
    }
    return max_index;
  }

  /**
   * 
   */
  private void setParameters() {
    //svm_print_interface print_func = null; // default printing to stdout
    param = new svm_parameter();
    // default values
    param.svm_type = svm_parameter.NU_SVC;
    param.kernel_type = svm_parameter.LINEAR;
    param.degree = 3;
    param.gamma = 0; // 1/num_features
    param.coef0 = 0;
    param.nu = 0.5;
    param.cache_size = 100;
    param.C = 0.5;
    param.eps = 1e-3;
    param.p = 0.1;
    param.shrinking = 1;
    param.probability = 0;
    param.nr_weight = 0;
    param.weight_label = new int[0];
    param.weight = new double[0];
    cross_validation = 0;
  }

  /**
   * 
   * @param vy
   * @param vx
   * @param max_index
   */
  private void read_problem(Vector<Double> vy, Vector<svm_node[]> vx,
    int max_index) {
    prob = new svm_problem();
    prob.l = vy.size();
    prob.x = new svm_node[prob.l][];
    for (int i = 0; i < prob.l; i++) {
      prob.x[i] = vx.elementAt(i);
    }
    prob.y = new double[prob.l];
    for (int i = 0; i < prob.l; i++) {
      prob.y[i] = vy.elementAt(i);
    }

    if (param.gamma == 0 && max_index > 0) {
      param.gamma = 1.0 / max_index;
    }
    if (param.kernel_type == svm_parameter.PRECOMPUTED) {
      for (int i = 0; i < prob.l; i++) {
        if (prob.x[i][0].index != 0) {
          System.err
            .print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
          System.exit(1);
        }
        if ((int) prob.x[i][0].value <= 0
          || (int) prob.x[i][0].value > max_index) {
          System.err
            .print("Wrong input format: sample_serial_number out of range\n");
          System.exit(1);
        }
      }
    }
  }

}