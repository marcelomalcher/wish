package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.model.security.Control;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDTableView;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDUtils.CRUDMode;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditor;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditorFactory;
import br.com.tecnoinf.aurora.ui.view.filter.IAuroraFilterFactory;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysis;

import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

public class ScheduledAnalysisCRUDView extends CRUDTableView<ScheduledAnalysis> {

  public ScheduledAnalysisCRUDView(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Control control) {
    super(application, moduleComponent, control, AuroraJPAContainerFactory
      .make(ScheduledAnalysis.class, application.getJPAHandler()
        .getEntityManagerFactory().createEntityManager()));
  }

  @Override
  public Resource getViewIcon() {
    return new ThemeResource("../aurora/icons/calendar.png");
  }

  @Override
  public String getViewTitle() {
    return application.getMessage("ScheduledAnalysisCRUDView.view_title");
  }

  @Override
  protected ScheduledAnalysis createNewBean() {
    return new ScheduledAnalysis();
  }

  @Override
  protected String[] getNestedProperties() {
    return null;
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "id", "insertDate", "scheduledDate",
        "initialOutputPath", "executionStatus", "executionMessage" }; //$NON-NLS-1$
  }

  @Override
  protected Map<Object, String> getBeanCaptions() {
    return null;
  }

  @Override
  protected ICRUDEditorFactory<ScheduledAnalysis> getEditorFactory() {
    return new ICRUDEditorFactory<ScheduledAnalysis>() {

      @Override
      public ICRUDEditor create(Item item, CRUDMode editorMode) {
        return new ScheduledAnalysisCRUDEditor(application, moduleComponent,
          item, editorMode);
      };
    };
  }

  @Override
  protected String getSavedBeanMessage(ScheduledAnalysis bean) {
    String message = bean.getId() + " - " + bean.getInputPath();
    return message;
  }

  @Override
  protected String getDeletedBeanMessage(ScheduledAnalysis bean) {
    String message = bean.getId() + " - " + bean.getInputPath();
    return message;
  }

  @Override
  protected IAuroraFilterFactory getFilterFactory() {
    return null;
  }
}
