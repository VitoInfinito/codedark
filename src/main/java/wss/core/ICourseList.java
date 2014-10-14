/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wss.core;

import java.util.List;
import javax.ejb.Local;
import wss.persistence.IDAO;

/**
 *
 * @author Patricia
 */
@Local
public interface ICourseList extends IDAO<Course, Long>{
    
    public Course getByCC(String cc);
    
    public List<Course> getByName(String name);
    
}
