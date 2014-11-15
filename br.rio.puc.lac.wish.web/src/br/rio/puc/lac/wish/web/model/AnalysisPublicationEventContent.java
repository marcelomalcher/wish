package br.rio.puc.lac.wish.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "analysis_publication_event_content")
public class AnalysisPublicationEventContent {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**  */
  @JoinColumn(name = "idPublicationEvent", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisPublicationEvent analysisPublicationEvent;

  @Column(name = "idContent", nullable = false)
  private int contentId;

  /** */
  @Column(nullable = false)
  private String classificationJson;

  /**
   * 
   */
  public AnalysisPublicationEventContent() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public AnalysisPublicationEvent getAnalysisPublicationEvent() {
    return analysisPublicationEvent;
  }

  public void setAnalysisPublicationEvent(
    AnalysisPublicationEvent analysisPublicationEvent) {
    this.analysisPublicationEvent = analysisPublicationEvent;
  }

  public int getContentId() {
    return contentId;
  }

  public void setContentId(int contentId) {
    this.contentId = contentId;
  }

  public String getClassificationJson() {
    return classificationJson;
  }

  public void setClassificationJson(String classificationJson) {
    this.classificationJson = classificationJson;
  }

}
