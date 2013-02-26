package com.swesource.sample.jee.ejb;

import com.swesource.sample.jee.EnversSlsbRemote;
import com.swesource.sample.jee.domain.Address;
import com.swesource.sample.jee.domain.Person;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author arnie
 */
public class EnversClient {

    private static final int ERROR_EXIT_CODE = -1;
    private static final Logger LOGGER = Logger.getLogger(EnversClient.class.getName());

    public static void main(String[] argv) {

        LOGGER.info("EnversClient starting.");

        Context jndiContext = null;
        try {
            jndiContext = EjbClientUtil.createContext();
        } catch(NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        /*
        Name-standard for remote ejb lookup:
        <applicationName>/<moduleName>/<beanName>!<fullyQualifiedNameOfBeansRemoteInterface>
         */
        EnversSlsbRemote slsb = null;
        try {
            slsb = (EnversSlsbRemote)jndiContext.lookup("sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT/EnversSlsbBean!com.swesource.sample.jee.EnversSlsbRemote");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }

        LOGGER.info("EnversSlsbClient creating objects.");
        Address address1 = new Address();
        address1.setStreet("MyStreet 2");
        address1.setZip("12345");
        address1.setCity("Town");
        address1.setCountry("SE");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.JANUARY, 1);
        address1.setMovingInDate(calendar.getTime());
        Address address2 = new Address();
        address2.setStreet("OtherStreet 1");
        address2.setZip("67890");
        address2.setCity("Area");
        address2.setCountry("SE");
        calendar.set(2013, Calendar.JANUARY, 2);
        address2.setMovingInDate(calendar.getTime());
        Collection<Address> addresses = new ArrayList<Address>();
        addresses.add(address1);
        addresses.add(address2);

        Person personA = new Person();
        personA.setFirstName("Adam");
        personA.setLastName("East");
        personA.setAddresses(addresses);

        LOGGER.info("EnversSlsbClient persisting objects.");
        slsb.persistPerson(personA);
        personA = null;

        Person personX = slsb.findPerson(1L);
        personX.setLastName("West");
        LOGGER.info("EnversSlsbClient updating objects.");
        Person personY = slsb.updatePerson(personX);
        personX = null;
        personY = null;

        LOGGER.info("EnversSlsbClient looking for versioned objects.");
        List<Person> persons = slsb.findByRevisionAndLastName(2, "lastName", "West");
        persons = null;

        try {
            jndiContext.close();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }
        LOGGER.info("EnversSlsbClient done.");
    }
}
