package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public abstract class AbstractAggregatorJob implements IAggregatorJob {

  @Override
  public String getName() {
    return "Aggregator";
  }

  @Override
  public Class<?> getOutputKeyClass() {
    return AggregatorGroup.class;
  }

  @Override
  public Class<?> getOutputValueClass() {
    return Text.class;
  }

  @Override
  public abstract Class<? extends AbstractAggregatorMapper> getMapper(
    Properties props, JobConf job);

  @Override
  public Class<? extends AggregatorReducer> getReducer(Properties props,
    JobConf job) {
    return AggregatorReducer.class;
  }
}
