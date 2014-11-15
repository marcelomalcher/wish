package br.rio.puc.lac.wish.web.app.ui.field.wish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.field.TableCreateMultiBeanWindowField;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDEditorWindow;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDUtils.CRUDMode;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditor;
import br.com.tecnoinf.aurora.ui.view.crud.ICRUDEditorFactory;
import br.com.tecnoinf.aurora.ui.widget.numberfield.TextualNumberField;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysisJob;

import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class ScheduledAnalysisJobCreateMultiField extends
  TableCreateMultiBeanWindowField<ScheduledAnalysisJob, ScheduledAnalysisJob> {

  private TableFieldFactory tableFieldFactory;

  public ScheduledAnalysisJobCreateMultiField(AuroraApplication application,
    AbstractModuleComponent moduleComponent) {
    super(application, moduleComponent);
  }

  @Override
  protected Class<ScheduledAnalysisJob> getEntityClass() {
    return ScheduledAnalysisJob.class;
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "ordination", "jobClass" };
  }

  @Override
  protected TableFieldFactory getTableFieldFactory() {
    return null;
  }

  @Override
  protected Collection<ScheduledAnalysisJob> createBeanCollection() {
    return new ArrayList<ScheduledAnalysisJob>();
  }

  @Override
  public Class<?> getType() {
    return ArrayList.class;
  }

  @Override
  protected BeanIdResolver<ScheduledAnalysisJob, ScheduledAnalysisJob> getBeanIdResolver() {
    return new BeanIdResolver<ScheduledAnalysisJob, ScheduledAnalysisJob>() {

      @Override
      public ScheduledAnalysisJob getIdForBean(ScheduledAnalysisJob bean) {
        return bean;
      }
    };
  }

  @Override
  protected ScheduledAnalysisJob createNewBean() {
    return new ScheduledAnalysisJob();
  }

  @Override
  protected ICRUDEditorFactory<ScheduledAnalysisJob> getEditorFactory() {
    return new ICRUDEditorFactory<ScheduledAnalysisJob>() {

      @Override
      public ICRUDEditor create(Item item, CRUDMode editorMode) {
        FormFieldFactory formFieldFactory = new FormFieldFactory() {

          @Override
          public Field createField(Item item, Object propertyId,
            Component uiContext) {

            if ("ordination".equals(propertyId)) {
              TextualNumberField<Integer> nField =
                new TextualNumberField<Integer>(Integer.class);
              nField
                .setCaption(application
                  .getMessage("ScheduledAnalysisJobCreateMultiField.cfield_caption_order"));
              nField.setAllowNegative(false);
              nField.setAllowNull(false);
              nField.setWidth("5em");
              return nField;
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
            "ordination",
            application
              .getMessage("ScheduledAnalysisJobCreateMultiField.field_required_order"));
        requiredMap
          .put(
            "jobClass",
            application
              .getMessage("ScheduledAnalysisJobCreateMultiField.field_required_jobClass"));

        return new CRUDEditorWindow<ScheduledAnalysisJob>(application, item,
          ScheduledAnalysisJob.class, application
            .getMessage("ScheduledAnalysisJobCreateMultiField.editor_title"),
          Arrays.asList("ordination", "jobClass"), formFieldFactory,
          requiredMap, editorMode);
      };
    };
  }

  @Override
  protected String[] getNestedProperties() {
    return null;
  }
}