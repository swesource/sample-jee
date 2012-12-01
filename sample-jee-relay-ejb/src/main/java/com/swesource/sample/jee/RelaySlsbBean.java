package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.ProviderException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
@Stateless
@Remote(RelaySlsbRemote.class)
@Clustered
public class RelaySlsbBean implements RelaySlsbRemote {

    private static final Logger LOGGER = Logger.getLogger(RelaySlsbBean.class.getName());

    @PersistenceContext(unitName = "samplePU")
    private EntityManager em;

    public String called() {
        LOGGER.info("In RelaySlsbBean.called()");
        return "RelaySlsbBean.called";
    }

    public String callEjbInSameEar() {
        LOGGER.info("In RelaySlsbBean.callEjbInSameEar()");
        return "";
    }

    public String callEjbInOtherAppSrv() {
        LOGGER.info("In RelaySlsbBean.callEjbInOtherAppSrv()");
        return "";
    }

    public String callEjbInSameAppSrvButOtherEar() {
        LOGGER.info("In RelaySlsbBean.callEjbInSameAppSrvButOtherEar()");
        ServerSlsbRemote bean = lookupBean();
        final String callResponse = bean.sayHello();
        LOGGER.info("RalaySlsbBean: Received call response: " + callResponse);
        return callResponse;
    }

    private ServerSlsbRemote lookupBean() {
        final Hashtable props = new Hashtable();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            final Context context = new javax.naming.InitialContext(props);
             String path = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote";
             return (ServerSlsbRemote)context.lookup(path);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error looking up ServerSlsbBean");
            throw new ProviderException(e);
        }
    }

    /* dummy method to avoid code metrics errors awaiting real implementation */
    public void persist() {
        em.persist(new Object());
    }
}
