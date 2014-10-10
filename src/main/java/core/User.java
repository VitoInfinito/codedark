package core;

import javax.persistence.Column;
import javax.persistence.Entity;
import persistence.AbstractEntity;


/**
 *
 * @author
 */
@Entity
public class User extends AbstractEntity{

    @Column(nullable = false)
    private long ssnbr;
    private String fname;

    private String lname;
    private String email;
    @Column(nullable = false)
    private String pwd;
    
    public User(){}
    
    public User(Long ssnbr, String email, String pwd) {
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
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

}
