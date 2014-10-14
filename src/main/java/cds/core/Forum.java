/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author HForsvall
 */
@ApplicationScoped // TODO: Kanske ska vara session
public class Forum implements IForum{

    @EJB private ICourseList courseList;
    @EJB private IGroupUserList userList;
    @EJB private ICourseGroupList groupList;
    
    public Forum(){}
    
     public static IForum newInstance() {
        return new Forum();
    }

    
    @Override
    public ICourseList getCourseList() {
        return courseList;
    }

    @Override
    public IGroupUserList getUserList() {
        return userList;
    }

    @Override
    public ICourseGroupList getGroupList() {
        return groupList;
    }
    
}
