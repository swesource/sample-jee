package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.User;
import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * SLSB with persistence.
 * @author Arnold Johansson
 */
@Stateless
@Local(ServerSlsbLocal.class)
@Remote(ServerSlsbRemote.class)
@Clustered
public class ServerSlsbBean implements ServerSlsbLocal, ServerSlsbRemote, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ServerSlsbBean.class.getName());

    @PersistenceContext(unitName = "samplePU")
    private EntityManager em;

    @Override
    public String sayHello() {
        LOGGER.info("In ServerSlsbBean.sayHello()");
        return "Hello!";
    }

    public void persistUser(User u) {
        em.persist(u);
    }

    public User findUserWithUsername(String username) {
        Query query = em.createNamedQuery("findUserWithUsername");
        query.setParameter("username", username);
        return (User)query.getSingleResult();
    }
}
