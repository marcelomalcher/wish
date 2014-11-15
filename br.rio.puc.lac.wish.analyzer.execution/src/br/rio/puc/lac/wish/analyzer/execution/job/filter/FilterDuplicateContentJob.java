package br.rio.puc.lac.wish.analyzer.execution.job.filter;

import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;

import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

public class FilterDuplicateContentJob implements IJob {

  @Override
  public String getName() {
    return "FilterDuplicateContent";
  }

  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {
    return FilterDuplicateContentMapper.class;
  }

  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {
    return FilterDuplicateContentReducer.class;
  }

  @Override
  public Class<?> getOutputKeyClass() {
    return Text.class;
  }

  @Override
  public Class<?> getOutputValueClass() {
    return Text.class;
  }

}
