/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import cds.core.Forum;
import cds.core.Course;
import cds.core.CourseGroup;
import cds.core.GroupUser;
import java.util.ArrayList;
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
    
    @Test
    public void testPersistACourse(){
        Course c = new Course("1234", "testCourse");
        forum.getCourseList().create(c);
        Course c2 = forum.getCourseList().getByCC(c.getCCode());
        assertTrue(c2.getCCode().equals(c.getCCode()));
                
    }
    
    @Test
    public void testPersistAGroup(){
        Course c = new Course("DAT076", "Web-applikationer");
        forum.getCourseList().create(c);
        CourseGroup g = new CourseGroup(c, "Code Dark");
        forum.getGroupList().create(g);
        List<CourseGroup> gs = forum.getGroupList().getByCourse(c);
        assertTrue(gs.size() > 0);
        assertTrue(gs.get(0).getCourse().equals(c));
    }

    public void testUserDelete(){
        GroupUser u = new GroupUser(1234L, "aaa@a.a", "pwd");
        forum.getUserList().create(u);
        assertTrue(forum.getUserList().count() == 1);
        forum.getUserList().delete(u.getId());
        assertTrue(forum.getUserList().count() == 0);
    }
    
    @Test
    public void testGroupDelete(){
        Course c = new Course("DAT999", "Testkursen");
        forum.getCourseList().create(c);
        CourseGroup u = new CourseGroup(c, "Testgrupp");
        forum.getGroupList().create(u);
        assertTrue(forum.getGroupList().count() == 1);
        forum.getGroupList().delete(u.getId());
        assertTrue(forum.getGroupList().count() == 0);
    }
    
    @Test
    public void testCourseDelete(){
        Course c = new Course("TST999", "Testkurs");
        forum.getCourseList().create(c);
        assertTrue(forum.getCourseList().count() == 1);
        System.out.println(c.getId());
        forum.getCourseList().delete(c.getId());
        assertTrue(forum.getCourseList().count() == 0);
    }
    
    @Test
    public void testGroupGetByName(){
        Course c = new Course("DAT999", "Testkursen");
        forum.getCourseList().create(c);
        CourseGroup u = new CourseGroup(c, "Testgrupp");
        forum.getGroupList().create(u);
        
        CourseGroup u1 = forum.getGroupList().getByName("Testgrupp");
        
        assertTrue(u.equals(u1));
    }
    
    @Test
    public void testCourseGetByCC(){
        Course c = new Course("DAT999", "Testkursen");
        forum.getCourseList().create(c);
        
        Course c1 = forum.getCourseList().getByCC("DAT999");
        assertTrue(c.equals(c1));
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

