/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import persistence.AbstractEntity;

/**
 *
 * @author 
 */

public class Course extends AbstractEntity<String>{
    
    private String cCode;
    private String name;
    
    
    protected Course(){
        
    }
    
    public Course(String cCode, String name){
        this.cCode = cCode;
        this.name = name;
    }

    public String getCCode(){
        return cCode;
    }
    
}
