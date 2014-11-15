package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.sequential.SequentialDividerSpatialBoxMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.TemporalMapper;

/**
 * 
 * 
 * 
 * @author Tecgraf
 */
public class ExclusiveAggregatorJob extends AbstractAggregatorJob {

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Class<? extends AbstractAggregatorMapper> getMapper(Properties props,
    JobConf job) {
    float maximumDistance =
      Float.valueOf(props.getProperty("aggregator.maximum-distance")); //e.g.: 0.85f;
    float temporalWeight =
      Float.valueOf(props.getProperty("aggregator.temporal-weight")); //e.g.: 0.25f;
    float spatialWeight =
      Float.valueOf(props.getProperty("aggregator.spatial-weight")); //e.g.: 0.50f;
    float semanticWeight =
      Float.valueOf(props.getProperty("aggregator.semantic-weight")); //e.g.: 0.75f;

    //
    job.setFloat(AbstractAggregatorMapper.MAXIMUM_DISTANCE, maximumDistance);

    //
    job.setFloat(AbstractAggregatorMapper.TEMPORAL_WEIGHT, temporalWeight);
    job.setFloat(AbstractAggregatorMapper.SPATIAL_WEIGHT, spatialWeight);
    job.setFloat(AbstractAggregatorMapper.SEMANTIC_WEIGHT, semanticWeight);

    //
    float subRegionWidth =
      Float.valueOf(props.getProperty("spatial.box.width"));
    job.setFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_WIDTH,
      subRegionWidth);
    float subRegionHeight =
      Float.valueOf(props.getProperty("spatial.box.height"));
    job.setFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_HEIGHT,
      subRegionHeight);

    long timeWindow = Long.valueOf(props.getProperty("temporal.time-window"));
    job.setLong(TemporalMapper.TIME_WINDOW, timeWindow);

    return ExclusiveAggregatorMapper.class;
  }
}
