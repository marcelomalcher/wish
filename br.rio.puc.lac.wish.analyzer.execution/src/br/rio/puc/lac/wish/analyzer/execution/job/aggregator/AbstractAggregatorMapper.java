package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public abstract class AbstractAggregatorMapper implements
  Mapper<Object, Text, AggregatorGroup, Text> {

  public enum Counter {
    MERGED_RESULTS;
  }

  public final static String MAXIMUM_DISTANCE = "maximum.distance";
  public final static String ANALYSIS_RESULT = "analysis.results.";
  public final static String ANALYSIS_RESULT_NUMBER = "analysis.results.number";

  public final static String TEMPORAL_WEIGHT = "temporal.weigth";
  public final static String SEMANTIC_WEIGHT = "semantic.weigth";
  public final static String SPATIAL_WEIGHT = "spatial.weigth";

  public final static String TEMPORAL_DISTANCE = "temporal.distance";
  public final static String SEMANTIC_DISTANCE = "semantic.distance";
  public final static String SPATIAL_DISTANCE = "spatial.distance";

  private IClassificationDistance semanticDistance;
  private IClassificationDistance spatialDistance;
  private IClassificationDistance temporalDistance;

  private float temporalWeight;
  private float semanticWeight;
  private float spatialWeight;

  /**
     * 
     */
  private float maximumDistance;

  /**
     * 
     */
  private List<AnalysisResult> analysisResults;

  /**
   * 
   */
  private Map<String, Class> classMap;

  /**
     * 
     */
  private Text analysisResultText = new Text();

  /**
     */
  @Override
  public void map(Object key, Text value,
    OutputCollector<AggregatorGroup, Text> output, Reporter reporter)
    throws IOException {
    //
    String results = value.toString();
    StringTokenizer tokenizer = new StringTokenizer(results, "\n");
    while (tokenizer.hasMoreTokens()) {
      String resultStr = tokenizer.nextToken();
      try {
        //recovering analysis result
        AnalysisResult result =
          JSon.getFromJSONString(resultStr, AnalysisResult.class, classMap);
        System.out.println("Result: " + result.toString());
        //creating aggregation group
        AggregatorGroup group = new AggregatorGroup(result.getId());
        //Iterating through analysis results
        for (AnalysisResult otherResult : analysisResults) {
          //
          System.out.println("- Other result: " + otherResult.toString());
          boolean foundSimilarity = false;
          //checking if it is not calculating distance from the same result
          if (result.compareTo(otherResult) != 0) {
            //TODO review asap! propably componetize this distance calculation
            //Ugly!
            for (Content content : result.getContents()) {
              System.out.println("# content: " + content.toString());
              //
              for (Content otherContent : otherResult.getContents()) {
                System.out.println("# other content: "
                  + otherContent.toString());
                //                
                if (content.compareTo(otherContent) == 0) {
                  foundSimilarity = true;
                  break;
                }
                else {
                  System.out.println("calculating distance");
                  //semantic distance
                  double semantic =
                    semanticDistance.getDistance(content, otherContent,
                      semanticWeight);
                  //spatial distance
                  double spatial =
                    spatialDistance.getDistance(content, otherContent,
                      spatialWeight);
                  //temporal distance
                  double temporal =
                    temporalDistance.getDistance(content, otherContent,
                      temporalWeight);
                  //calculating total distance 
                  double distance = Math.sqrt(semantic + spatial + temporal);
                  System.out.println("distance = " + distance);
                  //verifying if results are similar 
                  if (distance <= maximumDistance) {
                    System.out.println("found similarity by calculation");
                    //Found similarity
                    foundSimilarity = true;
                    break;
                  }
                }
              }
              //
              if (foundSimilarity) {
                break;
              }
            }
          }
          //Checking if results are similar...
          if (foundSimilarity) {
            System.out.println("applying similiarity");
            //
            reporter.incrCounter(
              AbstractAggregatorMapper.Counter.MERGED_RESULTS, 1);
            //
            group.setId(otherResult.getId());
            break;
          }
        }
        analysisResultText.set(resultStr);
        output.collect(group, analysisResultText);
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
    classMap = new HashMap<String, Class>();
    classMap.put("contents", Content.class);
    //
    this.maximumDistance = job.getFloat(MAXIMUM_DISTANCE, 1);
    System.out.println("Maximum distance = " + maximumDistance);
    //      
    this.analysisResults = new ArrayList<AnalysisResult>();
    int number = job.getInt(ANALYSIS_RESULT_NUMBER, 0);
    if (number > 0) {
      for (int i = 0; i < number; i++) {
        this.analysisResults.add(JSon.getFromJSONString(job.get(ANALYSIS_RESULT
          + i), AnalysisResult.class, classMap));
      }
    }
    System.out.println("Result size = " + number);
    //
    this.semanticWeight = job.getFloat(SEMANTIC_WEIGHT, 0.25f);
    System.out.println("Semantic weight = " + semanticWeight);
    this.spatialWeight = job.getFloat(SPATIAL_WEIGHT, 0.75f);
    System.out.println("Spatial weight = " + spatialWeight);
    this.temporalWeight = job.getFloat(TEMPORAL_WEIGHT, 0.50f);
    System.out.println("Temporal weight = " + temporalWeight);

    //
    semanticDistance = getSemanticClassificationDistance(job);
    spatialDistance = getSpatialClassificationDistance(job);
    temporalDistance = getTemporalClassificationDistance(job);
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
   */
  public abstract IClassificationDistance getSemanticClassificationDistance(
    JobConf job);

  /**
   * 
   */
  public abstract IClassificationDistance getSpatialClassificationDistance(
    JobConf job);

  /**
   * 
   */
  public abstract IClassificationDistance getTemporalClassificationDistance(
    JobConf job);

}
