/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import javax.ejb.Local;
import perstistence.IDAO;

/**
 *
 * @author 
 */
@Local
public interface IUserList extends IDAO<User, Long> {
    
    public List<User> getByName(String name);
}
