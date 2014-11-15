package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.sequential;

import java.util.Properties;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;

import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Region;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.AbstractSpatialBoxMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.AbstractDividerSpatialBoxJob;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class SequentialDividerSpatialBoxJob extends
  AbstractDividerSpatialBoxJob {

  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {
    double southWestLatitude =
      Float.valueOf(props.getProperty("spatial.southwest-latitude")); //e.g.: 0d;
    double southWestLongitude =
      Float.valueOf(props.getProperty("spatial.southwest-longitude")); //e.g.: 0d;
    double northEastLatitude =
      Float.valueOf(props.getProperty("spatial.northeast-latitude")); //e.g.: 200d;
    double northEastLongitude =
      Float.valueOf(props.getProperty("spatial.northeast-longitude")); //e.g.: 200d;    

    float boxWidth = Float.valueOf(props.getProperty("spatial.box.width")); //e.g.: 0d;
    float boxHeight = Float.valueOf(props.getProperty("spatial.box.height")); //e.g.: 0d;
    float horizontalMove =
      Float.valueOf(props.getProperty("spatial.horizontal-move")); //e.g.: 200d;
    float verticalMove =
      Float.valueOf(props.getProperty("spatial.vertical-move")); //e.g.: 200d;    

    job.setFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_WIDTH, boxWidth);
    job
      .setFloat(SequentialDividerSpatialBoxMapper.SUB_REGION_HEIGHT, boxHeight);
    job.setFloat(SequentialDividerSpatialBoxMapper.HORIZONTAL_MOVE,
      horizontalMove);
    job.setFloat(SequentialDividerSpatialBoxMapper.VERTICAL_MOVE, verticalMove);

    Region region =
      new Region(new Location(southWestLatitude, southWestLongitude),
        new Location(northEastLatitude, northEastLongitude));
    job.set(AbstractSpatialBoxMapper.MAIN_REGION, JSon.toJSONString(
      Region.class, region));

    System.out.println("# Spatial attributes - region: " + region.toString()
      + "; width: " + boxWidth + "; height: " + boxHeight
      + "; horizontal-move: " + horizontalMove + "; vertical-move: "
      + verticalMove);

    return SequentialDividerSpatialBoxMapper.class;
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    String strNumber = "-23.10f";
    double number = Float.valueOf(strNumber);
    System.out.println("Number is " + number);
  }
}
