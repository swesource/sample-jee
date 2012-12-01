package com.swesource.sample.jee.ejb;

import com.swesource.sample.jee.ServerSfsbRemote;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class ServerSfsbClient {

    private static final int ERROR_EXIT_CODE = -1;
    private static final Logger LOGGER = Logger.getLogger(ServerSfsbClient.class.getName());

    private ServerSfsbClient() {
        super();
    }

    public static void main(String[] argv) {
        LOGGER.info("ServerSfsbClient starting.");
        Context jndiContext = null;
        try {
            jndiContext = EjbClientUtil.createContext();
        } catch(NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        /*
        Name-standard for remote ejb lookup:
        <applicationName>/<moduleName>/<beanName>!<fullyQualifiedNameOfBeansRemoteInterface>
         */
        ServerSfsbRemote sfsb = null;
        try {
            sfsb = (ServerSfsbRemote)jndiContext.lookup("sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT/ServerSfsbBean!com.swesource.sample.jee.ServerSfsbRemote");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        String response = sfsb.sayHello();
        try {
            jndiContext.close();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        LOGGER.info("ServerSfsbClient: " + response);
        LOGGER.info("ServerSfsbClient done.");
    }
}
