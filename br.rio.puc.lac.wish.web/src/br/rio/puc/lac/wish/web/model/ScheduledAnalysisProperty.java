package br.rio.puc.lac.wish.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "scheduled_analysis_properties")
public class ScheduledAnalysisProperty {

  /**  
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @JoinColumn(name = "idScheduledAnalysis")
  @ManyToOne(fetch = FetchType.LAZY)
  private ScheduledAnalysis analysis;

  /** */
  @Column(nullable = false, length = 100)
  private String propertyKey;

  /** */
  @Column(nullable = false, length = 100)
  private String propertyValue;

  public ScheduledAnalysisProperty() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ScheduledAnalysis getAnalysis() {
    return analysis;
  }

  public void setAnalysis(ScheduledAnalysis analysis) {
    this.analysis = analysis;
  }

  public String getPropertyKey() {
    return propertyKey;
  }

  public void setPropertyKey(String propertyKey) {
    this.propertyKey = propertyKey;
  }

  public String getPropertyValue() {
    return propertyValue;
  }

  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

}
