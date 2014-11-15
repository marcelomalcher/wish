package br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reducer;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.execution.job.semantic.QuantitySemanticReducer;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class QuantityThemeSemanticJob extends BasicThemeSemanticJob {

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   */
  public QuantityThemeSemanticJob() {
    logger = Logger.getLogger(QuantityThemeSemanticJob.class.getSimpleName());
  }

  @Override
  public String getName() {
    return "SemanticThemeQuantity";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {
    int minimumNumber =
      Integer.valueOf(props.getProperty("semantic.minimum-quantity-value")); //e.g.: 5;
    job.setInt(QuantitySemanticReducer.MINIMUM_VALUE, minimumNumber);
    logger.info("# Semantic Minimum Value: " + minimumNumber);
    return QuantitySemanticReducer.class;
  }
}
