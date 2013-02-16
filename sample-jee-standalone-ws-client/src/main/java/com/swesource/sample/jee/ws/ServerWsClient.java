package com.swesource.sample.jee.ws;

import com.swesource.sample.jee.ServerWsEndpoint;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * @author arnie
 */
public class ServerWsClient {

    public static void main(String[] argv) throws Exception {
        URL url = new URL("http://localhost:8080/sample-jee-server-ejb-1.0-SNAPSHOT/ServerWsBean?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://jee.sample.swesource.com/", "ServerWsBeanService");
        Service service = Service.create(url, qname);
        ServerWsEndpoint ws = service.getPort(ServerWsEndpoint.class);
        System.out.println(ws.wsSimple());
    }
}
