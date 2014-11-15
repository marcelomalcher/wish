package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.distance.ExclusiveSemanticClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.distance.ExclusiveBoxSpatialClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.sequential.SequentialDividerSpatialBoxMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.TemporalMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.distance.ExclusiveTimeWindowTemporalClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ExclusiveAggregatorMapper extends AbstractAggregatorMapper {

  @Override
  public IClassificationDistance getSemanticClassificationDistance(JobConf job) {
    return new ExclusiveSemanticClassificationDistance();
  }

  @Override
  public IClassificationDistance getSpatialClassificationDistance(JobConf job) {
    double width =
      job.getFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_WIDTH, 0);
    double height =
      job.getFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_HEIGHT, 0);
    return new ExclusiveBoxSpatialClassificationDistance(width, height);
  }

  @Override
  public IClassificationDistance getTemporalClassificationDistance(JobConf job) {
    long timeWindow = job.getLong(TemporalMapper.TIME_WINDOW, 0);
    return new ExclusiveTimeWindowTemporalClassificationDistance(timeWindow);
  }
}
