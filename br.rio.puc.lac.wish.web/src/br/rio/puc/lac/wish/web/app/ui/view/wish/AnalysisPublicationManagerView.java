package br.rio.puc.lac.wish.web.app.ui.view.wish;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.dialog.ConfirmDialog;
import br.com.tecnoinf.aurora.ui.layout.GroupTitleLayout;
import br.com.tecnoinf.aurora.ui.view.AuroraView;
import br.rio.puc.lac.wish.web.app.ui.layout.wish.AnalysisPublicationEventViewerLayout;
import br.rio.puc.lac.wish.web.model.AnalysisFeedback;
import br.rio.puc.lac.wish.web.model.AnalysisFeedbackContent;
import br.rio.puc.lac.wish.web.model.AnalysisPublication;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEvent;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEventContent;

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

public class AnalysisPublicationManagerView extends AuroraView implements
  Button.ClickListener {

  private AuroraView previousView;

  private AnalysisPublication analysisPublication;

  private Button closeButton;

  public AnalysisPublicationManagerView(AuroraApplication application,
    AbstractModuleComponent moduleComponent,
    AnalysisPublication analysisPublication, AuroraView previousView) {
    super(application, moduleComponent);
    this.analysisPublication = analysisPublication;
    this.previousView = previousView;
    build();
  }

  @Override
  public Resource getViewIcon() {
    return null;
  }

  @Override
  public String getViewTitle() {
    return application.getMessage("AnalysisPublicationManagerView.view_title");
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
        .getMessage("AnalysisPublicationManagerView.info_layout"));

    {
      FormLayout layout = new FormLayout();
      layout.setWidth("100%");
      layout.setSpacing(true);

      //Publication Id 
      TextField publicationIdField =
        new TextField(
          application
            .getMessage("AnalysisPublicationManagerView.cfield_caption_publication_id"));
      publicationIdField.setValue(analysisPublication.getId());
      publicationIdField.setReadOnly(true);
      layout.addComponent(publicationIdField);

      //Publication Date
      PopupDateField publicationDateField =
        new PopupDateField(
          application
            .getMessage("AnalysisPublicationManagerView.cfield_caption_publication_date"));
      publicationDateField.setValue(analysisPublication.getDate());
      publicationDateField.setReadOnly(true);
      layout.addComponent(publicationDateField);

      //Publication IsPublic
      final CheckBox publicationIsPublicField =
        new CheckBox(
          application
            .getMessage("AnalysisPublicationManagerView.cfield_caption_publication_is_public"));
      publicationIsPublicField.setValue(analysisPublication
        .getOpenPublication());
      layout.addComponent(publicationIsPublicField);

      //Publication ReceiveVote
      final CheckBox publicationReceiveVoteField =
        new CheckBox(
          application
            .getMessage("AnalysisPublicationManagerView.cfield_caption_publication_receive_vote"));
      publicationReceiveVoteField
        .setValue(analysisPublication.getReceiveVote());
      layout.addComponent(publicationReceiveVoteField);

      //Button
      Button updateButton =
        new Button(
          application
            .getMessage("AnalysisPublicationManagerView.analysis_publication_update_button_caption"));
      updateButton.setStyleName(Reindeer.BUTTON_SMALL);
      updateButton.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          ConfirmDialog.show(application.getMainWindow(), application
            .getMessage("AnalysisPublicationManagerView.update_message"),
            new ConfirmDialog.Listener() {

              @Override
              public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {

                  //
                  analysisPublication
                    .setOpenPublication(publicationIsPublicField.booleanValue());
                  analysisPublication
                    .setReceiveVote(publicationReceiveVoteField.booleanValue());

                  //EntityManager
                  EntityManager man =
                    application.getJPAHandler().getEntityManagerFactory()
                      .createEntityManager();
                  try {
                    man.getTransaction().begin();
                    man.merge(analysisPublication);
                    man.getTransaction().commit();

                    application
                      .getMainWindow()
                      .showNotification(
                        application
                          .getMessage("AnalysisPublicationManagerView.update_successfully_caption_notification"),
                        application
                          .getMessage("AnalysisPublicationManagerView.update_successfully_message_notification"),
                        Notification.TYPE_TRAY_NOTIFICATION);
                  }
                  catch (Exception e) {
                    man.getTransaction().rollback();
                    application
                      .getMainWindow()
                      .showNotification(
                        application
                          .getMessage("AnalysisPublicationManagerView.update_error_caption_notification"),
                        application
                          .getMessage("AnalysisPublicationManagerView.update_error_description_notification")
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
      layout.addComponent(updateButton);

      //
      if (analysisPublication.getAnalysisFeedback() == null) {
        //Button
        final Button askFeedbackButton =
          new Button(
            application
              .getMessage("AnalysisPublicationManagerView.analysis_publication_ask_feedback_button_caption"));
        askFeedbackButton.setStyleName(Reindeer.BUTTON_SMALL);
        askFeedbackButton.addListener(new Button.ClickListener() {
          @Override
          public void buttonClick(ClickEvent event) {
            ConfirmDialog
              .show(
                application.getMainWindow(),
                application
                  .getMessage("AnalysisPublicationManagerView.ask_feedback_message"),
                new ConfirmDialog.Listener() {

                  @Override
                  public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {

                      //
                      AnalysisFeedback analysisFeedback =
                        new AnalysisFeedback();
                      analysisFeedback
                        .setAnalysisPublication(analysisPublication);
                      analysisFeedback
                        .setDate(Calendar.getInstance().getTime());

                      List<AnalysisFeedbackContent> feedbackContents =
                        new ArrayList<AnalysisFeedbackContent>();

                      for (AnalysisPublicationEvent event : analysisPublication
                        .getEvents()) {

                        for (AnalysisPublicationEventContent content : event
                          .getContents()) {
                          //Creating feedback content...
                          AnalysisFeedbackContent feedbackContent =
                            new AnalysisFeedbackContent();
                          feedbackContent.setRequestFeedbackId(UUID
                            .randomUUID().toString());
                          feedbackContent
                            .setAnalysisPublicationEventContent(content);
                          feedbackContents.add(feedbackContent);
                        }
                      }

                      analysisFeedback.setContents(feedbackContents);

                      //EntityManager
                      EntityManager man =
                        application.getJPAHandler().getEntityManagerFactory()
                          .createEntityManager();
                      try {
                        man.getTransaction().begin();
                        man.merge(analysisFeedback);
                        man.getTransaction().commit();

                        askFeedbackButton.setEnabled(false);

                        application
                          .getMainWindow()
                          .showNotification(
                            application
                              .getMessage("AnalysisPublicationManagerView.ask_feedback_successfully_caption_notification"),
                            application
                              .getMessage("AnalysisPublicationManagerView.ask_feedback_successfully_message_notification"),
                            Notification.TYPE_TRAY_NOTIFICATION);
                      }
                      catch (Exception e) {
                        man.getTransaction().rollback();
                        application
                          .getMainWindow()
                          .showNotification(
                            application
                              .getMessage("AnalysisPublicationManagerView.ask_feedback_error_caption_notification"),
                            application
                              .getMessage("AnalysisPublicationManagerView.ask_feedback_error_description_notification")
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
        layout.addComponent(askFeedbackButton);
      }

      //
      infoGroupTitleLayout.add(layout);
    }

    addComponent(infoGroupTitleLayout);

    //Information
    GroupTitleLayout resultsGroupTitleLayout =
      new GroupTitleLayout(application
        .getMessage("AnalysisPublicationManagerView.results_layout"));
    {
      //Iterating through events...
      for (AnalysisPublicationEvent event : analysisPublication.getEvents()) {
        AnalysisPublicationEventViewerLayout layout =
          new AnalysisPublicationEventViewerLayout(application, event);
        resultsGroupTitleLayout.add(layout);
      }
    }
    addComponent(resultsGroupTitleLayout);

    //
    closeButton =
      new Button(application.getMessage("CRUDEditor.close_button_caption"),
        this);
    closeButton.setStyleName(BaseTheme.BUTTON_LINK);

    addComponent(closeButton);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == closeButton) {
      moduleComponent.setAuroraView(previousView);
    }
  }
}
