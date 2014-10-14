/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cds.core;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import cds.persistence.AbstractDAO;

/**
 *
 * @author
 */
@Stateless
public class GroupUserList extends AbstractDAO<GroupUser, Long> implements IGroupUserList{

    
    @PersistenceContext
    private EntityManager em;
    
    public GroupUserList() {
        super(GroupUser.class);
    }
    
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public GroupUser getBySsnbr(Long ssnbr) {
       for(GroupUser u : findAll()) {
            if (u.getSsnbr() == ssnbr) {
                return u;
            }
        }
        return null;
    }
    
    
    
}
