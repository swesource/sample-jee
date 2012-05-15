package com.swesource.sample.jee;

import javax.ejb.Remote;

/**
 *
 */
@Remote
public interface RelaySlsbRemote {
    public String called();
    public String callEjbInSameEar();
    public String callEjbInOtherAppSrv();
    public String callEjbInSameAppSrvButOtherEar();
}
