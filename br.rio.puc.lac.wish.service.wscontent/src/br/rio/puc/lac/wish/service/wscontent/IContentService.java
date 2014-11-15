package br.rio.puc.lac.wish.service.wscontent;

import javax.jws.WebService;

import br.rio.puc.lac.wish.analyzer.commons.Content;

@WebService
public interface IContentService {

  /**
   * 
   * @param content
   * @param socialSource
   * @return
   */

  public boolean create(Content<String, String> content, String socialSource);

}
