package br.rio.puc.lac.wish.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "analysis_feedback")
public class AnalysisFeedback {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**  */
  @JoinColumn(name = "idPublication", nullable = true)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  private AnalysisPublication analysisPublication;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  /**
   * 
   */
  @OneToMany(mappedBy = "analysisFeedback", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<AnalysisFeedbackContent> contents;

  /**
   * 
   */
  private int status;

  /**
   * 
   */
  private String message;

  /**
   * 
   */
  public AnalysisFeedback() {
    this.contents = new ArrayList<AnalysisFeedbackContent>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public AnalysisPublication getAnalysisPublication() {
    return analysisPublication;
  }

  public void setAnalysisPublication(AnalysisPublication analysisPublication) {
    this.analysisPublication = analysisPublication;
  }

  public Collection<AnalysisFeedbackContent> getContents() {
    return contents;
  }

  public void setContents(Collection<AnalysisFeedbackContent> contents) {
    this.contents.clear();
    for (AnalysisFeedbackContent content : contents) {
      this.addEvent(content);
    }
  }

  public void addEvent(AnalysisFeedbackContent content) {
    this.contents.add(content);
    if (content.getAnalysisFeedback() != this) {
      content.setAnalysisFeedback(this);
    }
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
