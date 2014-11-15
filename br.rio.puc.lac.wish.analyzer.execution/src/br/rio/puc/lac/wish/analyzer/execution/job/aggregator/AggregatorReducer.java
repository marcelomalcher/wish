package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * @author Marcelo Malcher
 */
public class AggregatorReducer implements
  Reducer<AggregatorGroup, Text, NullWritable, Text> {

  /** */
  private Text situationText = new Text();

  /** */
  private NullWritable nullWritable = NullWritable.get();

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void reduce(AggregatorGroup key, Iterator<Text> values,
    OutputCollector<NullWritable, Text> output, Reporter reporter)
    throws IOException {

    //
    Map<String, Class> classMap = new HashMap<String, Class>();
    classMap.put("contents", Content.class);

    //Creating analysis result
    AnalysisResult aggregatedResult = new AnalysisResult(key.getId());

    //Iterating through results..
    while (values.hasNext()) {
      //Getting result from text
      Text text = values.next();
      AnalysisResult result =
        JSon.getFromJSONString(text.toString(), AnalysisResult.class, classMap);

      //Adding all semantics to the aggregated result
      aggregatedResult.getSemantics().addAll(result.getSemantics());

      //Adding contents to the aggregated result
      for (Content content : result.getContents()) {
        aggregatedResult.addContent(content);
      }
    }

    //General counter - used to compute average timestamp
    int counter = 0;

    //Avergage timestamp    
    long averageTimestamp = 0;

    //Location counter - used to compute average latitude and longitude
    int locationCounter = 0;

    //Average location
    double averageLatitude = 0;
    double averageLongitude = 0;

    for (Content c : aggregatedResult.getContents()) {
      //Incrementing general counter 
      counter++;

      //Adding result's timestamp to current total
      averageTimestamp += c.getTimestamp();

      //Acquiring location by latitude and longitude coordinates
      Location location = c.getLocation();
      if (location != null) {
        if (location.getLatitude() != null && location.getLongitude() != null) {
          locationCounter++;
          averageLatitude += location.getLatitude();
          averageLongitude += location.getLongitude();
        }
      }
    }

    //Computing average timestamps
    averageTimestamp = (averageTimestamp) / counter;
    aggregatedResult.setTemporal(averageTimestamp); //Setting temporal

    //Computing location averages
    averageLatitude = averageLatitude / locationCounter;
    averageLongitude = averageLongitude / locationCounter;
    aggregatedResult
      .setSpatial(new Location(averageLatitude, averageLongitude)); //Setting location

    //Writing to ouput after aggregation
    situationText.set(JSon.toJSONString(AnalysisResult.class, aggregatedResult,
      classMap));
    output.collect(nullWritable, situationText);
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