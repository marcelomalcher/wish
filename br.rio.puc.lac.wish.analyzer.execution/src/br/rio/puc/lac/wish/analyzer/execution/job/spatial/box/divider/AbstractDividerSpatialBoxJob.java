package br.rio.puc.lac.wish.analyzer.execution.job.spatial.box.divider;

import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.execution.job.ContentMinimumNumberReducer;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

public abstract class AbstractDividerSpatialBoxJob implements IJob {

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   */
  public AbstractDividerSpatialBoxJob() {
    logger =
      Logger.getLogger(AbstractDividerSpatialBoxJob.class.getSimpleName());
  }

  @Override
  public String getName() {
    return "AbstractDividerSpatialBoxJob";
  }

  @Override
  public abstract Class<? extends Mapper> getMapper(Properties props,
    JobConf job);

  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {
    int minimumNumber =
      Integer.valueOf(props.getProperty("spatial.minimum-number")); //e.g.: 5;
    job.setInt(ContentMinimumNumberReducer.MINIMUM_VALUE, minimumNumber);
    logger.info("# Spatial Minimum Number: " + minimumNumber);

    int minimumDiffUsers =
      Integer.valueOf(props.getProperty("spatial.minimum-diff-users", "0")); //e.g.: 5;
    job
      .setInt(ContentMinimumNumberReducer.MINIMUM_DIFF_USERS, minimumDiffUsers);
    logger.info("# Spatial Minimum Diff Users: " + minimumDiffUsers);

    return ContentMinimumNumberReducer.class;
  }

  @Override
  public Class<?> getOutputKeyClass() {
    return Classification.class;
  }

  @Override
  public Class<?> getOutputValueClass() {
    return Text.class;
  }

}
