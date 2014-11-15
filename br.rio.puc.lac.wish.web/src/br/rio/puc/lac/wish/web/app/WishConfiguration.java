package br.rio.puc.lac.wish.web.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.app.services.IConfiguration;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishConfiguration implements IConfiguration {

  /**
   * 
   */
  private AuroraApplication application;

  /**
   * 
   * @param application
   */
  public WishConfiguration(AuroraApplication application) {
    this.application = application;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String getApplicationTheme() {
    return "wish";
  }

  private Embedded theApplicationLogo;
  {
    theApplicationLogo =
      new Embedded(null, new ThemeResource("images/app_logo.png"));
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Embedded getApplicationLogo() {
    return theApplicationLogo;
  }

  private Embedded theApplicationImage;
  {
    theApplicationImage =
      new Embedded(null, new ThemeResource("images/app_image.png"));
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Embedded getLoginImage() {
    return theApplicationImage;
  }

  /**
   * 
   * @return
   */
  @Override
  public String getApplicationName() {
    return application.getApplicationMessages().getString(
      "WishConfiguration.application_name");
  }

  /**
   * 
   * @return
   */
  @Override
  public String getApplicationTitle() {
    return application.getApplicationMessages().getString(
      "WishConfiguration.application_title");
  }

  /**
   * 
   * @return
   */
  @Override
  public String getApplicationSubtitle() {
    return application.getApplicationMessages().getString(
      "WishConfiguration.application_subtitle");
  }

  private static final String BUNDLE_NAME = "res/wish/config.properties";

  private Properties prop;
  {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream stream = classLoader.getResourceAsStream(BUNDLE_NAME);

    if (stream == null) {
      // File not nound
    }
    else {
      try {
        prop = new Properties();
        prop.load(stream);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public String getConfigurationValue(String key) {
    return prop.getProperty(key);
  }

  @Override
  public Properties getConfigurationProperties() {
    return prop;
  }
}
