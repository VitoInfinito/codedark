/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import javax.persistence.Column;
import javax.persistence.Entity;
import util.AbstractEntity;


/**
 *
 * @author
 */
@Entity
public class User extends AbstractEntity<Long>{

    @Column(nullable = false)
    private long ssnbr;
    private String email;
    @Column(nullable = false)
    private String pwd;
    
    public User(){}
    
    public User(int ssnbr, String email, String pwd) {
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
    //personnummer
    @Override
    public Long getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.ssnbr ^ (this.ssnbr >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.ssnbr != other.ssnbr) {
            return false;
        }
        return true;
    }  
}
