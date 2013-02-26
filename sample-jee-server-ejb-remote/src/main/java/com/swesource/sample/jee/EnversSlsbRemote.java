package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.Person;
import com.swesource.sample.jee.domain.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * Remote interface to com.swesource.sample.jee.EnversSlsbSlsbBean
 * @author Arnold Johansson
 */
@Remote
public interface EnversSlsbRemote {
    public void persistPerson(Person person);
    public Person findPerson(Long id);
    public List<Person> findByRevisionAndLastName(int revision, String propertyName, String propertyValue);
    public Person updatePerson(Person person);
}
