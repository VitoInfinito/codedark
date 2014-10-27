/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

import java.util.List;
import javax.ejb.Local;
import cds.persistence.IDAO;

/**
 * Enables us to Use backend operations
 * 
 * @author codedark
 */
@Local
public interface ICourseList extends IDAO<Course, String>{
    
    public Course getById(String cc);
    
    public List<Course> getByName(String name);
    
}
