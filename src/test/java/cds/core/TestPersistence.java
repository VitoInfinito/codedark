/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    
    private final static Logger log = Logger.getAnonymousLogger();
    //True enables all instances of log
    private final static boolean D = false;
    
    @PersistenceContext(unitName = "jpa_forum_test_pu")
    @Produces
    @Default
    EntityManager em;
    
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "forum.war")
                .addPackage("cds.core")
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
    // --- GROUPTESTS --- //
    @Test
    public void testGroupFindAllGroups(){
        Course c = new Course("DAT998", "Testkursen");
        forum.getCourseList().create(c);
        GroupUser user = new GroupUser("t", "a@a.se", "1", "test", "er");
        forum.getUserList().create(user);
        CourseGroup u = new CourseGroup(c, "Testgruppen", user, 2);
        forum.getGroupList().create(u);
        
        Course c2 = new Course("DAT997", "Testkursen1");
        forum.getCourseList().create(c2);
        GroupUser user2 = new GroupUser("t2", "a@a.se", "1", "test", "er");
        forum.getUserList().create(user2);
        CourseGroup u2 = new CourseGroup(c2, "Testgruppen2", user2, 2);
        forum.getGroupList().create(u2);
        
        assertTrue(forum.getGroupList().findAll().size() == 2);
    }
    @Test
    public void testGroupGetByNameAndCourse(){
        Course c = new Course("DAT998", "Testkursen2");
        forum.getCourseList().create(c);
        GroupUser user = new GroupUser("t", "a@a.se", "1", "test", "er");
        forum.getUserList().create(user);
        CourseGroup u = new CourseGroup(c, "Testgruppen", user, 2);
        if(D) log.log(Level.WARNING, u+"");
        forum.getGroupList().create(u);
        
        CourseGroup u1 = forum.getGroupList().getByNameAndCourse(u.getgName(), c.getId());
        if(D) log.log(Level.INFO, u + " ||| " + u1);
        assertTrue(u.equals(u1));
    }
    
    @Test
    public void testPersistAGroup(){
        Course c = new Course("DAT076", "Web-applikationer");
        forum.getCourseList().create(c);
        GroupUser user = new GroupUser("t", "a@a.se", "1", "test", "er");
        forum.getUserList().create(user);
        CourseGroup g = new CourseGroup(c, "Code Dark", user, 2);
        forum.getGroupList().create(g);
        CourseGroup g1 = forum.getGroupList().find(g.getId());
        assertTrue(g.equals(g1));
    }
    
    @Test
    public void testGroupDelete(){
        Course c = new Course("DAT999", "Testkursen");
        forum.getCourseList().create(c);
        GroupUser u = new GroupUser("tester", "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        CourseGroup g = new CourseGroup(c, "Testgrupp", u, 2);
        forum.getGroupList().create(g);
        assertNotNull(forum.getGroupList().find(g.getId()));
        forum.getGroupList().delete(g.getId());
        assertNull(forum.getGroupList().find(g.getId()));
    }
    
    @Test
    public void testGroupGetByCourse(){
        String cId = "DAT999";
        Course c = new Course(cId, "Testkursen");
        forum.getCourseList().create(c);
        GroupUser u = new GroupUser("tester", "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        CourseGroup g = new CourseGroup(c, "Testgrupp", u, 2);
        forum.getGroupList().create(g);
        int index = forum.getGroupList().getByCourse(cId).indexOf(g);
        CourseGroup g2 = forum.getGroupList().getByCourse(cId).get(index);
        assertTrue(g.equals(g2));
    }
    @Test
    public void testGroupGetByUser(){
        String uId = "tester";
        Course c = new Course("DAT999", "Testkursen");
        forum.getCourseList().create(c);
        GroupUser u = new GroupUser(uId, "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        CourseGroup g = new CourseGroup(c, "Testgrupp", u, 2);
        forum.getGroupList().create(g);
        int index = forum.getGroupList().getByUser(uId).indexOf(g);
        CourseGroup g2 = forum.getGroupList().getByUser(uId).get(index);
        assertTrue(g.equals(g2));
    }
    // --- USERTESTS --- //
    @Test
    public void testUserFindAll(){
        GroupUser u = new GroupUser("tester", "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        GroupUser u2 = new GroupUser("tester2", "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u2);
        assertTrue(forum.getUserList().findAll().size() == 2);
    }
    
    @Test
    public void testUserDelete(){
        GroupUser u = new GroupUser("tester", "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        assertTrue(forum.getUserList().find(u.getId()).equals(u));
        forum.getUserList().delete(u.getId());
        assertNull(forum.getUserList().find(u.getId()));
    }
    
    @Test
    public void testPersistAUser() {
        GroupUser u = new GroupUser("a" ,"aaa@a.a", "pwd", "fnmae", "lname");
        forum.getUserList().create(u);
        GroupUser u1 = forum.getUserList().find(u.getId());
        assertTrue(u.equals(u1));
    }
    
    @Test
    public void testUserGetByID() {
        String username = "tester";
        GroupUser u = new GroupUser(username, "aaa@a.a", "pwd", "a", "b");
        forum.getUserList().create(u);
        GroupUser u2 = forum.getUserList().getById(username);
        assertTrue(u2.getId() == u.getId());
    }
    // --- COURSETESTS --- //
    @Test
    public void testCourseFindAll(){
        Course c = new Course("AAA000","Test");
        forum.getCourseList().create(c);
        Course c1 = new Course("AAA001","Test1");
        forum.getCourseList().create(c1);
        Course c2 = new Course("AAA002","Test2");
        forum.getCourseList().create(c2);
        assertTrue(forum.getCourseList().findAll().size() == 3);
    }
    @Test
    public void testPersistACourse(){
        Course c = new Course("1234", "testCourse");
        forum.getCourseList().create(c);
        Course c2 = forum.getCourseList().getById(c.getId());
        assertTrue(c2.equals(c));                
    }
    
    @Test
    public void testCourseDelete(){
        Course c = new Course("TST999", "Testkurs");
        forum.getCourseList().create(c);
        assertTrue(forum.getCourseList().count() == 1);
        if(D) log.log(Level.INFO, "Number of courses: " + forum.getCourseList().count());
        System.out.println(c.getId());
        forum.getCourseList().delete(c.getId());
        assertTrue(forum.getCourseList().count() == 0);
        if(D) log.log(Level.INFO, "Number of courses: " + forum.getCourseList().count());
    }
    @Test
    public void testCoursegetById(){
        Course c = new Course("DAT997", "Testkursen");
        forum.getCourseList().create(c);
        
        Course c1 = forum.getCourseList().getById("DAT997");
        assertTrue(c.equals(c1));
    }
    @Test
    public void testCourseGetByName(){
        String cName = "Testing";
        Course c = new Course("AAA000",cName);
        forum.getCourseList().create(c);
        int index = forum.getCourseList().getByName(cName).indexOf(c);
        assertNotNull(forum.getCourseList().getByName(cName).get(index));
    }
}

