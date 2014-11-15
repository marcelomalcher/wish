package br.rio.puc.lac.wish.web.app.ui.field.wish;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.field.EntitySelectorField;
import br.rio.puc.lac.wish.web.model.AnalysisConfiguration;

import com.vaadin.addon.jpacontainer.EntityContainer;

/**
 * 
 */
public class AnalysisConfigurationSelectorField extends
  EntitySelectorField<AnalysisConfiguration> {

  public AnalysisConfigurationSelectorField(AuroraApplication application,
    EntityContainer<AnalysisConfiguration> container) {
    super(application, container);
  }

  @Override
  protected Class<AnalysisConfiguration> getEntityClass() {
    return AnalysisConfiguration.class;
  }

  @Override
  protected Object getEntityId(AnalysisConfiguration value) {
    return value.getId();
  }

  @Override
  protected String getFieldCaption() {
    return application.getApplicationMessages().getString(
      "AnalysisConfigurationSelectorField.field_caption");
  }

  @Override
  protected Object getItemCaptionPropertyId() {
    return "hdfsName";
  }

}
