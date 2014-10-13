/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.AbstractDAO;

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
    public CourseGroup getByName(String name) {
        Iterator<CourseGroup> it = findAll().iterator();
        while(it.hasNext()){
            CourseGroup g = it.next();
            if(g.getgName().equals(name))
                return g;
        }
        return null;
    }  
}
