/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.ArrayList;
import java.util.List;
import perstistence.IDAO;
import javax.inject.*;
import javax.persistence.EntityManager;
import perstistence.AbstractDAO;

/**
 *
 * @author Patricia
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
//        List<Course> found = new ArrayList<>();
//        for (Course c : findRange(0, count())) {
//            if (c.getFname().equals(name) || c.getLname().equals(name)) {
//                found.add(c);
//            }
//        }
        return null;
    }

       
}
