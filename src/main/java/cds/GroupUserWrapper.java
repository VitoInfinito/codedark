package cds;


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
@XmlType(name = "GroupUser", propOrder = {
    "ssnbr",
    "id",
    "email",
    "pwd",
    "fname",
    "lname"
})
public class GroupUserWrapper {

    private GroupUser user;

    protected GroupUserWrapper() { // Must have
    }
   
    public GroupUserWrapper(GroupUser user) { 
        this.user = user; 
    }
    
    @XmlElement
    public long getSsnbr() {
        return user.getSsnbr();
    }

    @XmlElement //If serving XML we should use @XmlAttribute 
    public Long getId() {
        return user.getId();
    }

    @XmlElement
    public String getEmail() {
        return user.getEmail();
    }
    
    @XmlElement
    public String getPwd() {
        return user.getPwd();
    }
    
    @XmlElement
    public String getFname() {
        return user.getFname();
    }
    
    @XmlElement
    public String getLname() {
        return user.getLname();
    }

    @Override
    public String toString() {
        return "GroupUser{" + "ssnbr=" + getSsnbr() + ", fname=" + getFname() + ", lname=" + getLname() + ", email=" + getEmail() + ", pwd=" + getPwd() + '}';
    }
    
}
