/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cds.core;

import java.util.Objects;
import javax.persistence.*;
import cds.persistence.AbstractEntity;

/**
 *
 * @author sofiaedstrom
 */
@Entity
public class Course extends AbstractEntity{
    
    @Column(nullable = false, unique = true)
    private String ccode;
    private String name;
    
    
    public Course(){
        
    }
    
    public Course(Long id, String ccode, String name){
        super(id);
        this.ccode = ccode;
        this.name = name;
    }
    
    public Course(String ccode, String name){
        this.ccode = ccode;
        this.name = name;
    }

    public String getCcode(){
        return ccode;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public String toString(){
        return "Course{ ccode = " + ccode + ", name = " + name + "}";
    }
    
    /*@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Course other = (Course) obj;
        if (!Objects.equals(this.ccode, other.ccode) || !Objects.equals(this.name, other.name)) {
            return false;
        }
        
        return true;
    }*/
    
}
