package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.execution.job.IClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.semantic.distance.ExclusiveSemanticClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.distance.RelativeSpatialClassificationDistance;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.TemporalMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.distance.RelativeTemporalClassificationDistance;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class RelativeAggregatorMapper extends AbstractAggregatorMapper {

  @Override
  public IClassificationDistance getSemanticClassificationDistance(JobConf job) {
    return new ExclusiveSemanticClassificationDistance();
  }

  @Override
  public IClassificationDistance getSpatialClassificationDistance(JobConf job) {
    //
    double northEastLatitude = job.getFloat(TemporalMapper.INITIAL_TIME, 0);
    double northEastLongitude = job.getFloat(TemporalMapper.END_TIME, 0);
    double southWestLatitude = job.getFloat(TemporalMapper.INITIAL_TIME, 0);
    double southWestLongitude = job.getFloat(TemporalMapper.END_TIME, 0);

    return new RelativeSpatialClassificationDistance(northEastLatitude,
      northEastLongitude, southWestLatitude, southWestLongitude);
  }

  @Override
  public IClassificationDistance getTemporalClassificationDistance(JobConf job) {
    long initialTime = job.getLong(TemporalMapper.INITIAL_TIME, 0);
    long endTime = job.getLong(TemporalMapper.END_TIME, 0);
    return new RelativeTemporalClassificationDistance(initialTime, endTime);
  }
}
