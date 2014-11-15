package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Region;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public abstract class AbstractSpatialBoxMapper implements
  Mapper<Object, Text, Classification, Text> {

  public final static String MAIN_REGION = "main.region";

  public static final String NORTHEAST_LATITUDE = "northeast-latitude";
  public static final String NORTHEAST_LONGITUDE = "northeast-longitude";
  public static final String SOUTHWEST_LATITUDE = "southwest-latitude";
  public static final String SOUTHWEST_LONGITUDE = "southwest-longitude";

  private Map<String, Region> regionsMap;

  /** */
  private Text contentResult = new Text();

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
        Content content = JSon.getFromJSONString(contentStr, Content.class);
        Classification classification = content.getClassification();
        for (Entry<String, Region> entry : regionsMap.entrySet()) {
          if (entry.getValue().isLocationInside(content.getLocation())) {
            Classification newClassification =
              new Classification(classification);
            //
            newClassification.putClassification(AnalysisResult.Type.SPATIAL
              .name(), entry.getKey());
            newClassification.putInformation(
              AnalysisResult.Type.SPATIAL.name(), entry.getValue().toString());
            content.setClassification(newClassification);
            contentResult.set(JSon.toJSONString(Content.class, content));
            output.collect(newClassification, contentResult);
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
    //
    Region mainRegion =
      JSon.getFromJSONString(job.get(MAIN_REGION), Region.class);
    this.regionsMap = this.divideRegion(job, mainRegion);
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
   * @param job
   * @param mainRegion
   * @return
   */
  public abstract Map<String, Region> divideRegion(JobConf job,
    Region mainRegion);

}
