package br.rio.puc.lac.wish.analyzer.execution.job.temporal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.execution.job.ContentMinimumNumberReducer;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

public class TemporalJob implements IJob {

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   */
  public TemporalJob() {
    logger = Logger.getLogger(TemporalJob.class.getSimpleName());
  }

  @Override
  public String getName() {
    return "Temporal";
  }

  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {

    long initialTime = Long.valueOf(props.getProperty("temporal.initial-time")); //e.g.: 1290030078337l;
    if (props.containsKey("temporal.initial-date")) {
      logger.info("# getting initial date");
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      try {
        Date date =
          dateFormat.parse(props.getProperty("temporal.initial-date"));
        initialTime = date.getTime();
        logger.info("# initial time from date: " + initialTime);
      }
      catch (ParseException e) {
        e.printStackTrace();
      }
    }
    long endTime = Long.valueOf(props.getProperty("temporal.end-time")); //e.g.: 1290032874992l;
    if (props.containsKey("temporal.end-date")) {
      logger.info("# getting end date");
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      try {
        Date date = dateFormat.parse(props.getProperty("temporal.end-date"));
        endTime = date.getTime();
        logger.info("# end time from date: " + endTime);
      }
      catch (ParseException e) {
        e.printStackTrace();
      }
    }
    long timeWindow = Long.valueOf(props.getProperty("temporal.time-window")); //e.g.: 300000;
    long windowMove = Long.valueOf(props.getProperty("temporal.window-move")); //e.g.: 60000;

    job.setLong(TemporalMapper.INITIAL_TIME, initialTime);
    job.setLong(TemporalMapper.END_TIME, endTime);
    job.setLong(TemporalMapper.TIME_WINDOW, timeWindow);
    job.setLong(TemporalMapper.WINDOW_MOVE, windowMove);

    System.out.println("# TemporalMapper attributes - initial: " + initialTime
      + "; end: " + endTime + "; window: " + timeWindow + "; move: "
      + windowMove);

    return TemporalMapper.class;
  }

  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {
    int minimumNumber =
      Integer.valueOf(props.getProperty("temporal.minimum-number")); //e.g.: 5;
    job.setInt(ContentMinimumNumberReducer.MINIMUM_VALUE, minimumNumber);
    logger.info("# TemporalReducer Minimum Number: " + minimumNumber);

    int minimumDiffUsers =
      Integer.valueOf(props.getProperty("temporal.minimum-diff-users", "0")); //e.g.: 5;
    job
      .setInt(ContentMinimumNumberReducer.MINIMUM_DIFF_USERS, minimumDiffUsers);
    logger.info("# Semantic Minimum Diff Users: " + minimumDiffUsers);

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
