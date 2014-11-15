package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.Date;

import javax.persistence.EntityManager;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.dialog.ConfirmDialog;
import br.com.tecnoinf.aurora.ui.layout.GroupTitleLayout;
import br.com.tecnoinf.aurora.ui.view.AuroraView;
import br.rio.puc.lac.wish.web.app.ui.field.wish.ScheduledAnalysisJobCreateMultiField;
import br.rio.puc.lac.wish.web.app.ui.field.wish.ScheduledAnalysisPropertyCreateMultiField;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysis;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysisJob;
import br.rio.puc.lac.wish.web.model.ScheduledAnalysisProperty;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class ScheduledAnalysisManagerView extends AuroraView implements
  Button.ClickListener {

  private AuroraView previousView;

  private ScheduledAnalysis scheduledAnalysis;

  private Button closeButton;

  public ScheduledAnalysisManagerView(AuroraApplication application,
    AbstractModuleComponent moduleComponent,
    ScheduledAnalysis scheduledAnalysis, AuroraView previousView) {
    super(application, moduleComponent);
    this.scheduledAnalysis = scheduledAnalysis;
    this.previousView = previousView;
    build();
  }

  @Override
  public Resource getViewIcon() {
    return null;
  }

  @Override
  public String getViewTitle() {
    return application.getMessage("ScheduledAnalysisManagerView.view_title");
  }

  @Override
  public void refresh() {
  }

  @Override
  protected boolean showRefreshButton() {
    return false;
  }

  /**
   * 
   */
  protected void build() {

    //Information
    GroupTitleLayout infoGroupTitleLayout =
      new GroupTitleLayout(application
        .getMessage("ScheduledAnalysisManagerView.info_layout"));

    {
      FormLayout layout = new FormLayout();
      layout.setWidth("100%");
      layout.setSpacing(true);

      //Information
      TextField scheduledAnalysisIdField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_id"));
      scheduledAnalysisIdField.setValue(scheduledAnalysis.getId());
      scheduledAnalysisIdField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisIdField);

      //Configuration
      TextField scheduledAnalysisConfigurationField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_configuration"));
      scheduledAnalysisConfigurationField.setValue(scheduledAnalysis
        .getConfiguration().getHdfsName());
      scheduledAnalysisConfigurationField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisConfigurationField);

      //InputPath
      TextField scheduledAnalysisInputPathField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_input_path"));
      scheduledAnalysisInputPathField
        .setValue(scheduledAnalysis.getInputPath());
      scheduledAnalysisInputPathField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisInputPathField);

      //File
      TextField scheduledAnalysisFileField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_file"));
      scheduledAnalysisFileField
        .setValue(scheduledAnalysis.getFile().getName());
      scheduledAnalysisFileField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisFileField);

      //Map tasks
      TextField scheduledAnalysisMapTasksField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_map_tasks"));
      scheduledAnalysisMapTasksField.setValue(scheduledAnalysis
        .getNumberMapTasks());
      scheduledAnalysisMapTasksField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisMapTasksField);

      //Reduce tasks
      TextField scheduledAnalysisReduceTasksField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_reduce_tasks"));
      scheduledAnalysisReduceTasksField.setValue(scheduledAnalysis
        .getNumberReduceTasks());
      scheduledAnalysisReduceTasksField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisReduceTasksField);

      //Initial output path
      TextField scheduledAnalysisInitialOutputPathField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_initial_output_path"));
      scheduledAnalysisInitialOutputPathField.setValue(scheduledAnalysis
        .getInitialOutputPath());
      scheduledAnalysisInitialOutputPathField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisInitialOutputPathField);

      //Scheduled date
      PopupDateField scheduledAnalysisDateField =
        new PopupDateField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_date"));
      scheduledAnalysisDateField.setValue(scheduledAnalysis.getScheduledDate());
      scheduledAnalysisDateField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisDateField);

      //Jobs
      ScheduledAnalysisJobCreateMultiField scheduledAnalysisJobsField =
        new ScheduledAnalysisJobCreateMultiField(application, moduleComponent);
      scheduledAnalysisJobsField
        .setCaption(application
          .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_jobs"));
      scheduledAnalysisJobsField.setValue(scheduledAnalysis.getJobs());
      scheduledAnalysisJobsField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisJobsField);

      //Properties
      ScheduledAnalysisPropertyCreateMultiField scheduledAnalysisPropertiesField =
        new ScheduledAnalysisPropertyCreateMultiField(application,
          moduleComponent);
      scheduledAnalysisPropertiesField
        .setCaption(application
          .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_properties"));
      scheduledAnalysisPropertiesField.setValue(scheduledAnalysis
        .getProperties());
      scheduledAnalysisPropertiesField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisPropertiesField);

      //Aggregator job class
      TextField scheduledAnalysisAggregatorJobClassField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_aggregator_job_class"));
      scheduledAnalysisAggregatorJobClassField.setValue(scheduledAnalysis
        .getAggregatorJobClass());
      scheduledAnalysisAggregatorJobClassField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisAggregatorJobClassField);

      //Max distance
      TextField scheduledAnalysisMaxDistanceField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_max_distance"));
      scheduledAnalysisMaxDistanceField.setValue(scheduledAnalysis
        .getMaxDistance());
      scheduledAnalysisMaxDistanceField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisMaxDistanceField);

      //Temporal weight
      TextField scheduledAnalysisTemporalWeightField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_temporal_weight"));
      scheduledAnalysisTemporalWeightField.setValue(scheduledAnalysis
        .getTemporalWeight());
      scheduledAnalysisTemporalWeightField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisTemporalWeightField);

      //Spatial weight
      TextField scheduledAnalysisSpatialWeightField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_spatial_weight"));
      scheduledAnalysisSpatialWeightField.setValue(scheduledAnalysis
        .getSpatialWeight());
      scheduledAnalysisSpatialWeightField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisSpatialWeightField);

      //Semantic weight
      TextField scheduledAnalysisSemanticWeightField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_scheduled_analysis_semantic_weight"));
      scheduledAnalysisSemanticWeightField.setValue(scheduledAnalysis
        .getSemanticWeight());
      scheduledAnalysisSemanticWeightField.setReadOnly(true);
      layout.addComponent(scheduledAnalysisSemanticWeightField);

      //
      infoGroupTitleLayout.add(layout);
    }

    addComponent(infoGroupTitleLayout);

    //Status
    GroupTitleLayout statusGroupTitleLayout =
      new GroupTitleLayout(application
        .getMessage("ScheduledAnalysisManagerView.status_layout"));

    {
      FormLayout layout = new FormLayout();
      layout.setWidth("100%");
      layout.setSpacing(true);

      //Exec Begin Date
      PopupDateField execBeginDateField =
        new PopupDateField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_exec_begin_date"));
      execBeginDateField.setValue(scheduledAnalysis.getExecBeginDate());
      execBeginDateField.setReadOnly(true);
      layout.addComponent(execBeginDateField);

      //Exec End Date
      PopupDateField execEndDateField =
        new PopupDateField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_exec_end_date"));
      execEndDateField.setValue(scheduledAnalysis.getExecEndDate());
      execEndDateField.setReadOnly(true);
      layout.addComponent(execEndDateField);

      //Execution Status 
      TextField execStatusField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_exec_status"));
      execStatusField.setValue(scheduledAnalysis.getExecutionStatus());
      execStatusField.setReadOnly(true);
      layout.addComponent(execStatusField);

      //Execution Message 
      TextField execMessageField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_exec_message"));
      execMessageField.setValue(scheduledAnalysis.getExecutionMessage());
      execMessageField.setReadOnly(true);
      layout.addComponent(execMessageField);

      //Final Output Path
      TextField finalOutputPathField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_final_output_path"));
      finalOutputPathField.setValue(scheduledAnalysis.getFinalOutputPath());
      finalOutputPathField.setReadOnly(true);
      layout.addComponent(finalOutputPathField);

      //
      statusGroupTitleLayout.add(layout);
    }

    addComponent(statusGroupTitleLayout);

    //Publication...
    if (scheduledAnalysis.getAnalysisPublication() != null) {
      GroupTitleLayout publicationGroupTitleLayout =
        new GroupTitleLayout(application
          .getMessage("ScheduledAnalysisManagerView.publication_layout"));

      {
        FormLayout layout = new FormLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);

        //Publication Id 
        TextField publicationIdField =
          new TextField(
            application
              .getMessage("ScheduledAnalysisManagerView.cfield_caption_publication_id"));
        publicationIdField.setValue(scheduledAnalysis.getAnalysisPublication()
          .getId());
        publicationIdField.setReadOnly(true);
        layout.addComponent(publicationIdField);

        //Publication Date
        PopupDateField publicationDateField =
          new PopupDateField(
            application
              .getMessage("ScheduledAnalysisManagerView.cfield_caption_publication_date"));
        publicationDateField.setValue(scheduledAnalysis
          .getAnalysisPublication().getDate());
        publicationDateField.setReadOnly(true);
        layout.addComponent(publicationDateField);

        //Publication IsPublic
        CheckBox publicationIsPublicField =
          new CheckBox(
            application
              .getMessage("ScheduledAnalysisManagerView.cfield_caption_publication_is_public"));
        publicationIsPublicField.setValue(scheduledAnalysis
          .getAnalysisPublication().getOpenPublication());
        publicationIsPublicField.setReadOnly(true);
        layout.addComponent(publicationIsPublicField);

        //Publication ReceiveVote
        CheckBox publicationReceiveVoteField =
          new CheckBox(
            application
              .getMessage("ScheduledAnalysisManagerView.cfield_caption_publication_receive_vote"));
        publicationReceiveVoteField.setValue(scheduledAnalysis
          .getAnalysisPublication().getReceiveVote());
        publicationReceiveVoteField.setReadOnly(true);
        layout.addComponent(publicationReceiveVoteField);

        //Button
        Button viewButton =
          new Button(
            application
              .getMessage("ScheduledAnalysisManagerView.publication_view_button_caption"));
        viewButton.setStyleName(Reindeer.BUTTON_SMALL);
        viewButton.addListener(new Button.ClickListener() {
          @Override
          public void buttonClick(ClickEvent event) {
            AnalysisPublicationManagerView managerView =
              new AnalysisPublicationManagerView(application, moduleComponent,
                scheduledAnalysis.getAnalysisPublication(),
                ScheduledAnalysisManagerView.this);
            moduleComponent.setAuroraView(managerView);
          }
        });
        layout.addComponent(viewButton);

        //
        publicationGroupTitleLayout.add(layout);
      }

      addComponent(publicationGroupTitleLayout);
    }

    //Status
    GroupTitleLayout cloneGroupTitleLayout =
      new GroupTitleLayout(application
        .getMessage("ScheduledAnalysisManagerView.clone_layout"));

    {

      FormLayout layout = new FormLayout();
      layout.setWidth("100%");
      layout.setSpacing(true);

      //Clone OutputPath
      final TextField cloneOutputPathField =
        new TextField(
          application
            .getMessage("ScheduledAnalysisManagerView.cfield_caption_clone_output_path"));
      cloneOutputPathField.setValue(scheduledAnalysis.getInitialOutputPath());
      layout.addComponent(cloneOutputPathField);

      //Clone Scheduled Date
      final PopupDateField cloneDateField =
        new PopupDateField(application
          .getMessage("ScheduledAnalysisManagerView.cfield_caption_clone_date"));
      cloneDateField.setValue(scheduledAnalysis.getScheduledDate());
      cloneDateField.setResolution(PopupDateField.RESOLUTION_MIN);
      layout.addComponent(cloneDateField);

      //Button
      Button cloneButton =
        new Button(
          application
            .getMessage("ScheduledAnalysisManagerView.schedule_analysis_clone_button_caption"));
      cloneButton.setStyleName(Reindeer.BUTTON_SMALL);
      cloneButton.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          ConfirmDialog.show(application.getMainWindow(), application
            .getMessage("ScheduledAnalysisManagerView.clone_message"),
            new ConfirmDialog.Listener() {

              @Override
              public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {

                  //Cloning scheduled analysis                  
                  ScheduledAnalysis clone = new ScheduledAnalysis();
                  clone.setConfiguration(scheduledAnalysis.getConfiguration());
                  clone
                    .setContentsImport(scheduledAnalysis.getContentsImport());
                  clone.setInitialInputPath(scheduledAnalysis
                    .getInitialInputPath());
                  clone.setFile(scheduledAnalysis.getFile());
                  clone
                    .setNumberMapTasks(scheduledAnalysis.getNumberMapTasks());
                  clone.setNumberReduceTasks(scheduledAnalysis
                    .getNumberReduceTasks());
                  clone.setInitialOutputPath(scheduledAnalysis
                    .getInitialOutputPath());
                  if (cloneOutputPathField.getValue() != null) {
                    clone.setInitialOutputPath((String) cloneOutputPathField
                      .getValue());
                  }
                  clone.setScheduledDate(scheduledAnalysis.getScheduledDate());
                  if (cloneDateField.getValue() != null) {
                    clone.setScheduledDate((Date) cloneDateField.getValue());
                  }
                  //Jobs
                  for (ScheduledAnalysisJob job : scheduledAnalysis.getJobs()) {
                    ScheduledAnalysisJob cloneJob = new ScheduledAnalysisJob();
                    cloneJob.setOrdination(job.getOrdination());
                    cloneJob.setJobClass(job.getJobClass());
                    clone.addJob(cloneJob);
                  }
                  //Properties
                  for (ScheduledAnalysisProperty property : scheduledAnalysis
                    .getProperties()) {
                    ScheduledAnalysisProperty cloneProperty =
                      new ScheduledAnalysisProperty();
                    cloneProperty.setPropertyKey(property.getPropertyKey());
                    cloneProperty.setPropertyValue(property.getPropertyValue());
                    clone.addProperty(cloneProperty);
                  }
                  //Aggregator
                  clone.setAggregatorJobClass(scheduledAnalysis
                    .getAggregatorJobClass());
                  clone.setMaxDistance(scheduledAnalysis.getMaxDistance());
                  clone
                    .setTemporalWeight(scheduledAnalysis.getTemporalWeight());
                  clone.setSpatialWeight(scheduledAnalysis.getSpatialWeight());
                  clone
                    .setSemanticWeight(scheduledAnalysis.getSemanticWeight());
                  //Status
                  clone.setExecutionStatus(0);

                  //EntityManager
                  EntityManager man =
                    application.getJPAHandler().getEntityManagerFactory()
                      .createEntityManager();
                  try {
                    man.getTransaction().begin();
                    man.merge(clone);
                    man.getTransaction().commit();

                    application
                      .getMainWindow()
                      .showNotification(
                        application
                          .getMessage("ScheduledAnalysisManagerView.clone_successfully_caption_notification"),
                        application
                          .getMessage("ScheduledAnalysisManagerView.clone_successfully_message_notification"),
                        Notification.TYPE_TRAY_NOTIFICATION);
                  }
                  catch (Exception e) {
                    man.getTransaction().rollback();
                    application
                      .getMainWindow()
                      .showNotification(
                        application
                          .getMessage("ScheduledAnalysisManagerView.clone_error_caption_notification"),
                        application
                          .getMessage("ScheduledAnalysisManagerView.clone_error_description_notification")
                          + e.getLocalizedMessage(),
                        Notification.TYPE_ERROR_MESSAGE);
                  }
                  finally {
                    man.close();
                  }
                }
              }
            });
        }
      });
      layout.addComponent(cloneButton);

      //
      cloneGroupTitleLayout.add(layout);
    }

    addComponent(cloneGroupTitleLayout);

    //
    closeButton =
      new Button(application.getMessage("CRUDEditor.close_button_caption"),
        this);
    closeButton.setStyleName(BaseTheme.BUTTON_LINK);

    addComponent(closeButton);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == closeButton) {
      moduleComponent.setAuroraView(previousView);
    }
  }
}
