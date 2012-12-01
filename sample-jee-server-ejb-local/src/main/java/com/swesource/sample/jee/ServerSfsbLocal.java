package com.swesource.sample.jee;

import javax.ejb.Local;

/**
 * Local interface to com.swesource.sample.jee.ServerSfsbBean
 * @author Arnold Johansson
 */
@Local
public interface ServerSfsbLocal {
    String sayHello();
}
