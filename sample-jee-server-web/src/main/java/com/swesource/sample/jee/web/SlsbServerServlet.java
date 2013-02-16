package com.swesource.sample.jee.web;

import com.swesource.sample.jee.ServerSlsbLocal;
import com.swesource.sample.jee.ServerSlsbRemote;
import com.swesource.sample.jee.domain.User;

import javax.ejb.EJB;
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
@WebServlet(value = "/SlsbServerServlet")
public class SlsbServerServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SlsbServerServlet.class.getName());

    @EJB //(beanName = "ServerSlsbBean")
    private ServerSlsbLocal serverSlsbBean;

    //@EJB
    //private ServerSlsbLocal serverSlsRemoteInjectedBean;

    private ServerSlsbRemote serverSlsbLocalBean;
    private ServerSlsbRemote serverSlsbRemoteBean;

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

        String serverSlsbPath = "ejb:sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT//ServerSlsbBean!com.swesource.sample.jee.ServerSlsbRemote";
        try {
            serverSlsbRemoteBean = (ServerSlsbRemote) context.lookup(serverSlsbPath);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error looking up ServerSlsbBean", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();

        // Call the server bean by remote interface
        String greeting = invokeServerRemoteBean();
        pw.write("<p>SlsbServerServlet -> ServerSlsbBean.sayHello (remote interface lookup from same EAR) -> [" + greeting + "]</p>\n");

        // Call the server bean by @EJB-injection
        String greeting2 = invokeServerEjbInjectionBean();
        pw.write("<p>SlsbServerServlet -> ServerSlsbBean.sayHello (EJB-injection same EAR) -> [" + greeting2 + "]</p>\n");

        pw.flush();
        pw.close();
    }

    private String invokeServerRemoteBean() {
        String greeting = serverSlsbRemoteBean.sayHello();
        LOGGER.info("SlsbServerServlet: Received greeting (by remote interface): " + greeting);
        String username = "Dude1";
        User user1 = new User();
        user1.setUsername(username);
        serverSlsbRemoteBean.persistUser(user1);
        User user2 = serverSlsbRemoteBean.findUserWithUsername(username);
        return greeting + " from " + user2.getUsername();
    }

    private String invokeServerEjbInjectionBean() {
        String greeting = serverSlsbBean.sayHello();
        LOGGER.info("SlsbServerServlet: Received greeting (by @EJB-injection): " + greeting);
        String username = "Dude2";
        User user1 = new User();
        user1.setUsername(username);
        serverSlsbRemoteBean.persistUser(user1);
        User user2 = serverSlsbBean.findUserWithUsername(username);
        return greeting + " from " + user2.getUsername();
    }
}