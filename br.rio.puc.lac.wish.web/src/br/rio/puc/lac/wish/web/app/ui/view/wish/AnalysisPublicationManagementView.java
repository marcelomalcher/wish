package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.model.security.Control;
import br.com.tecnoinf.aurora.ui.view.AuroraTableView;
import br.com.tecnoinf.aurora.ui.view.filter.IAuroraFilterFactory;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.AnalysisPublication;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

public class AnalysisPublicationManagementView extends
  AuroraTableView<AnalysisPublication> {

  /**
   * 
   */
  private Button manageButton;

  public AnalysisPublicationManagementView(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Control control) {
    super(application, moduleComponent, control, AuroraJPAContainerFactory
      .make(AnalysisPublication.class, application.getJPAHandler()
        .getEntityManagerFactory().createEntityManager()));
  }

  /**
   * 
   */
  @Override
  protected void build() {
    super.build();

    //Bottom Toolbar
    HorizontalLayout bottomToolbar = new HorizontalLayout();
    manageButton =
      new Button(application
        .getMessage("AnalysisPublicationManagementView.manage_button_caption"));
    manageButton.setStyleName(Reindeer.BUTTON_SMALL);
    manageButton.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        final EntityItem<AnalysisPublication> item =
          container.getItem(table.getValue());
        if (item != null) {
          AnalysisPublication analysisPublication = item.getEntity();

          AnalysisPublicationManagerView managerView =
            new AnalysisPublicationManagerView(application, moduleComponent,
              analysisPublication, AnalysisPublicationManagementView.this);
          moduleComponent.setAuroraView(managerView);
        }
      }
    });
    manageButton.setEnabled(false);

    bottomToolbar.addComponent(manageButton);
    bottomToolbar.setSpacing(true);
    bottomToolbar.setSizeUndefined();

    addComponent(bottomToolbar);
  }

  @Override
  public Resource getViewIcon() {
    return new ThemeResource("../aurora/icons/map.png");
  }

  @Override
  public String getViewTitle() {
    return application
      .getMessage("AnalysisPublicationManagementView.view_title");
  }

  @Override
  protected String[] getNestedProperties() {
    return new String[] { "scheduledAnalysis.contentsImport.hdfsPath" };
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "id", "scheduledAnalysis.contentsImport.hdfsPath",
        "date", "openPublication", "receiveVote" };
  }

  @Override
  protected Map<Object, String> getBeanCaptions() {
    return null;
  }

  @Override
  protected IAuroraFilterFactory getFilterFactory() {
    return null;
  }

  @Override
  protected void onTableDoubleClick(ItemClickEvent arg0) {
    manageButton.click();
  }

  @Override
  protected void setModificationsEnabled(boolean b) {
    manageButton.setEnabled(b);
  }
}
