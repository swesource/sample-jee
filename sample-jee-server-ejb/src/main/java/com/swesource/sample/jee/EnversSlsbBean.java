package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.Person;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

/**
 * @author arnie
 */
@Stateless
@Remote(EnversSlsbRemote.class)
public class EnversSlsbBean implements EnversSlsbRemote {

    private static final Logger LOGGER = Logger.getLogger(EnversSlsbBean.class.getName());

    @PersistenceContext(unitName = "samplePU")
    private EntityManager em;

    public void persistPerson(Person person) {
        em.persist(person);
    }

    public Person findPerson(Long id) {
        System.out.println("inside");
        Person p = em.find(Person.class, id);
        System.out.println("outside: " + p.getId());
        return p;
    }

    public Person updatePerson(Person person) {
        return em.merge(person);
    }

    /*
    public List<Person> findPersonVersionsById(Long id) {
        Query query = em.createNamedQuery("findPersonVersionsById");
        query.setParameter("id", id);
        return query.getResultList();
    }
    */

    public List<Person> findByRevisionAndLastName(int revision, String propertyName, String propertyValue) {
        AuditReader auditReader = AuditReaderFactory.get(em);
        AuditQuery query = auditReader.createQuery().forEntitiesAtRevision(Person.class, revision);
        //query.add(AuditEntity.revisionProperty(propertyName).eq(propertyValue));
        query.add(AuditEntity.property(propertyName).eq(propertyValue));
        List<Person> persons = query.getResultList();
        return persons;
    }
}
