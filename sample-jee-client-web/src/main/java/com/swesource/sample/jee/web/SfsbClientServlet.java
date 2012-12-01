package com.swesource.sample.jee.web;

import com.swesource.sample.jee.ServerSfsbRemote;

import javax.naming.Context;
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
@WebServlet(value = "/SfsbClientServlet")
public class SfsbClientServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SfsbClientServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String greeting = null;
        try {
            greeting = invokeServerBean();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "", e);
            throw new ServletException(e);
        }
        PrintWriter pw = response.getWriter();
        pw.write("SfsbClientServlet -> SfsbOneBean.sayHello (in remote EAR) -> SfsbClientServlet -> [" + greeting + "]");
        pw.flush();
        pw.close();
    }

    private String invokeServerBean() throws NamingException {
        final Hashtable props = new Hashtable();
        // setup the ejb: namespace URL factory
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        // create the InitialContext
        Context context = new javax.naming.InitialContext(props);

        // Lookup the SfsbOne bean using the ejb: namespace syntax which is explained here
        // https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI

        // For remote ejbs (other container)
        String sfsbPath = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSfsbBean!com.swesource.sample.jee.ServerSfsbRemote?stateful";
        final ServerSfsbRemote serverSfsb = (ServerSfsbRemote) context.lookup(sfsbPath);

        // Call the sfsb server bean
        final String greeting = serverSfsb.sayHello();
        LOGGER.info("SfsbClientServlet: Received greeting: " + greeting);
        return greeting;
    }
}
