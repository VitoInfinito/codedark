/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import util.AbstractEntity;

/**
 *
 * @author
 */
public class User extends AbstractEntity<Long>{

    private int ssnbr;
    private String email;
    private String pwd;
    
    public User(int ssnbr, String email, String pwd) {
        this.ssnbr = ssnbr;
        this.email = email;
        this.pwd = pwd;
    }

    public int getSsnbr() {
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
    
    
    
}
