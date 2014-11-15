package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.model.security.Control;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDEditorWindow;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDTableView;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDUtils.CRUDMode;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditor;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditorFactory;
import br.com.tecnoinf.aurora.ui.view.filter.IAuroraFilterFactory;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.AnalysisConfiguration;

import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;

/**
 * 
 * 
 * 
 * @author Tecgraf
 */
public class AnalysisConfigurationCRUDView extends
  CRUDTableView<AnalysisConfiguration> {

  /**
   * 
   * @param application
   * @param moduleComponent
   * @param control
   */
  public AnalysisConfigurationCRUDView(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Control control) {
    super(application, moduleComponent, control, AuroraJPAContainerFactory
      .make(AnalysisConfiguration.class, application.getJPAHandler()
        .getEntityManagerFactory().createEntityManager()));
  }

  @Override
  public Resource getViewIcon() {
    return null;
  }

  @Override
  public String getViewTitle() {
    return application.getMessage("AnalysisConfigurationCRUDView.view_title");
  }

  @Override
  protected AnalysisConfiguration createNewBean() {
    return new AnalysisConfiguration();
  }

  @Override
  protected String[] getNestedProperties() {
    return null;
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "id", "hdfsName", "mapRedJobTracker", "active" };
  }

  @Override
  protected ICRUDEditorFactory<AnalysisConfiguration> getEditorFactory() {
    return new ICRUDEditorFactory<AnalysisConfiguration>() {

      @Override
      public ICRUDEditor create(Item item, CRUDMode editorMode) {
        FormFieldFactory formFieldFactory = new FormFieldFactory() {

          @Override
          public Field createField(Item item, Object propertyId,
            Component uiContext) {
            Field field =
              DefaultFieldFactory.get()
                .createField(item, propertyId, uiContext);
            if (field instanceof TextField) {
              ((TextField) field).setNullRepresentation("");
            }
            return field;
          }
        };

        Map<Object, String> requiredMap = new HashMap<Object, String>();
        requiredMap
          .put(
            "hdfsName",
            application
              .getMessage("AnalysisConfigurationCRUDView.hdfs_name_field_required_message"));
        requiredMap
          .put(
            "mapRedJobTracker",
            application
              .getMessage("AnalysisConfigurationCRUDView.mapred_jobtracker_field_required_message"));

        return new CRUDEditorWindow<AnalysisConfiguration>(application, item,
          AnalysisConfiguration.class, application.getApplicationMessages()
            .getString("AnalysisConfigurationCRUDView.editor_title"), Arrays
            .asList("hdfsName", "mapRedJobTracker", "active"),
          formFieldFactory, requiredMap, editorMode);
      }
    };
  }

  @Override
  protected String getSavedBeanMessage(AnalysisConfiguration bean) {
    String message = bean.getId() + " - " + bean.getHdfsName(); //$NON-NLS-1$
    return message;
  }

  @Override
  protected String getDeletedBeanMessage(AnalysisConfiguration bean) {
    String message = bean.getId() + " - " + bean.getHdfsName(); //$NON-NLS-1$
    return message;
  }

  @Override
  protected Map<Object, String> getBeanCaptions() {
    return null;
  }

  @Override
  protected IAuroraFilterFactory getFilterFactory() {
    return null;
  }
}
