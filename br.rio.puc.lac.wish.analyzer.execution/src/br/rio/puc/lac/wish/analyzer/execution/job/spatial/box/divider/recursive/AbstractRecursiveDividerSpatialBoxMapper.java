package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider.recursive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.mapred.JobConf;

import br.rio.puc.lac.wish.analyzer.commons.Region;
import br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.AbstractSpatialBoxMapper;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public abstract class AbstractRecursiveDividerSpatialBoxMapper extends
  AbstractSpatialBoxMapper {

  public static final String REGION_DEPTH = "depth";

  /**
   * 
   * @param region
   * @return
   */
  protected abstract List<Region> getRegions(Region region);

  /**
   * 
   * @param region
   * @param prop
   * @return
   */
  @Override
  public Map<String, Region> divideRegion(JobConf job, Region region) {
    int depth = job.getInt(REGION_DEPTH, 0);
    return divideRegion(region, depth, 0, "0");
  }

  /**
   * 
   * @param region
   * @param depth
   * @param currentDepth
   * @param key
   * @return
   */
  private Map<String, Region> divideRegion(Region region, int depth,
    int currentDepth, String key) {
    //Receives the sub-regions from the main region
    List<Region> subRegions = this.getRegions(region);
    //Map with the results
    Map<String, Region> resultMap = new HashMap<String, Region>();
    //If this depth is the desired depth      
    if (depth == (currentDepth + 1)) {
      int i = 0;
      for (Region subRegion : subRegions) {
        resultMap.put(key + "." + i++, subRegion);
      }
    }
    else {
      int i = 0;
      currentDepth++;
      for (Region subRegion : subRegions) {
        for (Entry<String, Region> result : divideRegion(subRegion, depth,
          currentDepth, key + "." + i++).entrySet()) {
          resultMap.put(result.getKey(), result.getValue());
        }
      }
    }
    //Returns result map
    return resultMap;
  }
}
