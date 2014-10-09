/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import javax.persistence.Column;
import javax.persistence.Entity;
import persistence.AbstractEntity;


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

}
