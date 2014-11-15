package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

public interface IAggregatorJob extends IJob {

  @Override
  public Class<? extends AbstractAggregatorMapper> getMapper(Properties props,
    JobConf job);

  @Override
  public Class<? extends AggregatorReducer> getReducer(Properties props,
    JobConf job);

}
