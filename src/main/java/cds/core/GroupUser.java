package cds.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import cds.persistence.AbstractEntity;


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
    private String admin;
    
    public GroupUser(){}
    
    public GroupUser(Long id, Long ssnbr, String email, String pwd, String fname, String lname, String admin) {
        super(id);
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        this.admin = admin;
    }
    
    public GroupUser(Long ssnbr, String email, String pwd, String fname, String lname, String admin) {
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
        this.admin = admin;
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

    public String getAdmin() {
        return admin;
    }
    @Override
    public String toString() {
        return "GroupUser{" + "ssnbr=" + ssnbr + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", pwd=" + pwd + ", admin= " + admin + '}';
    }
    


}
