package br.rio.puc.lac.wish.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "analysis_feedback_content")
public class AnalysisFeedbackContent {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**  */
  @JoinColumn(name = "idFeedback", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisFeedback analysisFeedback;

  /** */
  @Column(name = "request_feedback_id", nullable = false)
  private String requestFeedbackId;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  /** */
  @Column(nullable = false)
  private boolean vote;

  /** */
  @Column(nullable = true)
  private String observations;

  /**  */
  @JoinColumn(name = "idPublicationEventContent", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisPublicationEventContent analysisPublicationEventContent;

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
  public AnalysisFeedbackContent() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public AnalysisFeedback getAnalysisFeedback() {
    return analysisFeedback;
  }

  public void setAnalysisFeedback(AnalysisFeedback analysisFeedback) {
    this.analysisFeedback = analysisFeedback;
  }

  public String getRequestFeedbackId() {
    return requestFeedbackId;
  }

  public void setRequestFeedbackId(String requestFeedbackId) {
    this.requestFeedbackId = requestFeedbackId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean isVote() {
    return vote;
  }

  public void setVote(boolean vote) {
    this.vote = vote;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public AnalysisPublicationEventContent getAnalysisPublicationEventContent() {
    return analysisPublicationEventContent;
  }

  public void setAnalysisPublicationEventContent(
    AnalysisPublicationEventContent analysisPublicationEventContent) {
    this.analysisPublicationEventContent = analysisPublicationEventContent;
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
