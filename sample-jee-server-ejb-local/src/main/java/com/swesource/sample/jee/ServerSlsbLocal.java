package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.User;

import javax.ejb.Local;

/**
 * Local interface to com.swesource.sample.jee.ServerSlsbBean
 * @author Arnold Johansson

 */
@Local
public interface ServerSlsbLocal {
    String sayHello();
    void persistUser(User u);
    User findUserWithUsername(String username);
}
