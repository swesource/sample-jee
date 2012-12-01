package com.swesource.sample.jee.web;

import com.swesource.sample.jee.RelaySlsbRemote;
import com.swesource.sample.jee.ServerSlsbRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
@WebServlet(value = "/SlsbClientServlet")
public class SlsbClientServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SlsbClientServlet.class.getName());

    private ServerSlsbRemote serverSlsbProxy;
    private RelaySlsbRemote relaySlsbProxy;

    @Override
    public void init() throws ServletException {
        final Hashtable properties = new Hashtable();
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context;
        try {
            context = new InitialContext(properties);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error creating initial context", e);
            throw new ServletException(e);
        }

        // Lookup the beans using the ejb: namespace syntax which is explained here
        // https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI

        String relaySlsbPath = "ejb:sample-jee-relay/sample-jee-relay-ejb-1.0-SNAPSHOT//RelaySlsbBean!com.swesource.sample.jee.RelaySlsbRemote";
        try {
            relaySlsbProxy = (RelaySlsbRemote) context.lookup(relaySlsbPath);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error looking up RelaySlsbBean", e);
            throw new ServletException(e);
        }

        String serverSlsbPath = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote";
        try {
            serverSlsbProxy = (ServerSlsbRemote) context.lookup(serverSlsbPath);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error looking up ServerSlsbBean", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();

        // Call the relay bean
        String callResponse = invokeRelayBean();
        pw.write("SlsbClientServlet -> SlsbRelayBean.called (in remote EAR) -> SlsbOneBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + callResponse + "]\n");

        // Call the server bean
        String greeting = invokeServerBean();
        pw.write("SlsbClientServlet -> ServerSlsbBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + greeting + "]\n");

        pw.flush();
        pw.close();
    }

    private String invokeServerBean() {
        String greeting = serverSlsbProxy.sayHello();
        LOGGER.info("SlsbClientServlet: Received greeting: " + greeting);
        return greeting;
    }

    private String invokeRelayBean() {
        final String callResponse = relaySlsbProxy.callEjbInSameAppSrvButOtherEar();
        LOGGER.info("SlsbClientServlet: Received call response: " + callResponse);
        return callResponse;
    }
}