package br.rio.puc.lac.wish.web.app.ui.field.wish;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import br.com.tecnoinf.aurora.app.AbstractModuleComponent;
import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.field.TableSelectBeanField;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.ContentsHdfsImport;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.ui.TableFieldFactory;

public class ContentsHdfsImportSelectField extends
  TableSelectBeanField<Integer, ContentsHdfsImport> {

  public ContentsHdfsImportSelectField(AuroraApplication application,
    AbstractModuleComponent moduleComponent) {
    super(application, moduleComponent);
  }

  @Override
  protected Class<ContentsHdfsImport> getEntityClass() {
    return ContentsHdfsImport.class;
  }

  @Override
  protected Object[] getVisibleColumns() {
    return new Object[] { "queryBeginDate", "queryEndDate", "hdfsPath",
        "querySocialSource" };
  }

  @Override
  protected String[] getNestedProperties() {
    return null;
  }

  @Override
  protected TableFieldFactory getTableFieldFactory() {
    return null;
  }

  @Override
  protected Collection<ContentsHdfsImport> createBeanCollection() {
    return new HashSet<ContentsHdfsImport>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getType() {
    return HashSet.class;
  }

  @Override
  protected Object[] getSelectVisibleColumns() {
    return new Object[] { "id", "configuration.hdfsName",
        "configuration.mapRedJobTracker", "queryBeginDate", "queryEndDate",
        "hdfsPath", "querySocialSource", "importMessage" };
  }

  @Override
  protected String getSelectTitle() {
    return application.getApplicationMessages().getString(
      "ContentsHdfsImportSelectField.select_title");
  }

  @Override
  protected EntityContainer<ContentsHdfsImport> getSelectEntityContainer() {
    JPAContainer<ContentsHdfsImport> container =
      AuroraJPAContainerFactory.make(ContentsHdfsImport.class, application
        .getJPAHandler().getEntityManagerFactory().createEntityManager());
    container.addContainerFilter(new Equal("importStatus", 1)); //TODO
    container.applyFilters();
    return container;
  }

  @Override
  protected String[] getSelectNestedProperties() {
    return new String[] { "configuration.hdfsName",
        "configuration.mapRedJobTracker" };
  }

  @Override
  protected String[] getSelectSearchProperties() {
    return new String[] { "configuration.hdfsName",
        "configuration.mapRedJobTracker", "queryBeginDate", "queryEndDate",
        "hdfsPath", "querySocialSource" };
  }

  @Override
  protected BeanIdResolver<Integer, ContentsHdfsImport> getBeanIdResolver() {
    return new BeanIdResolver<Integer, ContentsHdfsImport>() {

      @Override
      public Integer getIdForBean(ContentsHdfsImport bean) {
        return bean.getId();
      }
    };
  }

  @Override
  protected Map<Object, String> getBeanCaptions() {
    return null;
  }

  @Override
  protected Map<Object, String> getSelectTableHeaders() {
    return null;
  }

  @Override
  public Object getValue() {
    ContentsHdfsImport contentsImport = null;
    Collection<ContentsHdfsImport> beans = createBeanCollection();
    for (Object itemId : container.getItemIds()) {
      contentsImport = container.getItem(itemId).getBean();
      break;
    }
    return contentsImport;
  }

}