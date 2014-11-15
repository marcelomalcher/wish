package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.recursive;

import java.util.ArrayList;
import java.util.List;

import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Region;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class NineDividerSpatialBoxMapper extends
  AbstractRecursiveDividerSpatialBoxMapper {

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  protected List<Region> getRegions(Region region) {
    List<Region> regionList = new ArrayList<Region>();

    //
    double boundingBoxSouthWestLatitude = region.getSouthWest().getLatitude();
    double boundingBoxSouthWestLongitude = region.getSouthWest().getLongitude();
    double boundingBoxNorthEastLatitude = region.getNorthEast().getLatitude();
    double boundingBoxNorthEastLongitude = region.getNorthEast().getLongitude();

    //
    double diffLatitude =
      (boundingBoxNorthEastLatitude - boundingBoxSouthWestLatitude) / 2;
    double diffLongitude =
      (boundingBoxNorthEastLongitude - boundingBoxSouthWestLongitude) / 2;

    //Sub Regions
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude,
      boundingBoxSouthWestLongitude + diffLongitude), new Location(
      boundingBoxNorthEastLatitude - diffLatitude,
      boundingBoxNorthEastLongitude)));
    regionList
      .add(new Region(new Location(boundingBoxSouthWestLatitude + diffLatitude,
        boundingBoxSouthWestLongitude + diffLongitude), new Location(
        boundingBoxNorthEastLatitude, boundingBoxNorthEastLongitude)));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude
      + diffLatitude, boundingBoxSouthWestLongitude), new Location(
      boundingBoxNorthEastLatitude, boundingBoxNorthEastLongitude
        - diffLongitude)));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude,
      boundingBoxSouthWestLongitude), new Location(boundingBoxNorthEastLatitude
      - diffLatitude, boundingBoxNorthEastLongitude - diffLongitude)));
    regionList.add(new Region(
      new Location(boundingBoxSouthWestLatitude + (diffLatitude / 2),
        boundingBoxSouthWestLongitude + (diffLongitude / 2)), new Location(
        boundingBoxNorthEastLatitude - (diffLatitude / 2),
        boundingBoxNorthEastLongitude - (diffLongitude / 2))));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude,
      boundingBoxSouthWestLongitude + (diffLongitude / 2)), new Location(
      boundingBoxNorthEastLatitude - (diffLatitude),
      boundingBoxNorthEastLongitude - (diffLongitude / 2))));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude
      + (diffLatitude / 2), boundingBoxSouthWestLongitude + (diffLongitude)),
      new Location(boundingBoxNorthEastLatitude - (diffLatitude / 2),
        boundingBoxNorthEastLongitude)));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude
      + (diffLatitude), boundingBoxSouthWestLongitude + (diffLongitude / 2)),
      new Location(boundingBoxNorthEastLatitude, boundingBoxNorthEastLongitude
        - (diffLongitude / 2))));
    regionList.add(new Region(new Location(boundingBoxSouthWestLatitude
      + (diffLatitude / 2), boundingBoxSouthWestLongitude), new Location(
      boundingBoxNorthEastLatitude - (diffLatitude / 2),
      boundingBoxNorthEastLongitude - (diffLongitude))));
    //
    return regionList;
  }
}
