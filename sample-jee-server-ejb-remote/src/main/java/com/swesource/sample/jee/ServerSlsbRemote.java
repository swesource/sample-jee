package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.User;

import javax.ejb.Remote;

/**
 * Remote interface to com.swesource.sample.jee.ServerSlsbBean
 * @author Arnold Johansson
 */
@Remote
public interface ServerSlsbRemote {
    String sayHello();
    void persistUser(User u);
    User findUserWithUsername(String username);
}
