package br.rio.puc.lac.wish.analyzer.execution.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import br.rio.puc.lac.wish.analyzer.execution.AnalysisExecution;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;
import br.rio.puc.lac.wish.analyzer.execution.job.aggregator.ExclusiveAggregatorJob;
import br.rio.puc.lac.wish.analyzer.execution.job.aggregator.IAggregatorJob;
import br.rio.puc.lac.wish.analyzer.execution.job.filter.FilterDuplicateContentJob;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme.QuantityThemeSemanticJob;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.sequential.SequentialDividerSpatialBoxJob;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.TemporalJob;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class QuantityThemeSequentialRegionExclusiveAggregationExecution {

  /**
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    //Checking parameters
    if (args.length != 1) {
      System.out.println("The <properties-file> parameter is mandatory.");
      System.exit(-1);
    }
    //Retrieving properties...
    Properties props = new Properties();
    try {
      props.load(new FileInputStream(args[0]));
    }
    catch (FileNotFoundException e) {
      System.out.println("Properties file does not exists: " + args[0]);
      e.printStackTrace();
      System.exit(-1);
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    //
    List<IJob> jobs = new ArrayList<IJob>();
    jobs.add(new TemporalJob());
    jobs.add(new SequentialDividerSpatialBoxJob());
    jobs.add(new QuantityThemeSemanticJob());
    //
    jobs.add(new FilterDuplicateContentJob());
    //
    IAggregatorJob aggregatorJob = new ExclusiveAggregatorJob();
    //
    AnalysisExecution execution =
      new AnalysisExecution(jobs, aggregatorJob, props);
    //
    int res = ToolRunner.run(new Configuration(), execution, args);
    System.exit(res);
  }
}
