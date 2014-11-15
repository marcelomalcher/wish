package br.rio.puc.lac.wish.web.app;

import java.util.ArrayList;
import java.util.List;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.app.services.IModule;
import br.com.tecnoinf.aurora.ui.layout.AbstractModuleComponentLayout;
import br.com.tecnoinf.aurora.ui.layout.MelodionHorizontalSplitLayout;
import br.com.tecnoinf.aurora.ui.options.ControlOption;
import br.com.tecnoinf.aurora.ui.options.GroupOption;
import br.com.tecnoinf.aurora.ui.view.AuroraView;
import br.com.tecnoinf.aurora.utils.security.SecurityEntityHelper;
import br.rio.puc.lac.wish.web.app.ui.view.wish.AnalysisConfigurationCRUDView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.AnalysisExecutionFileCRUDView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.AnalysisPublicationManagementView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.ContentsHdfsImportCRUDView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.ScheduledAnalysisCRUDView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.ScheduledAnalysisManagementView;
import br.rio.puc.lac.wish.web.app.ui.view.wish.WishDashboard;

import com.vaadin.terminal.ThemeResource;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishManagerModuleComponent extends AbstractModuleComponent {

  private List<GroupOption> groupOptions;

  private AnalysisConfigurationCRUDView analysisConfigurationView;
  private static final int ANALYSIS_CONFIGURATION_ID = 1;

  private ContentsHdfsImportCRUDView contentsHdfsImportCRUDView;
  private static final int CONTENTS_HDFS_IMPORT_ID = 2;

  private AnalysisExecutionFileCRUDView analysisExecutionFileCRUDView;
  private static final int ANALYSIS_EXECUTION_FILE_ID = 3;

  private ScheduledAnalysisCRUDView scheduledAnalysisCRUDView;
  private static final int SCHEDULED_ANALYSIS_ID = 4;

  private ScheduledAnalysisManagementView scheduledAnalysisManagementView;
  private static final int SCHEDULED_ANALYSIS_MANAGEMENT_ID = 5;

  private AnalysisPublicationManagementView analysisPublicationManagementView;
  private static final int ANALYSIS_PUBLIC_MANAGEMENT_ID = 6;

  public WishManagerModuleComponent(AuroraApplication application,
    IModule module) {
    super(application, module);
  }

  private WishDashboard dashboard;

  @Override
  public AuroraView getDashboard() {
    if (dashboard == null) {
      dashboard = new WishDashboard(application, this);
    }
    return dashboard;
  }

  @Override
  public List<GroupOption> getGroupOptions() {
    if (groupOptions == null) {
      groupOptions = new ArrayList<GroupOption>();

      //Settings group
      GroupOption settingsGroupOption =
        new GroupOption(application.getApplicationMessages().getString(
          "WishManagerModuleComponent.settings_tab_caption"),
          new ThemeResource("../aurora/icons/cog.png"));

      {
        // > AnalysisConfigurationCRUDView
        settingsGroupOption
          .addControlOption(new ControlOption(
            ANALYSIS_CONFIGURATION_ID,
            SecurityEntityHelper.getControl(application.getJPAHandler(),
              AnalysisConfigurationCRUDView.class.getCanonicalName(), module
                .getModuleName()
                + "-AnalysisConfiguration", module.getModule(), application
                .mustInsertSecurityValues()),
            application
              .getApplicationMessages()
              .getString(
                "WishManagerModuleComponent.settings_hadoop_configurations_option"),
            null));

        // > ContentsHdfsImportCRUDView
        settingsGroupOption.addControlOption(new ControlOption(
          CONTENTS_HDFS_IMPORT_ID, SecurityEntityHelper.getControl(application
            .getJPAHandler(), ContentsHdfsImportCRUDView.class
            .getCanonicalName(),
            module.getModuleName() + "-ContentsHdfsImport", module.getModule(),
            application.mustInsertSecurityValues()),
          application.getApplicationMessages().getString(
            "WishManagerModuleComponent.settings_import_contents_hdfs_option"),
          null));

        // > AnalysisExecutionFileCRUDView
        settingsGroupOption.addControlOption(new ControlOption(
          ANALYSIS_EXECUTION_FILE_ID, SecurityEntityHelper.getControl(
            application.getJPAHandler(), AnalysisExecutionFileCRUDView.class
              .getCanonicalName(), module.getModuleName()
              + "-AnalysisExecutionFile", module.getModule(), application
              .mustInsertSecurityValues()), application
            .getApplicationMessages().getString(
              "WishManagerModuleComponent.settings_execution_file_option"),
          null));
      }

      groupOptions.add(settingsGroupOption);

      //Executions group
      GroupOption executionsGroupOption =
        new GroupOption(application.getApplicationMessages().getString(
          "WishManagerModuleComponent.executions_tab_caption"),
          new ThemeResource("../aurora/icons/chart_organisation.png"));
      {
        // > ScheduledAnalysisCRUDView
        executionsGroupOption
          .addControlOption(new ControlOption(
            SCHEDULED_ANALYSIS_ID,
            SecurityEntityHelper.getControl(application.getJPAHandler(),
              ScheduledAnalysisCRUDView.class.getCanonicalName(), module
                .getModuleName()
                + "-ScheduledAnalysis", module.getModule(), application
                .mustInsertSecurityValues()),
            application
              .getMessage("WishManagerModuleComponent.executions_schedule_option"),
            null));

        //> ScheduledAnalysisManagementView
        executionsGroupOption
          .addControlOption(new ControlOption(
            SCHEDULED_ANALYSIS_MANAGEMENT_ID,
            SecurityEntityHelper.getControl(application.getJPAHandler(),
              ScheduledAnalysisManagementView.class.getCanonicalName(), module
                .getModuleName()
                + "-ScheduledAnalysisManagement", module.getModule(),
              application.mustInsertSecurityValues()),
            application
              .getMessage("WishManagerModuleComponent.executions_management_option"),
            null));
      }

      groupOptions.add(executionsGroupOption);

      //Publications group
      GroupOption publicationsGroupOption =
        new GroupOption(application.getApplicationMessages().getString(
          "WishManagerModuleComponent.publications_tab_caption"),
          new ThemeResource("../aurora/icons/map.png"));
      {
        //> AnalysisPublicationManagementView
        publicationsGroupOption
          .addControlOption(new ControlOption(
            ANALYSIS_PUBLIC_MANAGEMENT_ID,
            SecurityEntityHelper.getControl(application.getJPAHandler(),
              AnalysisPublicationManagementView.class.getCanonicalName(),
              module.getModuleName() + "-AnalysisPublicationManagement", module
                .getModule(), application.mustInsertSecurityValues()),
            application
              .getMessage("WishManagerModuleComponent.publications_management_option"),
            null));
      }

      groupOptions.add(publicationsGroupOption);
    }

    return groupOptions;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void executeControlOption(ControlOption controlOption) {
    AuroraView view;
    switch (controlOption.getId()) {
      case ANALYSIS_CONFIGURATION_ID:
        if (analysisConfigurationView == null) {
          analysisConfigurationView =
            new AnalysisConfigurationCRUDView(this.application, this,
              controlOption.getControl());
        }
        view = analysisConfigurationView;
        break;
      case CONTENTS_HDFS_IMPORT_ID:
        if (contentsHdfsImportCRUDView == null) {
          contentsHdfsImportCRUDView =
            new ContentsHdfsImportCRUDView(this.application, this,
              controlOption.getControl());
        }
        view = contentsHdfsImportCRUDView;
        break;
      case ANALYSIS_EXECUTION_FILE_ID:
        if (analysisExecutionFileCRUDView == null) {
          analysisExecutionFileCRUDView =
            new AnalysisExecutionFileCRUDView(this.application, this,
              controlOption.getControl());
        }
        view = analysisExecutionFileCRUDView;
        break;
      case SCHEDULED_ANALYSIS_ID:
        if (scheduledAnalysisCRUDView == null) {
          scheduledAnalysisCRUDView =
            new ScheduledAnalysisCRUDView(this.application, this, controlOption
              .getControl());
        }
        view = scheduledAnalysisCRUDView;
        break;
      case SCHEDULED_ANALYSIS_MANAGEMENT_ID:
        if (scheduledAnalysisManagementView == null) {
          scheduledAnalysisManagementView =
            new ScheduledAnalysisManagementView(this.application, this,
              controlOption.getControl());
        }
        view = scheduledAnalysisManagementView;
        break;
      case ANALYSIS_PUBLIC_MANAGEMENT_ID:
        if (analysisPublicationManagementView == null) {
          analysisPublicationManagementView =
            new AnalysisPublicationManagementView(this.application, this,
              controlOption.getControl());
        }
        view = analysisPublicationManagementView;
        break;
      default:
        getApplication()
          .getMainWindow()
          .showNotification(
            application.getAuroraMessages().getString(
              "AbstractModuleComponent.option_selected") + controlOption.getTitle()); //$NON-NLS-1$
        return;
    }
    setAuroraView(view);
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public AbstractModuleComponentLayout getModuleLayout() {
    return new MelodionHorizontalSplitLayout(this);
  }
}