package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 *
 */
@Remote
public interface ServerSlsbRemote {
    public String sayHello();
}
