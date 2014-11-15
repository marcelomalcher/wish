package br.rio.puc.lac.wish.web.app.ui.field.wish;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.field.EntitySelectorField;
import br.rio.puc.lac.wish.web.model.ContentsHdfsImport;

import com.vaadin.addon.jpacontainer.EntityContainer;

/**
 * 
 */
public class ContentsHdfsImportSelectorField extends
  EntitySelectorField<ContentsHdfsImport> {

  public ContentsHdfsImportSelectorField(AuroraApplication application,
    EntityContainer<ContentsHdfsImport> container) {
    super(application, container);
  }

  @Override
  protected Class<ContentsHdfsImport> getEntityClass() {
    return ContentsHdfsImport.class;
  }

  @Override
  protected Object getEntityId(ContentsHdfsImport value) {
    return value.getId();
  }

  @Override
  protected String getFieldCaption() {
    return application.getApplicationMessages().getString(
      "ContentsHdfsImportSelectorField.field_caption");
  }

  @Override
  protected Object getItemCaptionPropertyId() {
    return "name";
  }

}
