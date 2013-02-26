package com.swesource.sample.jee;

import javax.ejb.Remote;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author arnie
 */
@Remote
@WebService
// Suppressing warnings because IDE (InterlliJ IDEA 12.0.3)
// erroneous complains about java.rmi.Remote
@SuppressWarnings(value = "all")
public interface ServerWsEndpoint {

    @WebMethod
    public String wsSimple();

    @WebMethod
    @Oneway
    public void wsSimple1Way();

    @WebMethod
    public String wsWithParams(
            @WebParam(name = "idParamAsString") String idParamAsString,
            @WebParam(name = "idParamAsInteger")Integer idParamAsInteger);

    @WebMethod
    @WebResult
    public String wsWithReturn();

}