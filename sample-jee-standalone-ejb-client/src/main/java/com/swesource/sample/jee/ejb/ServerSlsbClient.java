package com.swesource.sample.jee.ejb;

import com.swesource.sample.jee.ServerSlsbRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 *
 */
public class ServerSlsbClient {

    public static void main(String[] argv) throws Exception {
        System.out.println("ServerSlsbClient starting.");
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        p.put(Context.PROVIDER_URL, "remote://localhost:4447");
        p.put("jboss.naming.client.ejb.context", true);  // <-- The tricky bit!
        p.put(Context.SECURITY_PRINCIPAL, "admin");      // Add an "Application User" with $JBOSS_HOME/bin/add_user.sh
        p.put(Context.SECURITY_CREDENTIALS, "admin2");
        Context jndiContext = new InitialContext(p);
        // Name-standard for remote ejb lookup:
        // <applicationName>/<moduleName>/<beanName>!<fullyQualifiedNameOfBeansRemoteInterface>
        ServerSlsbRemote slsb = (ServerSlsbRemote)jndiContext.lookup("sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT/ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote");
        String response = slsb.sayHello();
        jndiContext.close();

        System.out.println("ServerSlsbClient: " + response);
        System.out.println("ServerSlsbClient done.");
    }
}
