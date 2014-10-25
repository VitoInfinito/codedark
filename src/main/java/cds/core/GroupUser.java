package cds.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import cds.persistence.AbstractEntity;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author
 */
@Entity
public class GroupUser extends AbstractEntity<String>{

    private String fname;
    private String lname;
    private String email;
    @Column(nullable = false)
    private String pwd;
    
    private List<String> belongingTo;
    
    public GroupUser(){}
    
    public GroupUser(String username, String email, String pwd, 
            String fname, String lname) {
        super(username);
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        belongingTo = new ArrayList<>();
    }
    
    public GroupUser(String email, String pwd, String fname, String lname) {
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        belongingTo = new ArrayList<>();
    }
    
    public void addUserBelongingToGroup(String group) {
        belongingTo.add(group);
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }
    
    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }
    
    public List<String> getBelongingTo() {
        return belongingTo;
    }
    @Override
    public String toString() {
        return "GroupUser{" + "username=" + getId() + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", pwd=" + pwd + ", belongingTo= " + belongingTo.toString() + '}';
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setBelongingTo(List<String> belongingTo) {
        this.belongingTo = belongingTo;
    }
    
    

}
