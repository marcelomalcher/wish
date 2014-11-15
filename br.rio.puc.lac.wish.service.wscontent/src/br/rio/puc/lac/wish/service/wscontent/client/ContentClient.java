package br.rio.puc.lac.wish.service.wscontent.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.service.wscontent.IContentService;

/**
 * @author siva
 * 
 */
public class ContentClient {

  private String url;

  private IContentService port;

  private final static String CONTENT_SERVICE_ADDRESS =
    "http://wscontent.service.wish.lac.puc.rio.br/";

  private final static String CONTENT_SERVICE_NAME = "ContentService";

  public ContentClient(String url) {
    super();
    this.url = url;

    init();
  }

  private void init() {
    URL wsdlUrl;
    try {
      wsdlUrl = new URL(url);
      QName qName = new QName(CONTENT_SERVICE_ADDRESS, CONTENT_SERVICE_NAME);
      Service service = Service.create(wsdlUrl, qName);
      port = service.getPort(IContentService.class);
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public void publishContent(Content<String, String> content,
    String socialSource) {
    port.create(content, socialSource);
  }
}