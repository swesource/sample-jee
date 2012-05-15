package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
@Stateless
@Local(ServerSlsbLocal.class)
@Remote(ServerSlsbRemote.class)
@Clustered
public class ServerSlsbBean implements ServerSlsbLocal, ServerSlsbRemote {

    @PersistenceContext(unitName = "samplePU")
    EntityManager em;

    @Override
    public String sayHello() {
        System.out.println("In ServerSlsbBean.sayHello()");
        return "Hello!";
    }
}
