package cds.core;

import java.util.List;
import javax.ejb.Local;
import cds.persistence.IDAO;

/**
 * Enables us to Use backend operations
 * 
 * @author codedark
 */
 

//Possibly use and rework
@Local
public interface ICourseGroupList extends IDAO<CourseGroup, Long> {
    public List<CourseGroup> getByCourse(String ccode);
    public CourseGroup getByNameAndCourse(String name, String course);
    public List<CourseGroup> getByUser(String uName);
}
