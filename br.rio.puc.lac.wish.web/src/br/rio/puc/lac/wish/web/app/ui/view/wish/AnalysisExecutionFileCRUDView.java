package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.model.security.Control;
import br.com.tecnoinf.aurora.ui.field.BinaryContentField;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDEditorWindow;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDTableView;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDUtils.CRUDMode;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditor;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditorFactory;
import br.com.tecnoinf.aurora.ui.view.filter.IAuroraFilterFactory;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.AnalysisExecutionFile;

import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;

public class AnalysisExecutionFileCRUDView extends
  CRUDTableView<AnalysisExecutionFile> {

  public AnalysisExecutionFileCRUDView(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Control control) {
    super(application, moduleComponent, control, AuroraJPAContainerFactory
      .make(AnalysisExecutionFile.class, application.getJPAHandler()
        .getEntityManagerFactory().createEntityManager()));
  }

  @Override
  public Resource getViewIcon() {
    return null;
  }

  @Override
  public String getViewTitle() {
    return application.getApplicationMessages().getString(
      "AnalysisExecutionFileCRUDView.view_title");
  }

  @Override
  protected AnalysisExecutionFile createNewBean() {
    return new AnalysisExecutionFile();
  }

  @Override
  protected String[] getNestedProperties() {
    return new String[] { "content.fileName", "content.mimeType" };
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "id", "name", "description", "insertDate",
        "content.fileName", "content.mimeType" };
  }

  @Override
  protected ICRUDEditorFactory<AnalysisExecutionFile> getEditorFactory() {
    return new ICRUDEditorFactory<AnalysisExecutionFile>() {

      @Override
      public ICRUDEditor create(Item item, CRUDMode editorMode) {
        FormFieldFactory formFieldFactory = new FormFieldFactory() {

          @Override
          public Field createField(Item item, Object propertyId,
            Component uiContext) {
            if ("content".equals(propertyId)) {
              //
              BinaryContentField field =
                new BinaryContentField(application, moduleComponent,
                  application.getAuroraMessages().getString(
                    "BinaryContentField.cfield_caption_content"));

              //The file type can be validated if needed...perhaps only the extension is already ok (.jar)

              return field;
            }
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
            "name",
            application
              .getMessage("AnalysisExecutionFileCRUDView.name_field_required_message"));
        requiredMap.put("content", application
          .getMessage("BinaryContentField.content_field_required_message"));

        return new CRUDEditorWindow<AnalysisExecutionFile>(application, item,
          AnalysisExecutionFile.class, application
            .getMessage("AnalysisExecutionFileCRUDView.editor_title"), Arrays
            .asList("name", "description", "content"), formFieldFactory,
          requiredMap, editorMode);
      };
    };
  }

  @Override
  protected String getSavedBeanMessage(AnalysisExecutionFile bean) {
    String message = bean.getId() + " - " + bean.getContent().getFileName();
    return message;
  }

  @Override
  protected String getDeletedBeanMessage(AnalysisExecutionFile bean) {
    String message = bean.getId() + " - " + bean.getContent().getFileName();
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
