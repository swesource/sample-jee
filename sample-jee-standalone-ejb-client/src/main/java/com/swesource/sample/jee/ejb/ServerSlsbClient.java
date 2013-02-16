package com.swesource.sample.jee.ejb;

import com.swesource.sample.jee.ServerSlsbRemote;
import com.swesource.sample.jee.domain.User;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class ServerSlsbClient {

    private static final int ERROR_EXIT_CODE = -1;
    private static final Logger LOGGER = Logger.getLogger(ServerSlsbClient.class.getName());

    private ServerSlsbClient() {
        super();
    }

    public static void main(String[] argv) {
        LOGGER.info("ServerSlsbClient starting.");
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
        ServerSlsbRemote slsb = null;
        try {
            slsb = (ServerSlsbRemote)jndiContext.lookup("sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT/ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        String response = slsb.sayHello();

        String username = "Dude";
        User user1 = new User();
        user1.setUsername(username);
        slsb.persistUser(user1);
        User user2 = slsb.findUserWithUsername(username);

        try {
            jndiContext.close();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        LOGGER.info("ServerSlsbClient says: " + response);
        LOGGER.info("ServerSlsbClient user: " + user2.getUsername());
        LOGGER.info("ServerSlsbClient done.");
    }
}
