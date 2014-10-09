/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author HForsvall
 */
@ApplicationScoped // TODO: Kanske ska vara session
public class Forum implements IForum{

    @EJB private ICourseList courseList;
    @EJB private IUserList userList;
    @EJB private IGroupList groupList;
    
    public Forum(){}
    
     public static IForum newInstance() {
        return new Forum();
    }

    
    @Override
    public ICourseList getCourseList() {
        return courseList;
    }

    @Override
    public IUserList getUserList() {
        return userList;
    }

    @Override
    public IGroupList getGroupList() {
        return groupList;
    }
    
}
