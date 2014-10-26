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
 * Entity describing a University Course
 * @author codedark
 */
@Entity
public class Course extends AbstractEntity<String>{
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Course other = (Course) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        
        return true;
    }
    
}
