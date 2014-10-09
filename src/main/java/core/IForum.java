/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author HForsvall
 */
public interface IForum {
    public ICourseList getCourseList();
    
    public IUserList getUserList();
    
    public IGroupList getGroupList();
}
