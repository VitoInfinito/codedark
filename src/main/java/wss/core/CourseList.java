/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wss.core;


import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import wss.persistence.AbstractDAO;

/**
 *
 * @author codeshark
 */
@Stateless
public class CourseList extends AbstractDAO<Course, Long> implements ICourseList{

    
    
    @PersistenceContext
    private EntityManager em; 
    
    public static ICourseList newInstance() {
        return new CourseList();
    }

    protected CourseList() {
        super(Course.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }
    
    @Override
    public Course getByCC(String cc) {
        for(Course c: findAll()){
            if(c.getCCode().equals(cc)){
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Course> getByName(String name) {
        List<Course> found = new ArrayList<>();
        for (Course c : findRange(0, count())) {
            if (c.getName().equals(name)) {
                found.add(c);
            }
        }
        return found;
    }

       
}
