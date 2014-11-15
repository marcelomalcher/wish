package br.rio.puc.lac.wish.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "analysis_publication")
public class AnalysisPublication {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  /** */
  @Column(name = "isPublic", nullable = false)
  private Boolean openPublication;

  /** */
  @Column(nullable = false)
  private Boolean receiveVote;

  /**
   * 
   */
  @OneToMany(mappedBy = "analysisPublication", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<AnalysisPublicationEvent> events;

  @OneToOne(mappedBy = "analysisPublication")
  private ScheduledAnalysis scheduledAnalysis;

  @OneToOne(mappedBy = "analysisPublication")
  private AnalysisFeedback analysisFeedback;

  /**
   * 
   */
  public AnalysisPublication() {
    this.events = new ArrayList<AnalysisPublicationEvent>();
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

  public Boolean getOpenPublication() {
    return openPublication;
  }

  public void setOpenPublication(Boolean openPublication) {
    this.openPublication = openPublication;
  }

  public Boolean getReceiveVote() {
    return receiveVote;
  }

  public void setReceiveVote(Boolean receiveVote) {
    this.receiveVote = receiveVote;
  }

  public Collection<AnalysisPublicationEvent> getEvents() {
    return events;
  }

  public void setEvents(Collection<AnalysisPublicationEvent> events) {
    this.events.clear();
    for (AnalysisPublicationEvent event : events) {
      this.addEvent(event);
    }
  }

  public void addEvent(AnalysisPublicationEvent event) {
    this.events.add(event);
    if (event.getAnalysisPublication() != this) {
      event.setAnalysisPublication(this);
    }
  }

  public ScheduledAnalysis getScheduledAnalysis() {
    return scheduledAnalysis;
  }

  public void setScheduledAnalysis(ScheduledAnalysis scheduledAnalysis) {
    scheduledAnalysis = scheduledAnalysis;
  }

  public AnalysisFeedback getAnalysisFeedback() {
    return analysisFeedback;
  }

  public void setAnalysisFeedback(AnalysisFeedback analysisFeedback) {
    this.analysisFeedback = analysisFeedback;
  }
}
