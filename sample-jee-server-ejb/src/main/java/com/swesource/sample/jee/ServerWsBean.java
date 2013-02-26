package com.swesource.sample.jee;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * @author arnie
 */
@Stateless
//@Remote(ServerWsEndpoint.class)
@WebService(
        //name = "ServerWs"
        //serviceName = "ServerWebServiceBean",
        endpointInterface = "com.swesource.sample.jee.ServerWsEndpoint")
public class ServerWsBean implements ServerWsEndpoint {

    @Override
    public String wsSimple() {
        return "Return from wsSimple()";
    }

    @Override
    public void wsSimple1Way() {
        System.out.println("Executing wsSimple1Way()");
    }

    @Override
    public String wsWithParams(
            String idParamAsString,
            Integer idParamAsInteger) {
        return "Return from wsWithParams()";
    }

    @Override
    public String wsWithReturn() {
        return "Return from wsWithReturn()";
    }
}
