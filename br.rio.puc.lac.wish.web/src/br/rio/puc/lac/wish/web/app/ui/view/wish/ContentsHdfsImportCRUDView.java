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
import br.rio.puc.lac.wish.web.app.ui.field.wish.AnalysisConfigurationSelectorField;
import br.rio.puc.lac.wish.web.model.AnalysisConfiguration;
import br.rio.puc.lac.wish.web.model.ContentsHdfsImport;

import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class ContentsHdfsImportCRUDView extends
  CRUDTableView<ContentsHdfsImport> {

  public ContentsHdfsImportCRUDView(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Control control) {
    super(application, moduleComponent, control, AuroraJPAContainerFactory
      .make(ContentsHdfsImport.class, application.getJPAHandler()
        .getEntityManagerFactory().createEntityManager()));
  }

  @Override
  public Resource getViewIcon() {
    return null;
  }

  @Override
  public String getViewTitle() {
    return application.getApplicationMessages().getString(
      "ContentsHdfsImportCRUDView.view_title");
  }

  @Override
  protected ContentsHdfsImport createNewBean() {
    return new ContentsHdfsImport();
  }

  @Override
  protected String[] getNestedProperties() {
    return new String[] { "configuration.hdfsName" };
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "id", "configuration.hdfsName", "insertDate",
        "queryBeginDate", "queryEndDate", "hdfsPath", "querySocialSource",
        "importStatus", "importMessage" };
  }

  @Override
  protected ICRUDEditorFactory<ContentsHdfsImport> getEditorFactory() {
    return new ICRUDEditorFactory<ContentsHdfsImport>() {

      @Override
      public ICRUDEditor create(Item item, CRUDMode editorMode) {
        FormFieldFactory formFieldFactory = new FormFieldFactory() {

          @Override
          public Field createField(Item item, Object propertyId,
            Component uiContext) {
            if ("configuration".equals(propertyId)) {
              return new AnalysisConfigurationSelectorField(application,
                AuroraJPAContainerFactory.make(AnalysisConfiguration.class,
                  application.getJPAHandler().getEntityManagerFactory()
                    .createEntityManager()));
            }
            Field field =
              DefaultFieldFactory.get()
                .createField(item, propertyId, uiContext);
            if (field instanceof TextField) {
              ((TextField) field).setNullRepresentation(""); //$NON-NLS-1$
            }
            if (field instanceof DateField) {
              ((DateField) field).setResolution(PopupDateField.RESOLUTION_MIN);
            }
            return field;
          }
        };

        Map<Object, String> requiredMap = new HashMap<Object, String>();
        requiredMap
          .put(
            "configuration",
            application
              .getMessage("ContentsHdfsImportCRUDView.configuration_field_required_message"));
        requiredMap
          .put(
            "queryBeginDate",
            application
              .getMessage("ContentsHdfsImportCRUDView.query_begin_date_field_required_message"));
        requiredMap
          .put(
            "queryEndDate",
            application
              .getMessage("ContentsHdfsImportCRUDView.query_end_date_field_required_message"));
        requiredMap
          .put(
            "hdfsPath",
            application
              .getMessage("ContentsHdfsImportCRUDView.hdfs_path_field_required_message"));

        return new CRUDEditorWindow<ContentsHdfsImport>(application, item,
          ContentsHdfsImport.class, application
            .getMessage("ContentsHdfsImportCRUDView.editor_title"), Arrays
            .asList("configuration", "queryBeginDate", "queryEndDate",
              "hdfsPath", "querySocialSource"), formFieldFactory, requiredMap,
          editorMode);
      };
    };
  }

  @Override
  protected String getSavedBeanMessage(ContentsHdfsImport bean) {
    String message = bean.getId() + " - " + bean.getHdfsPath(); //$NON-NLS-1$
    return message;
  }

  @Override
  protected String getDeletedBeanMessage(ContentsHdfsImport bean) {
    String message = bean.getId() + " - " + bean.getHdfsPath(); //$NON-NLS-1$
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
