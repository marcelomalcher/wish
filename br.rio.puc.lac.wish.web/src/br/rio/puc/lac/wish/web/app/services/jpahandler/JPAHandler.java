package br.rio.puc.lac.wish.web.app.services.jpahandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.tecnoinf.aurora.app.services.IJPAHandler;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class JPAHandler implements IJPAHandler {

  private EntityManagerFactory factory;

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    if (factory == null) {
      factory =
        Persistence.createEntityManagerFactory("br.rio.puc.lac.wish.web");
    }
    return factory;
  }
}
