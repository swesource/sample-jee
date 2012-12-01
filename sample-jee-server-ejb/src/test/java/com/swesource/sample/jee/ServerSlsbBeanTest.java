package com.swesource.sample.jee;

import com.swesource.sample.jee.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
//import javax.ejb.EJB;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

/**
 * Container tests of the ServerSlsbBean using Arquillian and JUnit.
 * @see com.swesource.sample.jee.ServerSlsbBean
 * @author Arnold Johansson
 */
@RunWith(Arquillian.class)
public class ServerSlsbBeanTest {

    @Deployment
    public static Archive<?> createDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(ServerSlsbLocal.class, ServerSlsbRemote.class,
                        ServerSlsbBean.class, ServerSlsbBeanTest.class, User.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
                .addAsResource("test-persistence.xml", "persistence.xml")
                .addAsApplicationResource("jbossas-ds.xml") //???
                .addAsLibrary(jar);
        return ear;
    }

    /*
    Using @EJB doesn't work in Arqullian yet... When it does use @EJB instead of @Inject.
    E g @EJB(mappedName = "java:global/sample-jee-server/sample-jee-server-ejb-1.0-SNAPSHOT/ServerSlsbBean!com.swesource.sample.jee.ServerSlsbLocal")
    or @EJB(lookup = "java:module/ServerSlsbBean!com.swesource.sample.jee.ServerSlsbLocal")
    See https://community.jboss.org/message/774762
    and https://issues.jboss.org/browse/ARQ-77
    and https://github.com/arquillian/arquillian-core/pull/22
    */
    @Inject
    //@EJB(mappedName = "java:module/ServerSlsbBean!com.swesource.sample.jee.ServerSlsbLocal")
    //@EJB(mappedName = "java:module/org/swesource/sample/jee/ServerSlsbBean")
    //@EJB(beanName = "ServerSlsbBean")
    ServerSlsbLocal bean;

    @Inject
    UserTransaction utx;

    @Test
    @InSequence(1)
    public void testDummy() {
        System.out.println("Starting testDummy...");
        assertTrue(true);
    }

    @Test
    @InSequence(2)
    public void testSayHello() {
        System.out.println("Starting testSayHello...");
        String value = bean.sayHello();
        System.out.println("value=" + value);
        Assert.assertEquals("Hello!", value);
    }

    @Test
    @InSequence(3)
    public void testPersistingUser() throws Exception {
        System.out.println("Starting testPersistingUser...");

        String username = "john";
        User u1 = new User();
        u1.setUsername(username);

        utx.begin();
        bean.persistUser(u1);
        utx.commit();

        utx.begin();
        User u2 = bean.findUserWithUsername(username);
        utx.commit();

        Assert.assertEquals(u2.getUsername(), u1.getUsername());
    }
}
