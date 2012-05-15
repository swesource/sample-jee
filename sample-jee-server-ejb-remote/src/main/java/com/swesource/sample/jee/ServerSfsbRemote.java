package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 *
 */
@Remote
public interface ServerSfsbRemote {
    public String sayHello();
}
