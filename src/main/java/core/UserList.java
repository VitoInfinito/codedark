/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.ArrayList;
import persistence.IDAO;
import java.util.List;
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
    public List<User> getBySsnbr(Long ssnbr) {
       List<User> found = new ArrayList<>();
        for (User u : findRange(0, count())) {
            if (u.getSsnbr() == ssnbr) {
                found.add(u);
            }
        }
        return found;
    }
    
    
    
}
