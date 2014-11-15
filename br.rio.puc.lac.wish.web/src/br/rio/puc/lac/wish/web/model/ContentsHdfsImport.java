package br.rio.puc.lac.wish.web.model;

import java.util.Calendar;
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
@Table(name = "contents_hdfs_import")
public class ContentsHdfsImport {

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

  @Column(nullable = false)
  private String name;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date insertDate;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date queryBeginDate;

  /** */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date queryEndDate;

  /** */
  @Column(nullable = false)
  private String hdfsPath;

  /** */
  @Column(nullable = true)
  private String querySocialSource;

  /** */
  @Column(nullable = true)
  private int importStatus;

  /** */
  @Column(nullable = true)
  private String importMessage;

  public ContentsHdfsImport() {
    this.insertDate = Calendar.getInstance().getTime();
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getInsertDate() {
    return insertDate;
  }

  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }

  public Date getQueryBeginDate() {
    return queryBeginDate;
  }

  public void setQueryBeginDate(Date queryBeginDate) {
    this.queryBeginDate = queryBeginDate;
  }

  public Date getQueryEndDate() {
    return queryEndDate;
  }

  public void setQueryEndDate(Date queryEndDate) {
    this.queryEndDate = queryEndDate;
  }

  public String getHdfsPath() {
    return hdfsPath;
  }

  public void setHdfsPath(String hdfsPath) {
    this.hdfsPath = hdfsPath;
  }

  public String getQuerySocialSource() {
    return querySocialSource;
  }

  public void setQuerySocialSource(String querySocialSource) {
    this.querySocialSource = querySocialSource;
  }

  public int getImportStatus() {
    return importStatus;
  }

  public void setImportStatus(int importStatus) {
    this.importStatus = importStatus;
  }

  public String getImportMessage() {
    return importMessage;
  }

  public void setImportMessage(String importMessage) {
    this.importMessage = importMessage;
  }
}
