package br.rio.puc.lac.wish.analyzer.execution.job.aggregator;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.AbstractSpatialBoxMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.temporal.TemporalMapper;

public class RelativeAggregatorJob extends AbstractAggregatorJob {

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

    job.setFloat(AbstractAggregatorMapper.MAXIMUM_DISTANCE, maximumDistance);

    job.setFloat(AbstractAggregatorMapper.TEMPORAL_WEIGHT, temporalWeight);
    job.setFloat(AbstractAggregatorMapper.SPATIAL_WEIGHT, spatialWeight);
    job.setFloat(AbstractAggregatorMapper.SEMANTIC_WEIGHT, semanticWeight);

    //Spatial
    float northEastLatitude =
      Float.valueOf(props.getProperty("spatial.northeast-latitude"));
    job
      .setFloat(AbstractSpatialBoxMapper.NORTHEAST_LATITUDE, northEastLatitude);
    float northEastLongitude =
      Float.valueOf(props.getProperty("spatial.northeast-longitude"));
    job.setFloat(AbstractSpatialBoxMapper.NORTHEAST_LONGITUDE,
      northEastLongitude);
    float southWestLatitude =
      Float.valueOf(props.getProperty("spatial.southwest-latitude"));
    job
      .setFloat(AbstractSpatialBoxMapper.SOUTHWEST_LATITUDE, southWestLatitude);
    float southWestLongitude =
      Float.valueOf(props.getProperty("spatial.southwest-longitude"));
    job.setFloat(AbstractSpatialBoxMapper.SOUTHWEST_LONGITUDE,
      southWestLongitude);

    //Temporal
    long initialTime = Long.valueOf(props.getProperty("temporal.initial-time"));
    job.setLong(TemporalMapper.INITIAL_TIME, initialTime);
    long endTime = Long.valueOf(props.getProperty("temporal.end-time"));

    return RelativeAggregatorMapper.class;
  }
}
