package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 *
 */
@Stateless
@Local(ServerSlsbLocal.class)
@Remote(ServerSlsbRemote.class)
@Clustered
public class ServerSlsbBean implements ServerSlsbLocal, ServerSlsbRemote, Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "samplePU")
    EntityManager em;

    @Override
    public String sayHello() {
        System.out.println("In ServerSlsbBean.sayHello()");
        return "Hello!";
    }
}
