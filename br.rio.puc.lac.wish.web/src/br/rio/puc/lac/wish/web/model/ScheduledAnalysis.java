package br.rio.puc.lac.wish.web.model;

import java.util.ArrayList;
import java.util.Calendar;
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
@Table(name = "scheduled_analysis")
public class ScheduledAnalysis {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**  */
  @JoinColumn(name = "idConfiguration", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisConfiguration configuration;

  /**  */
  @JoinColumn(name = "idContentsHDFSImport", nullable = true)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private ContentsHdfsImport contentsImport;

  @Column(nullable = true)
  private String initialInputPath;

  @Column(nullable = false)
  private int numberMapTasks;

  @Column(nullable = false)
  private int numberReduceTasks;

  @Column(nullable = false)
  private String initialOutputPath;

  /**  */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date insertDate;

  /**  */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date scheduledDate;

  /**  */
  @JoinColumn(name = "idFile", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private AnalysisExecutionFile file;

  /**  */
  @Column(nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date execBeginDate;

  /**  */
  @Column(nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date execEndDate;

  @Column(nullable = true)
  private int executionStatus;

  @Column(nullable = true)
  private String executionMessage;

  @Column(nullable = true)
  private String executionLog;

  @Column(nullable = true)
  private String finalOutputPath;

  @Column(nullable = false)
  private String aggregatorJobClass;

  @Column(nullable = false)
  private float maxDistance;

  @Column(nullable = false)
  private float temporalWeight;

  @Column(nullable = false)
  private float spatialWeight;

  @Column(nullable = false)
  private float semanticWeight;

  @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<ScheduledAnalysisJob> jobs;

  @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<ScheduledAnalysisProperty> properties;

  /**  */
  @JoinColumn(name = "idPublication", nullable = true)
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  private AnalysisPublication analysisPublication;

  public ScheduledAnalysis() {
    super();
    this.insertDate = Calendar.getInstance().getTime();
    this.jobs = new ArrayList<ScheduledAnalysisJob>();
    this.properties = new ArrayList<ScheduledAnalysisProperty>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public AnalysisConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(AnalysisConfiguration configuration) {
    this.configuration = configuration;
  }

  public ContentsHdfsImport getContentsImport() {
    return contentsImport;
  }

  public void setContentsImport(ContentsHdfsImport contentsImport) {
    this.contentsImport = contentsImport;
  }

  public String getInitialInputPath() {
    return initialInputPath;
  }

  public void setInitialInputPath(String initialInputPath) {
    this.initialInputPath = initialInputPath;
  }

  public int getNumberMapTasks() {
    return numberMapTasks;
  }

  public void setNumberMapTasks(int numberMapTasks) {
    this.numberMapTasks = numberMapTasks;
  }

  public int getNumberReduceTasks() {
    return numberReduceTasks;
  }

  public void setNumberReduceTasks(int numberReduceTasks) {
    this.numberReduceTasks = numberReduceTasks;
  }

  public String getInitialOutputPath() {
    return initialOutputPath;
  }

  public void setInitialOutputPath(String initialOutputPath) {
    this.initialOutputPath = initialOutputPath;
  }

  public Date getInsertDate() {
    return insertDate;
  }

  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }

  public Date getScheduledDate() {
    return scheduledDate;
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = scheduledDate;
  }

  public AnalysisExecutionFile getFile() {
    return file;
  }

  public void setFile(AnalysisExecutionFile file) {
    this.file = file;
  }

  public Date getExecBeginDate() {
    return execBeginDate;
  }

  public void setExecBeginDate(Date execBeginDate) {
    this.execBeginDate = execBeginDate;
  }

  public Date getExecEndDate() {
    return execEndDate;
  }

  public void setExecEndDate(Date execEndDate) {
    this.execEndDate = execEndDate;
  }

  public int getExecutionStatus() {
    return executionStatus;
  }

  public void setExecutionStatus(int executionStatus) {
    this.executionStatus = executionStatus;
  }

  public String getExecutionMessage() {
    return executionMessage;
  }

  public void setExecutionMessage(String executionMessage) {
    this.executionMessage = executionMessage;
  }

  public String getExecutionLog() {
    return executionLog;
  }

  public void setExecutionLog(String executionLog) {
    this.executionLog = executionLog;
  }

  public String getFinalOutputPath() {
    return finalOutputPath;
  }

  public void setFinalOutputPath(String finalOutputPath) {
    this.finalOutputPath = finalOutputPath;
  }

  public String getAggregatorJobClass() {
    return aggregatorJobClass;
  }

  public void setAggregatorJobClass(String aggregatorJobClass) {
    this.aggregatorJobClass = aggregatorJobClass;
  }

  public float getMaxDistance() {
    return maxDistance;
  }

  public void setMaxDistance(float maxDistance) {
    this.maxDistance = maxDistance;
  }

  public float getTemporalWeight() {
    return temporalWeight;
  }

  public void setTemporalWeight(float temporalWeight) {
    this.temporalWeight = temporalWeight;
  }

  public float getSpatialWeight() {
    return spatialWeight;
  }

  public void setSpatialWeight(float spatialWeight) {
    this.spatialWeight = spatialWeight;
  }

  public float getSemanticWeight() {
    return semanticWeight;
  }

  public void setSemanticWeight(float semanticWeight) {
    this.semanticWeight = semanticWeight;
  }

  public Collection<ScheduledAnalysisJob> getJobs() {
    return jobs;
  }

  public void setJobs(Collection<ScheduledAnalysisJob> jobs) {
    this.jobs.clear();
    for (ScheduledAnalysisJob job : jobs) {
      this.addJob(job);
    }
  }

  public void addJob(ScheduledAnalysisJob job) {
    this.jobs.add(job);
    if (job.getAnalysis() != this) {
      job.setAnalysis(this);
    }
  }

  public Collection<ScheduledAnalysisProperty> getProperties() {
    return properties;
  }

  public void setProperties(Collection<ScheduledAnalysisProperty> properties) {
    this.properties.clear();
    for (ScheduledAnalysisProperty property : properties) {
      this.addProperty(property);
    }
  }

  public void addProperty(ScheduledAnalysisProperty property) {
    this.properties.add(property);
    if (property.getAnalysis() != this) {
      property.setAnalysis(this);
    }
  }

  public AnalysisPublication getAnalysisPublication() {
    return analysisPublication;
  }

  public void setAnalysisPublication(AnalysisPublication analysisPublication) {
    this.analysisPublication = analysisPublication;
  }

  public String getInputPath() {
    String inputPath =
      getContentsImport() != null ? getContentsImport().getHdfsPath()
        : initialInputPath;
    return inputPath;
  }
}
