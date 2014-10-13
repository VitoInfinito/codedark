/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.Objects;
import javax.persistence.*;
import persistence.AbstractEntity;

/**
 *
 * @author 
 */
@Entity
public class Course extends AbstractEntity{
    
    @Column(nullable = false)
    private String cCode;
    private String name;
    
    
    protected Course(){
        
    }
    
    public Course(Long id, String cCode, String name){
        super(id);
        this.cCode = cCode;
        this.name = name;
    }
    
    public Course(String cCode, String name){
        this.cCode = cCode;
        this.name = name;
    }

    public String getCCode(){
        return cCode;
    }
    
    public String getName(){
        return name;
    }
    
    public String toString(){
        return "Course{ cCode = " + cCode + ", name = " + name + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Course other = (Course) obj;
        if (!Objects.equals(this.cCode, other.cCode) || !Objects.equals(this.name, other.name)) {
            return false;
        }
        
        return true;
    }
    
}
