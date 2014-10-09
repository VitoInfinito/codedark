/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import perstistence.IEntity;

/**
 *
 * @author 
 */
public class Course implements IEntity<String>{
    
    private String cCode;
    private String name;
    
    
    protected Course(){
        
    }
    
    public Course(String cCode, String name){
        this.cCode = cCode;
        this.name = name;
    }

    //kurskod
    @Override
    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getCCode(){
        return cCode;
    }
    
}
