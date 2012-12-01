package com.swesource.sample.jee;

import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * REST: The sayHello method is available from the URL
 * http://localhost:8080/sample-jee-client/rest/clientslsb/sayhello
 */
@Path("/clientslsb")
@Stateless
//@Local(ClientSlsbLocal.class)
@Remote(ClientSlsbRemote.class)
@Clustered
public class ClientSlsbBean implements ClientSlsbRemote, ClientSlsbBeanMBean, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ClientSlsbBean.class.getName());

    @Override
    public String called() {
        LOGGER.info("In ClientSlsbBean.called()");
        return "";
    }

    @Override
    public String callEjbInSameEar() {
        LOGGER.info("In ClientSlsbBean.callEjbInSameEar()");
        return "";
    }

    @Override
    public String callEjbInOtherAppSrv() {
        LOGGER.info("In ClientSlsbBean.callEjbInOtherAppSrv()");
        return "";
    }

    @Override
    public String callEjbInSameAppSrvButOtherEar() {
        LOGGER.info("In ClientSlsbBean.callEjbInSameAppSrvButOtherEar()");
        return "";
    }

    @Override
    @GET
    @Path("/sayhello")
    @Produces("text/plain")
    public String sayHello() {
        LOGGER.info("In ClientSlsbBean.sayHello()");
        return "Hello!";
    }

    @Override
    @GET
    @Path("/sayhello/{name}")
    @Produces("text/plain")
    public String sayHello(@PathParam("name") String name) {
        LOGGER.info("In ClientSlsbBean.sayHello(String)");
        return "Hello " + name + "!";
    }

}
