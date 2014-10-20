package cds;


import cds.core.Course;
import cds.core.GroupUser;
import javax.xml.bind.annotation.*;

/**
 * Need this because we have an immutable class Product
 * (JAX-RS must have default constructor)
 * 
 * Possible other solution: Change in model to make Product mutable or 
 * http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
 * @author 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Course", propOrder = {
    "id",
    "cCode",
    "name"
})
public class CourseWrapper {

    private Course course;

    protected CourseWrapper() { // Must have
    }
   
    public CourseWrapper(Course course) { 
        this.course = course; 
    }
    
    @XmlElement //If serving XML we should use @XmlAttribute 
    public Long getId() {
        return course.getId();
    }
    
    @XmlElement
    public String getCCode() {
        return course.getCCode();
    }

    @XmlElement
    public String getName() {
        return course.getName();
    }

    @Override
    public String toString() {
        return "GroupUser{" + "id=" + getId() + ", cCode=" + getCCode() + ", name=" + getName() + "}";
    }
    
   
}
