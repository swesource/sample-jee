package com.swesource.sample.jee.web;

import com.swesource.sample.jee.RelaySlsbRemote;
import com.swesource.sample.jee.ServerSlsbRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 *
 */
@WebServlet(value = "/SlsbClientServlet")
public class SlsbClientServlet extends HttpServlet {

    private ServerSlsbRemote serverSlsbProxy;
    private RelaySlsbRemote relaySlsbProxy;

    @Override
    public void init() throws ServletException {
        try {
            final Hashtable properties = new Hashtable();
            properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            final Context context = new InitialContext(properties);

            // Lookup the beans using the ejb: namespace syntax which is explained here
            // https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI

            String serverSlsbPath = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote";
            serverSlsbProxy = (ServerSlsbRemote) context.lookup(serverSlsbPath);

            String relaySlsbPath = "ejb:sample-jee-relay/sample-jee-relay-ejb-1.0-SNAPSHOT//RelaySlsbBean!com.swesource.sample.jee.RelaySlsbRemote";
            relaySlsbProxy = (RelaySlsbRemote) context.lookup(relaySlsbPath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String greeting = invokeOnServerBean();
        PrintWriter pw = response.getWriter();
        pw.write("SlsbClientServlet -> ServerSlsbBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + greeting + "]\n");
        //String callResponse = invokeOnRelayBean();
        //pw.write("SlsbClientServlet -> SlsbRelayBean.called (in remote EAR) -> SlsbOneBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + callResponse + "]\n");
        pw.flush();
        pw.close();
    }

    private String invokeOnServerBean() {
        String greeting = serverSlsbProxy.sayHello();
        System.out.println("SlsbClientServlet: Received greeting: " + greeting);
        return greeting;
    }

    private String invokeOnRelayBean() {
        final String callResponse = relaySlsbProxy.callEjbInSameAppSrvButOtherEar();
        System.out.println("SlsbClientServlet: Received call response: " + callResponse);
        return callResponse;
    }
}