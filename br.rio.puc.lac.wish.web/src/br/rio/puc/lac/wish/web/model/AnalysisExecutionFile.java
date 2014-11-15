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

import br.com.tecnoinf.aurora.model.generic.binary.BinaryContent;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
@Entity
@Table(name = "analysis_execution_file")
public class AnalysisExecutionFile {

  /** 
   * 
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /** */
  @Column(nullable = false, length = 255)
  private String name;

  /** */
  @Column(nullable = true, length = 255)
  private String description;

  /**  */
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date insertDate;

  /**  */
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "idBinaryContent", nullable = false)
  private BinaryContent content;

  public AnalysisExecutionFile() {
    this.insertDate = Calendar.getInstance().getTime();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getInsertDate() {
    return insertDate;
  }

  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }

  public BinaryContent getContent() {
    return content;
  }

  public void setContent(BinaryContent content) {
    this.content = content;
  }
}
