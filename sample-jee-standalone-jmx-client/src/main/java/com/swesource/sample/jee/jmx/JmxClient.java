package com.swesource.sample.jee.jmx;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * Sample JMX client
 * Enable JMX access on port 4447 with
 * <pre>
 * <subsystem xmlns="urn:jboss:domain:jmx:1.1">
 *     <show-model value="true"/>
 *     <remoting-connector  use-management-endpoint="false" />
 * </subsystem>
 * </pre>
 * @author Arnold Johansson
 */
public class JmxClient {

    public static void main(String[] argv) throws Exception {

        String host = "localhost";
        String port = "4447";
        String urlString ="service:jmx:remoting-jmx://" + host + ":" + port;
        JMXServiceURL jmxServiceURL = new JMXServiceURL(urlString);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
        MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
        ObjectName objectName=new ObjectName("swesource:service=ClientSlsbBean");
        Object parameters[] = {};
        String signatures[] = {};
        //mBeanServerConnection.invoke(objectName, "sayHello",  parameters, signature);
        Object response = mBeanServerConnection.invoke(objectName, "sayHello",  null, null);
        System.out.println(response);
        jmxConnector.close();
    }
}
