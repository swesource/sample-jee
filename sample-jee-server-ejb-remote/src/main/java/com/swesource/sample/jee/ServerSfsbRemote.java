package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 * Remote interface to com.swesource.sample.jee.ServerSfsbBean
 * @author Arnold Johansson
 */
@Remote
public interface ServerSfsbRemote {
    String sayHello();
}
