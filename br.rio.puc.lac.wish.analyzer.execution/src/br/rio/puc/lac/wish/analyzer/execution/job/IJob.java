package br.rio.puc.lac.wish.analyzer.execution.job;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

public interface IJob {

  public String getName();

  public Class<? extends Mapper> getMapper(Properties props, JobConf job);

  public Class<? extends Reducer> getReducer(Properties props, JobConf job);

  public Class<?> getOutputKeyClass();

  public Class<?> getOutputValueClass();

}
