package br.rio.puc.lac.wish.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "analysis_configuration")
public class AnalysisConfiguration {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /** */
  @Column(nullable = false, length = 500)
  private String hdfsName;

  /** */
  @Column(nullable = false, length = 500)
  private String mapRedJobTracker;

  /** */
  @Column(name = "isActive", nullable = false)
  private Boolean active;

  public AnalysisConfiguration() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHdfsName() {
    return hdfsName;
  }

  public void setHdfsName(String hdfsName) {
    this.hdfsName = hdfsName;
  }

  public String getMapRedJobTracker() {
    return mapRedJobTracker;
  }

  public void setMapRedJobTracker(String mapRedJobTracker) {
    this.mapRedJobTracker = mapRedJobTracker;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
