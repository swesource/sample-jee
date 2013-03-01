package com.swesource.sample.jee;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * @author arnie
 */
@Stateful
@RequestScoped
public class EntityManagerResourceProducer {

    @PersistenceContext(unitName = "samplePU", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Produces
    public EntityManager getEm() {
        return em;
    }

    /* Other useful resources... */
    /*
    import javax.faces.context.FacesContext;
    @Produces
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance()
    }

    import javax.enterprise.inject.spi.InjectionPoint;
    import java.util.logging.Logger;
    @Produces
    public Logger getLogger(InjectionPoint injectionPoint) {
        String category = injectionPoint.getMember().getDeclaringClass().getName();
        return Logger.getLogger(category);
    }
    */
}
