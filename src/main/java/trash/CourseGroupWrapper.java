package trash;

//package cds;
//
//
//import cds.core.CourseGroup;
//import cds.core.GroupUser;
//import java.util.List;
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
//@XmlType(name = "CourseGroup", propOrder = {
//    "id",
//    "members",
//    "course",
//    "gName"
//})
//
//
//public class CourseGroupWrapper {
//
////    private CourseGroup cGroup;
////
////    protected CourseGroupWrapper() { // Must have
////    }
////   
////    public CourseGroupWrapper(CourseGroup cGroup) { 
////        this.cGroup = cGroup; 
////    }
////    
////    @XmlElement //If serving XML we should use @XmlAttribute 
////    public Long getId() {
////        return cGroup.getId();
////    }
////
////    @XmlElement
////    public List<GroupUser> getMembers() {
////        return cGroup.getMembers();
////    }
////    
////    @XmlElement
////    public String getgName() {
////        return cGroup.getgName();
////    }
////
////    @Override
////    public String toString() {
////        return "CourseGroup{" + "id=" + getId() + ", members=" + getMembers() + ", gName=" + getgName() + "}";
////    }
////    
//}
