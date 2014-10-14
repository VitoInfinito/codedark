package cds.core;

import java.util.List;
import javax.ejb.Local;
import cds.persistence.IDAO;

/**
 
 * A wrapper for the shop. We should only have
 * one shop (better use CDI more later)
 * @author 
 */
 

//Possibly use and rework
@Local
public interface ICourseGroupList extends IDAO<CourseGroup, Long> {
    public CourseGroup getByName(String name);
    
    public List<CourseGroup> getByCourse(Course course);
}
