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
import br.rio.puc.lac.wish.web.model.ScheduledAnalysisProperty;

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
public class ScheduledAnalysisPropertyCreateMultiField
  extends
  TableCreateMultiBeanWindowField<ScheduledAnalysisProperty, ScheduledAnalysisProperty> {

  private TableFieldFactory tableFieldFactory;

  public ScheduledAnalysisPropertyCreateMultiField(
    AuroraApplication application, AbstractModuleComponent moduleComponent) {
    super(application, moduleComponent);
  }

  @Override
  protected Class<ScheduledAnalysisProperty> getEntityClass() {
    return ScheduledAnalysisProperty.class;
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "propertyKey", "propertyValue" };
  }

  @Override
  protected TableFieldFactory getTableFieldFactory() {
    return null;
  }

  @Override
  protected Collection<ScheduledAnalysisProperty> createBeanCollection() {
    return new ArrayList<ScheduledAnalysisProperty>();
  }

  @Override
  public Class<?> getType() {
    return ArrayList.class;
  }

  @Override
  protected BeanIdResolver<ScheduledAnalysisProperty, ScheduledAnalysisProperty> getBeanIdResolver() {
    return new BeanIdResolver<ScheduledAnalysisProperty, ScheduledAnalysisProperty>() {

      @Override
      public ScheduledAnalysisProperty getIdForBean(
        ScheduledAnalysisProperty bean) {
        return bean;
      }
    };
  }

  @Override
  protected ScheduledAnalysisProperty createNewBean() {
    return new ScheduledAnalysisProperty();
  }

  @Override
  protected ICRUDEditorFactory<ScheduledAnalysisProperty> getEditorFactory() {
    return new ICRUDEditorFactory<ScheduledAnalysisProperty>() {

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
              ((TextField) field).setNullRepresentation(""); //$NON-NLS-1$
            }
            return field;
          }
        };

        Map<Object, String> requiredMap = new HashMap<Object, String>();
        requiredMap
          .put(
            "propertyKey",
            application
              .getMessage("ScheduledAnalysisPropertyCreateMultiField.field_required_key"));
        requiredMap
          .put(
            "propertyValue",
            application
              .getMessage("ScheduledAnalysisPropertyCreateMultiField.field_required_value"));

        return new CRUDEditorWindow<ScheduledAnalysisProperty>(
          application,
          item,
          ScheduledAnalysisProperty.class,
          application
            .getMessage("ScheduledAnalysisPropertyCreateMultiField.editor_title"),
          Arrays.asList("propertyKey", "propertyValue"), formFieldFactory,
          requiredMap, editorMode);
      };
    };
  }

  @Override
  protected String[] getNestedProperties() {
    return null;
  }
}