package br.rio.puc.lac.wish.web.results;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import eu.livotov.tpt.TPTApplication;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishResultsApplication extends TPTApplication {

  public static EntityManagerFactory FACTORY;
  static {
    FACTORY = Persistence.createEntityManagerFactory("br.rio.puc.lac.wish.web");
  }

  private static final String CONFIG_BUNDLE_NAME = "res/wish/config.properties";
  private static final String MESSAGES_BUNDLE_NAME =
    "res/wish/messages.properties";

  public Properties configProp;
  public Properties messagesProp;
  {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream configStream =
      classLoader.getResourceAsStream(CONFIG_BUNDLE_NAME);
    InputStream messagesStream =
      classLoader.getResourceAsStream(MESSAGES_BUNDLE_NAME);

    if (configStream == null || messagesStream == null) {
      // File not nound
    }
    else {
      try {
        configProp = new Properties();
        configProp.load(configStream);

        messagesProp = new Properties();
        messagesProp.load(messagesStream);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Result theme
   */
  public static final String THEME = "wish";

  @Override
  public void applicationInit() {
    setMainWindow(new WishMainResultsWindow(this));
  }

  @Override
  public void firstApplicationStartup() {
  }
}