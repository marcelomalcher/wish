package br.rio.puc.lac.wish.web.app;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.app.services.IModule;
import br.com.tecnoinf.aurora.model.security.Module;
import br.com.tecnoinf.aurora.utils.security.SecurityEntityHelper;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

/**
 * Security Module for Aurora
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishManagerModule implements IModule {

  private AuroraApplication application;

  /**
   * 
   */
  private WishManagerModuleComponent component;

  private Module module;

  @Override
  public Resource getModuleIcon() {
    return new ThemeResource("../aurora/icons/monitor.png");
  }

  @Override
  public String getModuleName() {
    return "W-IS-H Management";
  }

  @Override
  public AbstractModuleComponent getModuleComponent(
    AuroraApplication application) {
    this.application = application;
    if (component == null) {
      component = new WishManagerModuleComponent(application, this);
      component.setSizeFull();
    }
    return component;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Module getModule() {
    if (module == null) {
      module =
        SecurityEntityHelper.getModule(application.getJPAHandler(),
          WishManagerModule.class.getCanonicalName(), getModuleName(),
          application.mustInsertSecurityValues());
    }
    return module;
  }

  @Override
  public String getModuleDisplayName() {
    return application.getApplicationMessages().getString(
      "WishManagerModule.display_name");
  }
}
