/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import javax.ejb.Local;
import persistence.IDAO;

/**
 *
 * @author Patricia
 */
@Local
public interface ICourseList extends IDAO<Course, String>{
    
    public Course getByCC(String cc);
    
}
