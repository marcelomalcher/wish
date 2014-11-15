package br.rio.puc.lac.wish.analyzer.execution.job.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class FilterDuplicateContentReducer implements
  Reducer<Text, Text, NullWritable, Text> {

  /** */
  private Text resultText = new Text();

  /** */
  private NullWritable nullWritable = NullWritable.get();

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void reduce(Text key, Iterator<Text> values,
    OutputCollector<NullWritable, Text> output, Reporter reporter)
    throws IOException {

    int counter = 0;

    Map<String, Content> semanticContentMap = new HashMap<String, Content>();

    //Iterating through contents..    
    while (values.hasNext()) {
      //retrieving content from duplicate list
      String text = values.next().toString();
      Content content = JSon.getFromJSONString(text, Content.class);
      //adding content to content map by its semantic classification
      semanticContentMap.put(content.getClassification().getClassification(
        AnalysisResult.Type.SEMANTIC.name()), content);
      //incrementing counter
      counter++;
    }

    System.out.println("Number of duplicates = " + counter);

    //Class map for JSon 
    Map<String, Class> classMap = new HashMap<String, Class>();
    classMap.put("contents", Content.class);

    //iterating through contents
    for (Entry<String, Content> entry : semanticContentMap.entrySet()) {
      Content content = entry.getValue();
      //Need to change id of content?
      //content.newId();
      //creating analysis result for each content
      AnalysisResult result = new AnalysisResult(entry.getValue());
      result.addSemantic(entry.getKey());
      result.setTemporal(entry.getValue().getTimestamp());
      result.setSpatial(entry.getValue().getLocation());

      resultText.set(JSon.toJSONString(AnalysisResult.class, result, classMap));
      output.collect(nullWritable, resultText);
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
