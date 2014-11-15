package br.rio.puc.lac.wish.web.app.ui.view.wish;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.view.AuroraView;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.VerticalLayout;

public class WishDashboard extends AuroraView {

  private VerticalLayout dashboardLayout;

  /**
   * 
   */
  protected JPAContainer<br.com.tecnoinf.aurora.model.eveng.Event> container;

  public WishDashboard(AuroraApplication application,
    AbstractModuleComponent moduleComponent) {
    super(application, moduleComponent);
    build();
  }

  private void build() {
    dashboardLayout = new VerticalLayout();
    dashboardLayout.setSizeFull();
    dashboardLayout.setSpacing(true);
    //
    addComponent(dashboardLayout);
  }

  private Resource icon;

  @Override
  public Resource getViewIcon() {
    if (icon == null) {
      icon = new ThemeResource("../aurora/icons/control_equalizer.png");
    }
    return icon;
  }

  @Override
  protected boolean showDashboardButton() {
    return false;
  }

  @Override
  public String getViewTitle() {
    return application.getApplicationMessages().getString(
      "WishDashboard.view_title");
  }

  @Override
  public void refresh() {
    if (dashboardLayout != null) {
      removeComponent(dashboardLayout);
    }
    build();
  }
}