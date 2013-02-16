package com.swesource.sample.jee;

/**
 * JMX MBean interface for the @see ClientSlsbBean
 * @author Arnold Johansson
 */
public interface ClientSlsbBeanMBean {
    String sayHello();
    String sayHello(String name);
}
