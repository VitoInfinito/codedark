/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import cds.persistence.AbstractDAO;

/**
 *
 * @author HForsvall
 */
@Stateless
public class CourseGroupList extends AbstractDAO<CourseGroup, Long> implements ICourseGroupList {

    
    @PersistenceContext
    private EntityManager em;
    
    public CourseGroupList() {
        super(CourseGroup.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public CourseGroup getByNameAndCourse(String name, String course) {
        Iterator<CourseGroup> it = getByCourse(course).iterator();
        while(it.hasNext()){
            CourseGroup g = it.next();
            if(g.getgName().equals(name))
                return g;
        }
        return null;
    }  

    @Override
    public List<CourseGroup> getByCourse(String cc) {
       List<CourseGroup> found = new ArrayList<>();
        for (CourseGroup g : findRange(0, count())) {
            if (g.getCourse().getCcode().equals(cc)) {
                found.add(g);
            }
        }
        return found;
    }
    
    @Override
    public List<CourseGroup> getByUser(Long ssNbr){
        List<CourseGroup> found = new ArrayList<>();
        for(CourseGroup g : findRange(0, count())){
            for(GroupUser u: g.getMembers()){
                if(u.getSsnbr().equals(ssNbr)){
                    found.add(g);
                }
            }
        }
        
        return found;
    }
}
