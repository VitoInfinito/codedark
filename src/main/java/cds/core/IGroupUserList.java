/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

import javax.ejb.Local;
import cds.persistence.IDAO;

/**
 * Enables us to Use backend operations
 * 
 * @author codedark
 */
@Local
public interface IGroupUserList extends IDAO<GroupUser, String> {
    
    public GroupUser getById(String uName);
}
