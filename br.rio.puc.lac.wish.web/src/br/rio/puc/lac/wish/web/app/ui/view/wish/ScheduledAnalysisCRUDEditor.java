package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.layout.GroupTitleLayout;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDEditor;
import br.com.tecnoinf.aurora.ui.view.crud.CRUDUtils.CRUDMode;
import br.com.tecnoinf.aurora.ui.widget.numberfield.TextualNumberField;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.app.ui.field.wish.AnalysisConfigurationSelectorField;
import br.rio.puc.lac.wish.web.app.ui.field.wish.AnalysisExecutionFileSelectorField;
import br.rio.puc.lac.wish.web.app.ui.field.wish.ContentsHdfsImportSelectField;
import br.rio.puc.lac.wish.web.app.ui.field.wish.ScheduledAnalysisJobCreateMultiField;
import br.rio.puc.lac.wish.web.app.ui.field.wish.ScheduledAnalysisPropertyCreateMultiField;
import br.rio.puc.lac.wish.web.model.AnalysisConfiguration;
import br.rio.puc.lac.wish.web.model.AnalysisExecutionFile;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysis;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ScheduledAnalysisCRUDEditor extends CRUDEditor<ScheduledAnalysis> {

  private Map<Object, String> requiredMap;

  private FormFieldFactory formFieldFactory;

  private Collection<?> propertyIds;

  public ScheduledAnalysisCRUDEditor(AuroraApplication application,
    AbstractModuleComponent moduleComponent, Item item, CRUDMode crudMode) {
    super(application, moduleComponent, item, crudMode);
  }

  @Override
  public Map<Object, String> getRequiredMap() {
    if (requiredMap == null) {
      requiredMap = new HashMap<Object, String>();
      requiredMap
        .put(
          "configuration",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_configuration"));

      //      requiredMap
      //        .put(
      //          "contentsImport",
      //          application
      //            .getMessage("ScheduledAnalysisCRUDEditor.field_required_contentsImport"));

      requiredMap.put("file", application
        .getMessage("ScheduledAnalysisCRUDEditor.field_required_file"));
      requiredMap
        .put(
          "scheduledDate",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_scheduledDate"));
      requiredMap
        .put(
          "numberMapTasks",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_numberMapTasks"));
      requiredMap
        .put(
          "numberReduceTasks",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_numberReduceTasks"));
      requiredMap
        .put(
          "initialOutputPath",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_initialOutputPath"));
      requiredMap.put("jobs", application
        .getMessage("ScheduledAnalysisCRUDEditor.field_required_job"));
      requiredMap.put("properties", application
        .getMessage("ScheduledAnalysisCRUDEditor.field_required_properties"));
      requiredMap
        .put(
          "aggregatorJobClass",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_aggregatorJobClass"));
      requiredMap.put("maxDistance", application
        .getMessage("ScheduledAnalysisCRUDEditor.field_required_maxDistance"));
      requiredMap
        .put(
          "temporalWeight",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_temporalWeight"));
      requiredMap
        .put(
          "spatialWeight",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_spatialWeight"));
      requiredMap
        .put(
          "semanticWeight",
          application
            .getMessage("ScheduledAnalysisCRUDEditor.field_required_semanticWeight"));
    }
    return requiredMap;
  }

  @Override
  public FormFieldFactory getFormFieldFactory() {
    if (formFieldFactory == null) {
      formFieldFactory = new FormFieldFactory() {

        @Override
        public Field createField(Item item, Object propertyId,
          Component uiContext) {
          Field field = null;
          if ("configuration".equals(propertyId)) {
            field =
              new AnalysisConfigurationSelectorField(application,
                AuroraJPAContainerFactory.make(AnalysisConfiguration.class,
                  application.getJPAHandler().getEntityManagerFactory()
                    .createEntityManager()));
          }
          else if ("contentsImport".equals(propertyId)) {
            ContentsHdfsImportSelectField importsField =
              new ContentsHdfsImportSelectField(application, moduleComponent);
            importsField.setCaption(application
              .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_import"));
            field = importsField;
          }
          else if ("file".equals(propertyId)) {
            AnalysisExecutionFileSelectorField fileField =
              new AnalysisExecutionFileSelectorField(application,
                AuroraJPAContainerFactory.make(AnalysisExecutionFile.class,
                  application.getJPAHandler().getEntityManagerFactory()
                    .createEntityManager()));
            field = fileField;
          }
          else if ("numberMapTasks".equals(propertyId)) {
            TextualNumberField<Integer> nField =
              new TextualNumberField<Integer>(Integer.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_numberMapTasks"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else if ("numberReduceTasks".equals(propertyId)) {
            TextualNumberField<Integer> nField =
              new TextualNumberField<Integer>(Integer.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_numberMaReduceTasks"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else if ("jobs".equals(propertyId)) {
            ScheduledAnalysisJobCreateMultiField jobsField =
              new ScheduledAnalysisJobCreateMultiField(application,
                moduleComponent);
            jobsField.setCaption(application
              .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_jobs"));
            field = jobsField;
          }
          else if ("properties".equals(propertyId)) {
            ScheduledAnalysisPropertyCreateMultiField propertiesField =
              new ScheduledAnalysisPropertyCreateMultiField(application,
                moduleComponent);
            propertiesField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_properties"));
            field = propertiesField;
          }
          else if ("maxDistance".equals(propertyId)) {
            TextualNumberField<Float> nField =
              new TextualNumberField<Float>(Float.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_maxDistance"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else if ("temporalWeight".equals(propertyId)) {
            TextualNumberField<Float> nField =
              new TextualNumberField<Float>(Float.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_temporalWeight"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else if ("spatialWeight".equals(propertyId)) {
            TextualNumberField<Float> nField =
              new TextualNumberField<Float>(Float.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_spatialWeight"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else if ("semanticWeight".equals(propertyId)) {
            TextualNumberField<Float> nField =
              new TextualNumberField<Float>(Float.class);
            nField
              .setCaption(application
                .getMessage("ScheduledAnalysisCRUDEditor.cfield_caption_semanticWeight"));
            nField.setAllowNegative(false);
            nField.setAllowNull(false);
            field = nField;
          }
          else {
            field =
              DefaultFieldFactory.get()
                .createField(item, propertyId, uiContext);
            if (field instanceof TextField) {
              ((TextField) field).setNullRepresentation("");
            }
            if (field instanceof DateField) {
              ((DateField) field).setResolution(PopupDateField.RESOLUTION_MIN);
            }
          }
          //
          return field;
        }
      };
    }
    return formFieldFactory;
  }

  @Override
  public Class<ScheduledAnalysis> getBeanClass() {
    return ScheduledAnalysis.class;
  }

  @Override
  public Collection<?> getPropertyIds() {
    if (propertyIds == null) {
      propertyIds =
        Arrays.asList("configuration", "contentsImport", "initialInputPath",
          "file", "numberMapTasks", "numberReduceTasks", "initialOutputPath",
          "scheduledDate", "jobs", "properties", "aggregatorJobClass",
          "maxDistance", "temporalWeight", "spatialWeight", "semanticWeight");
    }
    return propertyIds;
  }

  @Override
  public Resource getViewIcon() {
    return new ThemeResource("icons/calendar.png");
  }

  @Override
  public String getViewTitle() {
    return application.getMessage("ScheduledAnalysisCRUDEditor.view_title");
  }

  /**
   * 
   */
  @Override
  protected BeanValidationForm createFormWithLayout() {
    return new ScheduledAnalysisForm(application, getBeanClass(),
      getItemObject(), getEditorMode());
  }

  /**
   * 
   * @author Marcelo
   * 
   */
  public static class ScheduledAnalysisForm extends
    BeanValidationForm<ScheduledAnalysis> {

    protected AuroraApplication application;

    protected VerticalLayout mainLayout;

    private FormLayout configFormLayout;

    private FormLayout detailsFormLayout;

    private ScheduledAnalysis scheduledAnalysis;
    private CRUDMode crudMode;

    /**
     * 
     * @param application
     * @param beanClass
     */
    public ScheduledAnalysisForm(AuroraApplication application,
      Class<ScheduledAnalysis> beanClass, ScheduledAnalysis scheduledAnalysis,
      CRUDMode crudMode) {
      super(beanClass);
      this.application = application;

      this.scheduledAnalysis = scheduledAnalysis;
      this.crudMode = crudMode;

      mainLayout = new VerticalLayout();
      mainLayout.setWidth("100%");
      mainLayout.setSpacing(true);

      //
      GroupTitleLayout configTitleLayout =
        new GroupTitleLayout(application
          .getMessage("ScheduledAnalysisForm.tab_configuration"));

      configFormLayout = new FormLayout();
      configFormLayout.setSizeFull();
      configFormLayout.setSpacing(true);
      configFormLayout.setMargin(true);

      configTitleLayout.add(configFormLayout);

      mainLayout.addComponent(configTitleLayout);

      //
      GroupTitleLayout detailsTitleLayout =
        new GroupTitleLayout(application
          .getMessage("ScheduledAnalysisForm.tab_details"));

      detailsFormLayout = new FormLayout();
      detailsFormLayout.setSizeFull();
      detailsFormLayout.setSpacing(true);
      detailsFormLayout.setMargin(true);

      detailsTitleLayout.add(detailsFormLayout);

      mainLayout.addComponent(detailsTitleLayout);

      setLayout(mainLayout);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected void attachField(Object propertyId, Field field) {
      if ("configuration".equals(propertyId)
        || "contentsImport".equals(propertyId)
        || "initialInputPath".equals(propertyId) || "file".equals(propertyId)
        || "numberMapTasks".equals(propertyId)
        || "numberReduceTasks".equals(propertyId)
        || "initialOutputPath".equals(propertyId)
        || "scheduledDate".equals(propertyId)) {
        configFormLayout.addComponent(field);
      }
      else if ("jobs".equals(propertyId) || "properties".equals(propertyId)
        || "aggregatorJobClass".equals(propertyId)
        || "maxDistance".equals(propertyId)
        || "temporalWeight".equals(propertyId)
        || "spatialWeight".equals(propertyId)
        || "semanticWeight".equals(propertyId)) {
        detailsFormLayout.addComponent(field);
      }
      //Checking if analysis already executed
      if ((crudMode == CRUDMode.EDITION)
        && (scheduledAnalysis.getExecutionStatus() != 0)) {
        field.setReadOnly(true);
      }
    }
  }
}
