package br.rio.puc.lac.wish.analyzer.execution.job.semantic.theme;

import java.util.HashSet;
import java.util.Set;

import br.rio.puc.lac.wish.analyzer.commons.Content;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class Theme {

  private String name;
  private Set<String> keyWords;

  public Theme(String name) {
    this.name = name;
    this.keyWords = new HashSet<String>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String[] keyWords) {
    for (String keyWord : keyWords) {
      this.keyWords.add(keyWord.toLowerCase());
    }
  }

  public void addKeyWord(String keyWord) {
    this.keyWords.add(keyWord.toLowerCase());
  }

  public int countRelation(Content content) {
    int relation = 0;
    for (String kw : keyWords) {
      if (content.getContent().toString().toLowerCase().contains(kw)) {
        relation++;
      }
      for (String tag : content.getTags()) {
        if (tag.toLowerCase().contains(kw)) {
          relation++;
        }
      }
    }
    return relation;
  }

  public static void main(String[] args) {
    Set<String> keyWords = new HashSet<String>();
    keyWords.add("vemprarua");
    String content = "Agora é isso, é agora! #vemprarua";
    int relation = 0;
    for (String kw : keyWords) {
      if (content.toLowerCase().contains(kw.toLowerCase())) {
        relation++;
      }
    }
    System.out.println("> " + relation);
  }
}
