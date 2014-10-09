/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import persistence.AbstractDAO;

/**
 *
 * @author HForsvall
 */
@Stateless
public class GroupList extends AbstractDAO<Group, Long> implements IGroupList {

   
    
    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Group> getByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
