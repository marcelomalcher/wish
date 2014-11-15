package br.rio.puc.lac.wish.web.app;

import java.util.ArrayList;
import java.util.List;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.app.services.IConfiguration;
import br.com.tecnoinf.aurora.app.services.IJPAHandler;
import br.com.tecnoinf.aurora.app.services.IMessages;
import br.com.tecnoinf.aurora.app.services.IModule;
import br.com.tecnoinf.aurora.app.services.messages.Messages;
import br.com.tecnoinf.aurora.ui.menu.DefaultAboutApplicationMenuAction;
import br.com.tecnoinf.aurora.ui.menu.IApplicationMenuAction;
import br.rio.puc.lac.wish.web.app.services.jpahandler.JPAHandler;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishManagerApplication extends AuroraApplication {

  /**
   * 
   */
  protected IConfiguration configuration;

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public IConfiguration getApplicationConfiguration() {
    if (configuration == null) {
      configuration = new WishConfiguration(this);
    }
    return configuration;
  }

  private static final String BUNDLE_NAME = "res/wish/messages.properties";

  /**
   * 
   */
  protected IMessages messages;

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public IMessages getApplicationMessages() {
    if (messages == null) {
      messages = new Messages(BUNDLE_NAME);
    }
    return messages;
  }

  private IJPAHandler jpaHandler;

  @Override
  public IJPAHandler getJPAHandler() {
    if (jpaHandler == null) {
      jpaHandler = new JPAHandler();
    }
    return jpaHandler;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  protected List<IModule> getModules() {
    List<IModule> modules = new ArrayList<IModule>();
    modules.add(new WishManagerModule());
    return modules;
  }

  /**
   * 
   */
  @Override
  protected List<IApplicationMenuAction> getMenuActions() {
    return null;
  }

  private IApplicationMenuAction aboutMenuAction;

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  protected IApplicationMenuAction getAboutMenuAction() {
    if (aboutMenuAction == null) {
      aboutMenuAction =
        new DefaultAboutApplicationMenuAction(
          this,
          getApplicationConfiguration().getApplicationName(),
          getApplicationMessages()
            .getString(
              "WishManagerApplication.DefaultAboutApplicationMenuAction.about_window_content_message"),
          getAuroraMessages().getString(
            "DefaultAboutApplicationMenuAction.menu_text"),
          getAuroraMessages().getString(
            "DefaultAboutApplicationMenuAction.about_window_caption"),
          getAuroraMessages().getString(
            "DefaultAboutApplicationMenuAction.about_window_close_caption"));
    }
    return aboutMenuAction;
  }
}
