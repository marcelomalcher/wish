package br.rio.puc.lac.wish.analyzer.execution.job.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class FilterDuplicateContentMapper implements
  Mapper<Object, Text, Text, Text> {

  /** */
  private Text idResult = new Text();

  /** */
  private Text contentResult = new Text();

  /**
     */
  @Override
  public void map(Object key, Text value, OutputCollector<Text, Text> output,
    Reporter reporter) throws IOException {
    String contents = value.toString();
    StringTokenizer tokenizer = new StringTokenizer(contents, "\n");
    while (tokenizer.hasMoreTokens()) {
      String contentStr = tokenizer.nextToken();
      try {
        Content content = JSon.getFromJSONString(contentStr, Content.class);
        idResult.set(content.getId());
        contentResult.set(contentStr);
        output.collect(idResult, contentResult);
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

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }
}