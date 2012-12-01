package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 *
 */
@Remote
public interface RelaySlsbRemote {
    String called();
    String callEjbInSameEar();
    String callEjbInOtherAppSrv();
    String callEjbInSameAppSrvButOtherEar();
}
