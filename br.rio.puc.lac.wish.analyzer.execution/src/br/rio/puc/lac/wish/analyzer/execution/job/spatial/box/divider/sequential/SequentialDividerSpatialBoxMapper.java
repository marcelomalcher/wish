package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.sequential;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Region;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.AbstractSpatialBoxMapper;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class SequentialDividerSpatialBoxMapper extends AbstractSpatialBoxMapper {

  public static final String SUB_REGION_WIDTH = "width";
  public static final String SUB_REGION_HEIGHT = "height";
  public static final String HORIZONTAL_MOVE = "horizontal";
  public static final String VERTICAL_MOVE = "vertical";

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Map<String, Region> divideRegion(JobConf job, Region region) {

    //Properties   
    double width = job.getFloat(SUB_REGION_WIDTH, 0f);
    double height = job.getFloat(SUB_REGION_HEIGHT, 0f);
    double horizontalMove = job.getFloat(HORIZONTAL_MOVE, 0f);
    double verticalMove = job.getFloat(VERTICAL_MOVE, 0f);

    // 
    if (width <= 0) {
      width =
        Math.abs(region.getSouthWest().getLatitude()
          - region.getNorthEast().getLatitude());
    }
    if (height <= 0) {
      height =
        Math.abs(region.getSouthWest().getLongitude()
          - region.getNorthEast().getLongitude());
    }
    //
    if (horizontalMove <= 0) {
      horizontalMove = width;
    }
    if (verticalMove <= 0) {
      verticalMove = height;
    }

    //creating map of regions indexed by code (string)
    Map<String, Region> regions = new HashMap<String, Region>();

    //defininf counter
    Integer counter = 0;

    //defining current latitude as minimum specified
    double currentLat = region.getSouthWest().getLatitude();

    //Verifying if current longitude is not greater than maximum allowed
    while ((currentLat) < region.getNorthEast().getLatitude()) {
      //defining current logitude as minimum specified
      double currentLon = region.getSouthWest().getLongitude();
      //Verifying if current longitude is not greater than maximum allowed
      while ((currentLon) < region.getNorthEast().getLongitude()) {
        //creating southwest and northeast locations for subregion
        Location southWest = new Location(currentLat, currentLon);
        Location northEast =
          new Location(currentLat + width, currentLon + height);
        if (currentLat + width > region.getNorthEast().getLatitude()) {
          northEast.setLatitude(currentLat + width
            - ((currentLat + width) - region.getNorthEast().getLatitude()));
        }
        if (currentLon + height > region.getNorthEast().getLongitude()) {
          northEast.setLongitude(currentLon + height
            - ((currentLon + height) - region.getNorthEast().getLongitude()));
        }
        //Creating new SubRegion
        Region subRegion = new Region(southWest, northEast);

        //Adding subRegion to map indexed by counter       
        regions.put(counter.toString(), subRegion);

        //incrementing current longitude 
        currentLon += verticalMove;

        //
        counter++;
      }
      //incrementing current latitude
      currentLat += horizontalMove;
    }
    //Returns result map
    return regions;
  }
}
