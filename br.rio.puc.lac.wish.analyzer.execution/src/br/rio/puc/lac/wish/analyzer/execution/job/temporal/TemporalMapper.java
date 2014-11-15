package br.rio.puc.lac.wish.analyzer.execution.job.temporal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class TemporalMapper implements
  Mapper<Object, Text, Classification, Text> {

  public static final String INITIAL_TIME = "initial.time";
  public static final String END_TIME = "end.time";
  public static final String TIME_WINDOW = "time.window";
  public static final String WINDOW_MOVE = "window.move";

  /**
     * 
     */
  private long initialTime;

  /**
     * 
     */
  private long endTime;

  /**
     * 
     */
  private long timeWindow;

  /**
     * 
     */
  private long windowMove;

  /** */
  private Text contentResult = new Text();

  /**
   * 
   * @param contentTime
   * @return
   */
  private List<TimeWindow> getTimeWindows(long contentTime) {
    if (contentTime >= initialTime && contentTime <= endTime) {
      long currentTime = initialTime;
      List<TimeWindow> timeWindows = new ArrayList<TimeWindow>();
      int i = 0;
      while ((contentTime >= currentTime) && (currentTime < endTime)) {
        i++;
        if (contentTime < (currentTime + timeWindow)) {
          timeWindows.add(new TimeWindow(i, currentTime, currentTime
            + timeWindow));
        }
        currentTime = currentTime + windowMove;
      }
      return timeWindows;
    }
    return null;
  }

  /**
     */
  @Override
  public void map(Object key, Text value,
    OutputCollector<Classification, Text> output, Reporter reporter)
    throws IOException {
    String contents = value.toString();
    StringTokenizer tokenizer = new StringTokenizer(contents, "\n");
    while (tokenizer.hasMoreTokens()) {
      String contentStr = tokenizer.nextToken();
      try {
        //
        Content content = JSon.getFromJSONString(contentStr, Content.class);

        Classification classification = content.getClassification();

        List<TimeWindow> timeWindows = getTimeWindows(content.getTimestamp());

        if (timeWindows != null) {
          for (TimeWindow timeWindow : timeWindows) {
            if (timeWindow != null) {
              Classification newClassification =
                new Classification(classification);
              newClassification.putClassification(AnalysisResult.Type.TEMPORAL
                .name(), String.valueOf(timeWindow.getNumber()));
              newClassification.putInformation(AnalysisResult.Type.TEMPORAL
                .name(), timeWindow.toString());
              content.setClassification(newClassification);
              contentResult.set(JSon.toJSONString(Content.class, content));
              output.collect(newClassification, contentResult);
            }
          }
        }
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
    this.initialTime = job.getLong(INITIAL_TIME, 0);
    this.endTime = job.getLong(END_TIME, 1);
    this.timeWindow = job.getLong(TIME_WINDOW, 1);
    this.windowMove = job.getLong(WINDOW_MOVE, 1);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }
}
