package br.rio.puc.lac.wish.web.model;

import java.util.ArrayList;
import java.util.Collection;

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

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "analysis_publication_event")
public class AnalysisPublicationEvent {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**  */
  @JoinColumn(name = "idPublication", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisPublication analysisPublication;

  /** */
  @Column(nullable = false)
  private String semantics;

  /** */
  @Column(nullable = false)
  private double latitude;

  /** */
  @Column(nullable = false)
  private double longitude;

  /** */
  @Column(nullable = false)
  private long timestamp;

  /**
   * 
   */
  @OneToMany(mappedBy = "analysisPublicationEvent", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<AnalysisPublicationEventContent> contents;

  /**
   * 
   */
  public AnalysisPublicationEvent() {
    this.contents = new ArrayList<AnalysisPublicationEventContent>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public AnalysisPublication getAnalysisPublication() {
    return analysisPublication;
  }

  public void setAnalysisPublication(AnalysisPublication analysisPublication) {
    this.analysisPublication = analysisPublication;
  }

  public String getSemantics() {
    return semantics;
  }

  public void setSemantics(String semantics) {
    this.semantics = semantics;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public Collection<AnalysisPublicationEventContent> getContents() {
    return contents;
  }

  public void setItems(Collection<AnalysisPublicationEventContent> contents) {
    this.contents.clear();
    for (AnalysisPublicationEventContent content : contents) {
      this.addContent(content);
    }
  }

  public void addContent(AnalysisPublicationEventContent content) {
    this.contents.add(content);
    if (content.getAnalysisPublicationEvent() != this) {
      content.setAnalysisPublicationEvent(this);
    }
  }

}
