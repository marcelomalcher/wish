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
@Table(name = "scheduled_analysis_jobs")
public class ScheduledAnalysisJob {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idScheduledAnalysis")
  private ScheduledAnalysis analysis;

  /** */
  @Column(nullable = false)
  private int ordination;

  /** */
  @Column(nullable = false, length = 500)
  private String jobClass;

  public ScheduledAnalysisJob() {

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

  public int getOrdination() {
    return ordination;
  }

  public void setOrdination(int ordination) {
    this.ordination = ordination;
  }

  public String getJobClass() {
    return jobClass;
  }

  public void setJobClass(String jobClass) {
    this.jobClass = jobClass;
  }
}
