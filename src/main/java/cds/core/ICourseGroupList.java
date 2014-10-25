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
    public List<CourseGroup> getByCourse(String ccode);
    public CourseGroup getByNameAndCourse(String name, String course);
    public List<CourseGroup> getByUser(String uName);
}
