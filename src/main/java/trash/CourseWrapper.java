//package cds;
//
//
//import cds.core.Course;
//import javax.xml.bind.annotation.*;
//
///**
// * Need this because we have an immutable class Product
// * (JAX-RS must have default constructor)
// * 
// * Possible other solution: Change in model to make Product mutable or 
// * http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
// * @author 
// */
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.PROPERTY)
//@XmlType(name = "Course", propOrder = {
//    "ccode",
//    "id",
//    "name"
//})
//public class Course {
//
//    private Course course;
//
//    protected Course() {
//    }
//   
//    public Course(Course course) { 
//        this.course = course; 
//    }
//    
//    @XmlElement
//    public Long getId() {
//        return course.getId();
//    }
//    
//    @XmlElement
//    public String getCcode() {
//        return course.getCcode();
//    }
//
//    @XmlElement
//    public String getName() {
//        return course.getName();
//    }
//
//    @Override
//    public String toString() {
//        return "Course{" + "id=" + getId() + ", cCode=" + getCcode() + ", name=" + getName() + "}";
//    }
//    
//   
//}
