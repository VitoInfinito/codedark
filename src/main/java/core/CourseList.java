/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;


import javax.ejb.Stateless;
import javax.inject.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.AbstractDAO;

/**
 *
 * @author codeshark
 */
@Stateless
public class CourseList extends AbstractDAO<Course, String> implements ICourseList{

    
    @Inject
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

       
}
