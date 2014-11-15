package br.rio.puc.lac.wish.analyzer.execution;

public class Constants {

  //Paths
  public static final String INPUT_PATH = "input-path";
  public static final String INITIAL_OUTPUT_PATH = "output-path";

  //# of Tasks
  public static final String JOB_MAP_TASKS = "job.number.map.tasks";
  public static final String JOB_REDUCE_TASKS = "job.number.reduce.tasks";

  //Configuration
  public static final String HDFS_NAME = "fs.default.name";
  public static final String MAP_JOB_TRACKER = "mapred.job.tracker";

  //Jar file
  public static final String JAR_FILE = "jar.file";

  //Aggregator
  public static final String AGGREGATOR_MAXIMUM_DISTANCE =
    "aggregator.maximum-distance";
  public static final String AGGREGATOR_TEMPORAL_WEIGHT =
    "aggregator.temporal-weight";
  public static final String AGGREGATOR_SPATIAL_WEIGHT =
    "aggregator.spatial-weight";
  public static final String AGGREGATOR_SEMANTIC_WEIGHT =
    "aggregator.semantic-weight";
}
