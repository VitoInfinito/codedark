/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import util.IDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.AbstractDAO;

/**
 *
 * @author bjornlexell
 */
@Stateless
public class UserList extends AbstractDAO<User, Long> implements IUserList{

    @Inject
    @PersistenceContext
    private EntityManager em;
    
    public UserList() {
        super(User.class);
    }
    
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<User> getByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
