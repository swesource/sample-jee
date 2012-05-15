package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Hashtable;

/**
 *
 */
@Stateless
@Remote(RelaySlsbRemote.class)
@Clustered
public class RelaySlsbBean implements RelaySlsbRemote {

    @PersistenceContext(unitName = "samplePU")
    EntityManager em;

    public String called() {
        System.out.println("In RelaySlsbBean.called()");
        return "RelaySlsbBean.called";
    }

    public String callEjbInSameEar() {
        System.out.println("In RelaySlsbBean.callEjbInSameEar()");
        return "";
    }

    public String callEjbInOtherAppSrv() {
        System.out.println("In RelaySlsbBean.callEjbInOtherAppSrv()");
        return "";
    }

    public String callEjbInSameAppSrvButOtherEar() {
        System.out.println("In RelaySlsbBean.callEjbInSameAppSrvButOtherEar()");
        return invokeOnBean();
    }

    private String invokeOnBean() {
        try {
            final Hashtable props = new Hashtable();
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            final Context context = new javax.naming.InitialContext(props);
            String path = "ejb:sample-jee/sample-jee-ejb-1.0-SNAPSHOT//SlsbOneBean!com.swesource.sample.jee.ServerSlsbRemote";
            final ServerSlsbRemote bean = (ServerSlsbRemote) context.lookup(path);

            // invoke on the bean
            final String callResponse = bean.sayHello();
            System.out.println("RalaySlsbBean: Received call response: " + callResponse);
            return callResponse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
