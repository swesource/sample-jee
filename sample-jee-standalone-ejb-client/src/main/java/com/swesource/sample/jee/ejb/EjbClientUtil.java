package com.swesource.sample.jee.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 *
 */
public class EjbClientUtil {

    protected EjbClientUtil() {
        super();
    }

    protected static Context createContext() throws NamingException {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        p.put(Context.PROVIDER_URL, "remote://localhost:4447");
        /* The tricky bit */
        p.put("jboss.naming.client.ejb.context", true);
        /* Add an "Application User" with $JBOSS_HOME/bin/add_user.sh */
        p.put(Context.SECURITY_PRINCIPAL, "admin");
        p.put(Context.SECURITY_CREDENTIALS, "admin2");
        return new InitialContext(p);
    }

}
