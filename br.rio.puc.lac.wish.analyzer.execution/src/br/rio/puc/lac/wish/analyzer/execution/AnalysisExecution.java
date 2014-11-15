package br.rio.puc.lac.wish.analyzer.execution;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.Tool;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.execution.job.IJob;
import br.rio.puc.lac.wish.analyzer.execution.job.aggregator.AbstractAggregatorMapper;
import br.rio.puc.lac.wish.analyzer.execution.job.aggregator.IAggregatorJob;
import br.rio.puc.lac.wish.analyzer.utils.HadoopUtils;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class AnalysisExecution extends Configured implements Tool {

  /**
   * 
   */
  private List<IJob> jobs;

  /**
   * 
   */
  private IAggregatorJob aggregatorJob;

  /**
   * 
   */
  private Properties props;

  /**
   * 
   */
  private Logger logger;

  /**
   * JobCounter
   */
  private int jobCounter = 0;

  /**
   * Current JobConf
   */
  private JobConf currentJobConf;

  /**
   * CurrentOutputPath
   */
  private Path currentOutputPath;

  /**
   * 
   * @param jobs
   */
  public AnalysisExecution(List<IJob> jobs, IAggregatorJob aggregatorJob,
    Properties props) {
    this.jobs = jobs;
    this.aggregatorJob = aggregatorJob;
    this.props = props;
    //
    //Logger
    DateFormat df = new SimpleDateFormat("yyyyMMdd");
    String s = df.format(Calendar.getInstance().getTime());
    logger =
      Logger.getLogger(AnalysisExecution.class.getSimpleName() + "_" + s);
  }

  /**
   * 
   * @return
   */
  public List<IJob> getJobs() {
    return jobs;
  }

  /**
   * 
   * @return
   */
  public IAggregatorJob getAggregatorJob() {
    return aggregatorJob;
  }

  /**
   * 
   * @return
   */
  public Properties getProps() {
    return props;
  }

  /**
   * 
   * @return
   */
  public int getJobCounter() {
    return jobCounter;
  }

  /**
   * 
   * @return
   */
  public JobConf getCurrentJobConf() {
    return currentJobConf;
  }

  /**
   * 
   * @return
   */
  public Path getCurrentOutputPath() {
    return currentOutputPath;
  }

  /**
   * 
   * @param jobConf
   * @param inputPaths
   * @param outputPath
   * @param jobName
   * @param mapperClass
   * @param reducerClass
   * @param outputKeyClass
   * @param outputValueClass
   * @return
   */
  private RunningJob runJob(JobConf jobConf, Path[] inputPaths,
    Path outputPath, String jobName, Class<? extends Mapper> mapperClass,
    Class<? extends Reducer> reducerClass, Class<?> outputKeyClass,
    Class<?> outputValueClass) {
    //Classification outputs    
    jobConf.setOutputKeyClass(outputKeyClass);
    jobConf.setOutputValueClass(outputValueClass);

    //Setting job name
    jobConf.setJobName(jobName);

    //Setting map and reduce classes
    jobConf.setMapperClass(mapperClass);
    jobConf.setReducerClass(reducerClass);

    //Setting input paths
    FileInputFormat.setInputPaths(jobConf, inputPaths);

    //Defining output paths
    outputPath = new Path(outputPath, jobName);
    FileOutputFormat.setOutputPath(jobConf, outputPath);

    //
    logger.info("#-> JOB : " + jobConf.getJobName());
    logger.info("#-> InputPaths : ");
    for (Path iPath : inputPaths) {
      logger.info("#-> > > " + iPath.toUri().toString());
    }
    logger.info("#-> OutputPath: " + outputPath.toUri().toString());

    //
    RunningJob runningJob = null;
    try {
      runningJob = JobClient.runJob(jobConf);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return runningJob;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int run(String[] args) throws Exception {
    //Input paths
    String inputPathString = props.getProperty(Constants.INPUT_PATH);
    logger.info("# Initial input path: " + inputPathString);
    List<Path> inputPathList = new ArrayList<Path>();
    inputPathList.add(new Path(inputPathString));

    //Configuration Steps?

    //Output
    String outputPath = props.getProperty(Constants.INITIAL_OUTPUT_PATH);
    logger.info("# Setting initial ouput path: " + outputPath);

    //
    RunningJob runningJob = null;

    Path[] inputPath = inputPathList.toArray(new Path[inputPathList.size()]);
    currentOutputPath = new Path(outputPath);

    //Jobs
    while (jobs.size() > 0) {
      IJob job = jobs.remove(0);
      JobConf jobConf = createJobConf();
      runningJob =
        runJob(jobConf, inputPath, currentOutputPath, job.getName(), job
          .getMapper(props, jobConf), job.getReducer(props, jobConf), job
          .getOutputKeyClass(), job.getOutputValueClass());
      //
      jobCounter++;
      //
      currentOutputPath = FileOutputFormat.getOutputPath(jobConf);
      //
      inputPath = new Path[] { currentOutputPath };
    }

    //Aggregator - aggregating results
    int aggregationRound = 0;
    boolean foundSimilarity = true;
    while (foundSimilarity) {
      //
      JobConf jobConf = createJobConf();
      //Defining variables
      logger.info("# > Reading results from: "
        + currentOutputPath.toUri().toString());
      List<String> analysisResults =
        HadoopUtils.readFileAndReturnListOfLines(jobConf, currentOutputPath);
      logger.info("# > Just read the results: " + analysisResults.size());
      //
      jobConf.setInt(AbstractAggregatorMapper.ANALYSIS_RESULT_NUMBER,
        analysisResults.size());
      int i = 0;
      for (String result : analysisResults) {
        jobConf.set(AbstractAggregatorMapper.ANALYSIS_RESULT + i, result);
        i++;
      }
      runningJob =
        runJob(jobConf, new Path[] { currentOutputPath }, currentOutputPath,
          aggregatorJob.getName() + aggregationRound++, aggregatorJob
            .getMapper(props, jobConf), aggregatorJob
            .getReducer(props, jobConf), aggregatorJob.getOutputKeyClass(),
          aggregatorJob.getOutputValueClass());
      //
      jobCounter++;
      currentOutputPath = FileOutputFormat.getOutputPath(jobConf);
      //
      long mergedResults =
        runningJob.getCounters().getCounter(
          AbstractAggregatorMapper.Counter.MERGED_RESULTS);
      //
      logger.info("# > Merged results: " + mergedResults);
      //
      foundSimilarity = mergedResults > 0;
    }

    return jobCounter;
  }

  /**
   * Creates a JobConf object
   * 
   * @return a JobConf object
   */
  private JobConf createJobConf() {
    //Creating job configuration
    JobConf jobConf = new JobConf();
    String fsName = props.getProperty(Constants.HDFS_NAME);
    if (fsName != null && fsName.trim().length() > 0) {
      logger.info("# Setting fs.default.name to: " + fsName);
      jobConf.set("fs.default.name", fsName);
    }
    String jobTracker = props.getProperty(Constants.MAP_JOB_TRACKER);
    if (jobTracker != null && jobTracker.trim().length() > 0) {
      logger.info("# Setting mapred.job.tracker to: " + jobTracker);
      jobConf.set("mapred.job.tracker", jobTracker);
    }

    //Map tasks
    String numberMapTasks = props.getProperty(Constants.JOB_MAP_TASKS);
    if (numberMapTasks != null && numberMapTasks.trim().length() > 0) {
      logger.info("# Setting number of MAP tasks to: " + numberMapTasks);
      jobConf.setNumMapTasks(Integer.valueOf(numberMapTasks));
    }

    //Reduce tasks
    String numberReduceTasks = props.getProperty(Constants.JOB_REDUCE_TASKS);
    if (numberReduceTasks != null && numberReduceTasks.trim().length() > 0) {
      logger.info("# Setting number of REDUCE tasks to: " + numberReduceTasks);
      jobConf.setNumReduceTasks(Integer.valueOf(numberReduceTasks));
    }

    //Jar file
    if (props.getProperty(Constants.JAR_FILE) != null) {
      String jarFile = props.getProperty(Constants.JAR_FILE);
      logger.info("# Setting JAR file: " + jarFile);
      jobConf.setJar(jarFile);
    }

    //

    //Updating current JobConf object 
    this.currentJobConf = jobConf;

    return jobConf;
  }
}
