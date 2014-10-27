/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

/**
 * Enables us to change and expand the backend more easily
 * 
 * @author codedark
 */
public interface IForum {
    public ICourseList getCourseList();
    
    public IGroupUserList getUserList();
    
    public ICourseGroupList getGroupList();
}
