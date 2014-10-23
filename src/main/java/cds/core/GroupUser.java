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
public class GroupUser extends AbstractEntity{

    @Column(nullable = false, unique = true)
    private Long ssnbr;
    private String fname;
    private String lname;
    private String email;
    @Column(nullable = false)
    private String pwd;
   // private String adminuser;
    
    private List<String> belongingTo;
    
    public GroupUser(){}
    
    public GroupUser(Long id, Long ssnbr, String email, String pwd, String fname, String lname, boolean isAdmin) {
        super(id);
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        belongingTo = new ArrayList<String>();
        if(isAdmin) {
          belongingTo.add("admin");
        }
    }
    
    public GroupUser(Long ssnbr, String email, String pwd, String fname, String lname, boolean isAdmin) {
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        belongingTo = new ArrayList<String>();
        if(isAdmin) {
          belongingTo.add("admin");
        }
    }
    
    public void addUserBelongingToGroup(String group) {
        belongingTo.add(group);
    }
    

    public long getSsnbr() {
        return ssnbr;
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
        return "GroupUser{" + "ssnbr=" + ssnbr + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", pwd=" + pwd + ", belongingTo= " + belongingTo.toString() + '}';
    }

    public void setSsnbr(Long ssnbr) {
        this.ssnbr = ssnbr;
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
