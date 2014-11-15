package br.rio.puc.lac.wish.web.app.ui.field.wish;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.field.EntitySelectorField;
import br.rio.puc.lac.wish.web.model.AnalysisExecutionFile;

import com.vaadin.addon.jpacontainer.EntityContainer;

/**
 * 
 */
public class AnalysisExecutionFileSelectorField extends
  EntitySelectorField<AnalysisExecutionFile> {

  public AnalysisExecutionFileSelectorField(AuroraApplication application,
    EntityContainer<AnalysisExecutionFile> container) {
    super(application, container);
  }

  @Override
  protected Class<AnalysisExecutionFile> getEntityClass() {
    return AnalysisExecutionFile.class;
  }

  @Override
  protected Object getEntityId(AnalysisExecutionFile value) {
    return value.getId();
  }

  @Override
  protected String getFieldCaption() {
    return application.getApplicationMessages().getString(
      "AnalysisExecutionFileSelectorField.field_caption");
  }

  @Override
  protected Object getItemCaptionPropertyId() {
    return "name";
  }

}
