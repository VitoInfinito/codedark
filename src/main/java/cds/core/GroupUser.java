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

    @Column(nullable = false)
    private Long ssnbr;
    private String fname;
    private String lname;
    private String email;
    @Column(nullable = false)
    private String pwd;
    
    public GroupUser(){}
    
    public GroupUser(Long id, Long ssnbr, String email, String pwd, String fname, String lname) {
        super(id);
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
    }
    
    public GroupUser(Long ssnbr, String email, String pwd, String fname, String lname) {
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
        this.fname = fname;
        this.lname = lname;
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

    @Override
    public String toString() {
        return "GroupUser{" + "ssnbr=" + ssnbr + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", pwd=" + pwd + '}';
    }
    


}
