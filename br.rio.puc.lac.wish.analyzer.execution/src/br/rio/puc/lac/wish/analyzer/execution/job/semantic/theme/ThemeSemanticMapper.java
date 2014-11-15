package br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.execution.AnalysisResult;
import br.rio.puc.lac.wish.analyzer.utils.JSon;

/**
 * 
 * 
 * @author Marcelo Malcher
 */
public class ThemeSemanticMapper implements
  Mapper<Object, Text, Classification, Text> {

  public final static String THEME_NUMBER = "theme.number";
  public final static String THEME_NAME = "theme.name_";
  public final static String THEME_KEYWORDS = "theme.keywords_";

  public final static String THEME_PATTERN_NUMBER =
    "theme.negative-pattern.number";
  public final static String THEME_PATTERN = "theme.negative-pattern_";

  /**
     * 
     */
  private List<Theme> themes;

  /**
   * 
   */
  private List<Pattern> negativePatterns;

  /** */
  private Text contentResult = new Text();

  /**
   * 
   * @param message
   * @return
   */
  private boolean matchesNegativePattern(String message) {
    boolean found = false;
    for (Pattern pattern : negativePatterns) {
      Matcher matcher = pattern.matcher(message);
      if (matcher.find()) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
     */
  @Override
  public void map(Object key, Text value,
    OutputCollector<Classification, Text> output, Reporter reporter)
    throws IOException {
    String contents = value.toString();
    StringTokenizer tokenizer = new StringTokenizer(contents, "\n");
    while (tokenizer.hasMoreTokens()) {
      String contentStr = tokenizer.nextToken();
      try {
        Content content = JSon.getFromJSONString(contentStr, Content.class);
        Classification classification = content.getClassification();
        if (!matchesNegativePattern(content.getContent().toString())) {
          for (Theme theme : themes) {
            int keyWordsCounter = theme.countRelation(content);
            if (keyWordsCounter > 0) {
              Classification newClassification =
                new Classification(classification);
              newClassification.putClassification(AnalysisResult.Type.SEMANTIC
                .name(), theme.getName());
              newClassification.putInformation(AnalysisResult.Type.SEMANTIC
                .name(), String.valueOf(keyWordsCounter));
              content.setClassification(newClassification);
              contentResult.set(JSon.toJSONString(Content.class, content));
              output.collect(newClassification, contentResult);
            }
          }
        }
      }
      catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void configure(JobConf job) {
    this.themes = new ArrayList<Theme>();
    int number = job.getInt(THEME_NUMBER, 0);
    for (int i = 0; i < number; i++) {
      String name = job.get(THEME_NAME + i);
      String[] keyWords = job.getStrings(THEME_KEYWORDS + i);
      Theme theme = new Theme(name);
      theme.setKeyWords(keyWords);
      this.themes.add(theme);
    }
    //Patterns    
    this.negativePatterns = new ArrayList<Pattern>();
    number = job.getInt(THEME_PATTERN_NUMBER, 0);
    for (int i = 0; i < number; i++) {
      Pattern pattern = Pattern.compile(job.get(THEME_PATTERN + i));
      this.negativePatterns.add(pattern);
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }

}
