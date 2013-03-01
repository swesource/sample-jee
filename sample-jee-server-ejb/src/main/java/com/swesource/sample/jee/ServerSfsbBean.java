package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * SFSB with persistence.
 * @author Arnold Johansson
 */

@Stateful
@Local(ServerSfsbLocal.class)
@Remote(ServerSfsbRemote.class)
@Clustered
public class ServerSfsbBean implements ServerSfsbLocal, ServerSfsbRemote, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ServerSfsbBean.class.getName());

    @PersistenceContext(unitName = "samplePU")
    private EntityManager em;

    @Override
    public String sayHello() {
        LOGGER.info("In ServerSfsbBean.sayHello()");
        return "Hello!";
    }

    /* dummy method to avoid code metrics errors awaiting real implementation */
    public void persist() {
        em.persist(new Object());
    }
}
