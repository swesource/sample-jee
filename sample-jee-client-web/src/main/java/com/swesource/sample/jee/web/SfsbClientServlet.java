package com.swesource.sample.jee.web;

import com.swesource.sample.jee.ServerSfsbRemote;

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
@WebServlet(value = "/SfsbClientServlet")
public class SfsbClientServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        invokeOnBean();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String greeting = invokeOnBean();
        PrintWriter pw = response.getWriter();
        pw.write("SfsbClientServlet -> SfsbOneBean.sayHello (in remote EAR) -> SfsbClientServlet -> [" + greeting + "]");
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

            // Lookup the SfsbOne bean using the ejb: namespace syntax which is explained here
            // https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI
            
            // For remote ejbs (other container)
            String path = "ejb:sample-jee/sample-jee-ejb-1.0-SNAPSHOT//SfsbOneBean!com.swesource.sample.jee.ServerSfsbRemote?stateful";
            final ServerSfsbRemote bean = (ServerSfsbRemote) context.lookup(path);

            /*
            Use cases:
            - Remote EAR, remote container: java-ejb-client.xml
            - Remote EAR, same container  :
            - Same EAR (=same container)  : @Inject or @EJB
            */

            //final ServerSlsbRemote bean = (ServerSlsbRemote)
            //        context.lookup("java:global/sample-jee/sample-jee-ejb-1.0-SNAPSHOT/SlsbOneBean!" +
            //        com.swesource.sample.jee.ServerSlsbRemote.class.getName());

            // invoke on the bean
            final String greeting = bean.sayHello();
            System.out.println("SfsbClientServlet: Received greeting: " + greeting);
            return greeting;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
