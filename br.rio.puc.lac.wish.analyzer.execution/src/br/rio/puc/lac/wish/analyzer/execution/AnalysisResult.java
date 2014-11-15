package br.rio.puc.lac.wish.analyzer.execution;

import java.util.HashSet;
import java.util.Set;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class AnalysisResult implements Comparable<AnalysisResult> {

  public enum Type {
    SEMANTIC,
    SPATIAL,
    TEMPORAL;
  }

  /**
	 * 
	 */
  private String id;

  /**
	 * 
	 */
  private Set<String> semantics;

  /**
	 * 
	 */
  private Location spatial;

  /**
	 * 
	 */
  private long temporal;

  /**
   * 
   */
  Set<Content> contents;

  /**
	 * 
	 */
  private AnalysisResult() {
    this.semantics = new HashSet<String>();
    this.contents = new HashSet<Content>();
  }

  /**
   * 
   */
  public AnalysisResult(String id) {
    this();
    this.id = id;
  }

  /**
   * 
   */
  public AnalysisResult(Content content) {
    this(content.getId());
    this.addContent(content);
  }

  /**
   * 
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * 
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }

  public Set<String> getSemantics() {
    return semantics;
  }

  public void setSemantics(Set<String> semantic) {
    this.semantics = semantic;
  }

  public void addSemantic(String semantic) {
    this.semantics.add(semantic);
  }

  public Location getSpatial() {
    return spatial;
  }

  public void setSpatial(Location spatial) {
    this.spatial = spatial;
  }

  public long getTemporal() {
    return temporal;
  }

  public void setTemporal(long temporal) {
    this.temporal = temporal;
  }

  public Set<Content> getContents() {
    return contents;
  }

  public void setContents(Set<Content> contents) {
    this.contents = contents;
  }

  public void addContent(Content content) {
    this.contents.add(content);
    //
    if (content.getId().compareTo(this.id) < 0) {
      this.id = content.getId();
    }
  }

  public void clearContents() {
    this.contents.clear();
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public int compareTo(AnalysisResult that) {
    return this.id.compareTo(that.id);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "AnalysisResult [id=" + id + ", semantics=" + semantics
      + ", spatial=" + spatial + ", temporal=" + temporal + ", contents="
      + contents + "]";
  }

  public static void main(String[] args) {
  }
}
