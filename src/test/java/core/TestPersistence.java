/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author
 */
@RunWith(Arquillian.class)
public class TestPersistence {
    
    @Inject
    Forum forum;
    
    @Inject
    UserTransaction utx;
    
    @PersistenceContext(unitName = "jpa_forum_test_pu")
    @Produces
    @Default
    EntityManager em;
    
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "forum.war")
                .addPackage("core")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void before() throws Exception {
        clearAll();
    }
    
    private void clearAll() throws Exception {  
        utx.begin();  
        em.joinTransaction();
        em.createQuery("delete from CourseGroup").executeUpdate();
        em.createQuery("delete from GroupUser").executeUpdate();
        em.createQuery("delete from Course").executeUpdate();
        
        utx.commit();
    }
    
    @Test
    public void testTester() {
        //Will always fail to check if the testing is working.
        //assertTrue(1 == 2);
    }
    
    @Test
    public void testPersistAUser() {
        GroupUser u = new GroupUser(1234L, "aaa@a.a", "pwd");
        forum.getUserList().create(u);
        List<GroupUser> us = forum.getUserList().findAll();
        assertTrue(us.size() > 0);
        assertTrue(us.get(0).getSsnbr() == u.getSsnbr());
    }
    
    public void testPersistAGroup(){
        
    }
    
    public void testPersistACourse(){
        
    }
    
    
    public void testUserDelete(){
        
    }
    
    public void testGroupDelete(){
        
    }
    
    public void testCourseDelete(){
        
    }
    
    public void testGroupGetByName(){
        
    }
    
    public void testCourseGetByCC(){
        
    }
    
    @Test
    public void testUserGetBySsnbr() {
        Long ssnbr = 1234L;
        GroupUser u = new GroupUser(ssnbr, "aaa@a.a", "pwd");
        forum.getUserList().create(u);
        GroupUser u2 = forum.getUserList().getBySsnbr(ssnbr);
        assertTrue(u2.getSsnbr() == u.getSsnbr());
    }
}

