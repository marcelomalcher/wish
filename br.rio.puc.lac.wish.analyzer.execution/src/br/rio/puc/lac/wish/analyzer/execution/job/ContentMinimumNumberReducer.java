package br.rio.puc.lac.wish.analyzer.execution.job;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * @author Marcelo Malcher
 */
public class ContentMinimumNumberReducer implements
  Reducer<Classification, Text, NullWritable, Text> {

  /**
   * 
   */
  public final static String MINIMUM_VALUE = "minimum.value";

  /**
   * 
   */
  public final static String MINIMUM_DIFF_USERS = "minimum.diff.users";

  /**
   * 
   */
  private double minimumValue;

  /**
   * 
   */
  private double minimumDiffUsers;

  /** */
  private Text contentText = new Text();

  /** */
  private NullWritable nullWritable = NullWritable.get();

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void reduce(Classification key, Iterator<Text> values,
    OutputCollector<NullWritable, Text> output, Reporter reporter)
    throws IOException {
    Set<Content> contents = new HashSet<Content>();
    Set<String> users = new HashSet<String>();
    while (values.hasNext()) {
      Text text = values.next();
      Content content = JSon.getFromJSONString(text.toString(), Content.class);
      contents.add(content);
      users.add(content.getCreatorId().toString());
    }
    if (getRelatedContentValue(contents) >= this.minimumValue) {
      if (users.size() >= this.minimumDiffUsers) {
        for (Content content : contents) {
          String value = JSon.toJSONString(Content.class, content);
          contentText.set(value);
          output.collect(nullWritable, contentText);
        }
      }
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void configure(JobConf job) {
    this.minimumValue = job.getFloat(MINIMUM_VALUE, 1);
    this.minimumDiffUsers = job.getFloat(MINIMUM_DIFF_USERS, 1);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }

  /**
   * 
   * @param contents
   * @return
   */
  public double getRelatedContentValue(Set<Content> contents) {
    return contents.size();
  }
}