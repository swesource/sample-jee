package com.swesource.sample.jee.web;

import com.swesource.sample.jee.RelaySlsbRemote;
import com.swesource.sample.jee.ServerSlsbRemote;

import javax.naming.Context;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        invokeOnBean();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String greeting = invokeOnBean();
        PrintWriter pw = response.getWriter();
        pw.write("SlsbClientServlet -> ServerSlsbBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + greeting + "]\n");
        //String callResponse = invokeOnRelayBean();
        //pw.write("SlsbClientServlet -> SlsbRelayBean.called (in remote EAR) -> SlsbOneBean.sayHello (in remote EAR) -> SlsbClientServlet -> [" + callResponse + "]\n");
        pw.flush();
        pw.close();
    }

    private String invokeOnBean() {
        try {
            final Hashtable props = new Hashtable();
            // setup the ejb: namespace URL factory
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            // create the InitialContext
            final Context context = new javax.naming.InitialContext(props);

            // Lookup the SlsbOne bean using the ejb: namespace syntax which is explained here
            // https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI
            
            // For remote ejbs (other container)
            String path = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote";
            final ServerSlsbRemote bean = (ServerSlsbRemote) context.lookup(path);
            
            // invoke on the bean
            final String greeting = bean.sayHello();
            System.out.println("SlsbClientServlet: Received greeting: " + greeting);
            return greeting;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String invokeOnRelayBean() {
        try {
            final Hashtable props = new Hashtable();
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            final Context context = new javax.naming.InitialContext(props);
            String path = "ejb:sample-jee-relay/sample-jee-relay-ejb-1.0-SNAPSHOT//RelaySlsbBean!com.swesource.sample.jee.RelaySlsbRemote";
            final RelaySlsbRemote bean = (RelaySlsbRemote) context.lookup(path);

            // invoke on the bean
            final String callResponse = bean.callEjbInSameAppSrvButOtherEar();
            System.out.println("SlsbClientServlet: Received call response: " + callResponse);
            return callResponse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}