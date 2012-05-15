package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
@Stateless
//@Local(ClientSlsbLocal.class)
@Remote(ClientSlsbRemote.class)
@Clustered
public class ClientSlsbBean implements /*SlsbOneLocal,*/ ClientSlsbRemote {

    @PersistenceContext(unitName = "samplePU")
    EntityManager em;

    public String called() {
        System.out.println("In ClientSlsbBean.called()");
        return "";
    }

    public String callEjbInSameEar() {
        System.out.println("In ClientSlsbBean.callEjbInSameEar()");
        return "";
    }

    public String callEjbInOtherAppSrv() {
        System.out.println("In ClientSlsbBean.callEjbInOtherAppSrv()");
        return "";
    }

    public String callEjbInSameAppSrvButOtherEar() {
        System.out.println("In ClientSlsbBean.callEjbInSameAppSrvButOtherEar()");
        return "";
    }
}
