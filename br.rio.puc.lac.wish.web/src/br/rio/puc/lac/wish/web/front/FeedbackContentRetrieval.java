package br.rio.puc.lac.wish.web.front;

import java.util.List;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.web.model.AnalysisFeedbackContent;

public class FeedbackContentRetrieval {

  private AnalysisFeedbackContent feedbackContent;

  private Content theContent;

  private List<Content<String, String>> others;

  public FeedbackContentRetrieval(AnalysisFeedbackContent feedbackContent,
    Content theContent, List<Content<String, String>> others) {
    super();
    this.feedbackContent = feedbackContent;
    this.theContent = theContent;
    this.others = others;
  }

  public AnalysisFeedbackContent getFeedbackContent() {
    return feedbackContent;
  }

  public void setFeedbackContent(AnalysisFeedbackContent feedbackContent) {
    this.feedbackContent = feedbackContent;
  }

  public Content getTheContent() {
    return theContent;
  }

  public void setTheContent(Content theContent) {
    this.theContent = theContent;
  }

  public List<Content<String, String>> getOthers() {
    return others;
  }

  public void setOthers(List<Content<String, String>> others) {
    this.others = others;
  }

}
