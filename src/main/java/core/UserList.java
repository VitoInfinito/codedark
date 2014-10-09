/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.AbstractDAO;

/**
 *
 * @author
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
    public User getBySsnbr(Long ssnbr) {
        for (User u : findAll()) {
            if (u.getSsnbr() == ssnbr) {
                return u;
            }
        }
        return null;
    }
    
    
    
}
