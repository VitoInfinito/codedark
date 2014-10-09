/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;


import java.util.ArrayList;
import java.util.List;
import persistence.IDAO;
import javax.inject.*;
import javax.persistence.EntityManager;
import persistence.AbstractDAO;

/**
 *
 * @author codeshark
 */
public class CourseList extends AbstractDAO<Course, String> implements ICourseList{

    
    @Inject
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
        Course found = null;
        for(Course c: findAll()){
            if(c.getCCode().equals(cc)){
                found = c;
            }
        }
        return found;
    }

       
}
