package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 *
 */
@Remote
public interface ClientSlsbRemote {
    String called();
    String callEjbInSameEar();
    String callEjbInOtherAppSrv();
    String callEjbInSameAppSrvButOtherEar();
    String sayHello();
    String sayHello(String name);
}
