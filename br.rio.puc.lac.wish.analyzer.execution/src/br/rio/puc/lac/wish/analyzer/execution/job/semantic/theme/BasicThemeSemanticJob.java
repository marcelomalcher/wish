package br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme;

import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.log4j.Logger;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.execution.job.ContentMinimumNumberReducer;
import br.rio.puc.lac.wish.analyzer.execution.job.IJob;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class BasicThemeSemanticJob implements IJob {

  /**
   * The logger of this class
   */
  private static Logger logger;

  /**
   * 
   */
  public BasicThemeSemanticJob() {
    logger = Logger.getLogger(BasicThemeSemanticJob.class.getSimpleName());
  }

  @Override
  public String getName() {
    return "SemanticThemeBasic";
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Mapper> getMapper(Properties props, JobConf job) {
    int number = Integer.valueOf(props.getProperty("theme.number")); //e.g.: 3;
    job.setInt(ThemeSemanticMapper.THEME_NUMBER, number);
    logger.info("# Number of themes: " + number);
    for (int i = 0; i < number; i++) {
      String themeName = props.getProperty("theme.name." + i);
      String themeKeyWords = props.getProperty("theme.keywords." + i);
      logger.info("# Semantic Theme: " + themeName + " - " + themeKeyWords);
      job.set(ThemeSemanticMapper.THEME_NAME + i, themeName);
      job.setStrings(ThemeSemanticMapper.THEME_KEYWORDS + i, themeKeyWords);
    }
    //Semantic Negative Patterns
    number =
      Integer.valueOf(props.getProperty("theme.negative-pattern.number", "0")); //e.g.: 3;
    logger.info("# Number of negative patterns: " + number);
    job.setInt(ThemeSemanticMapper.THEME_PATTERN_NUMBER, number);
    for (int i = 0; i < number; i++) {
      String pattern = props.getProperty("theme.negative-pattern." + i);
      logger.info("# Semantic Negative Pattern: " + pattern);
      job.set(ThemeSemanticMapper.THEME_PATTERN + i, pattern);
    }
    //
    return ThemeSemanticMapper.class;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Reducer> getReducer(Properties props, JobConf job) {

    int minimumNumber =
      Integer.valueOf(props.getProperty("semantic.minimum-number", "1")); //e.g.: 5;
    job.setInt(ContentMinimumNumberReducer.MINIMUM_VALUE, minimumNumber);
    logger.info("# Semantic Minimum Number: " + minimumNumber);

    int minimumDiffUsers =
      Integer.valueOf(props.getProperty("semantic.minimum-diff-users", "0")); //e.g.: 5;
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
