package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.recursive;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;

import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Region;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.AbstractSpatialBoxMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.AbstractDividerSpatialBoxJob;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

public class NineDividerSpatialBoxJob extends AbstractDividerSpatialBoxJob {

  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {
    double southWestLatitude =
      Long.valueOf(props.getProperty("spatial.southwest-latitude")); //e.g.: 0d;
    double southWestLongitude =
      Long.valueOf(props.getProperty("spatial.southwest-longitude")); //e.g.: 0d;
    double northEastLatitude =
      Long.valueOf(props.getProperty("spatial.northeast-latitude")); //e.g.: 200d;
    double northEastLongitude =
      Long.valueOf(props.getProperty("spatial.northeast-longitude")); //e.g.: 200d;    

    int spatialDepth = Integer.valueOf(props.getProperty("spatial.depth"));

    job.setInt(AbstractRecursiveDividerSpatialBoxMapper.REGION_DEPTH,
      spatialDepth);

    Region region =
      new Region(new Location(southWestLatitude, southWestLongitude),
        new Location(northEastLatitude, northEastLongitude));
    job.set(AbstractSpatialBoxMapper.MAIN_REGION, JSon.toJSONString(
      Region.class, region));

    System.out.println("# SpatialMapper attributes - region: "
      + region.toString() + "; depth: " + spatialDepth);

    return NineDividerSpatialBoxMapper.class;
  }
}
