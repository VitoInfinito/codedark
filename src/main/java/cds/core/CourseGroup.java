/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.core;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import cds.persistence.AbstractEntity;

/**
 *
 * @author HForsvall
 */
@Entity
public class CourseGroup extends AbstractEntity {
   
    @OneToMany
    private List<GroupUser> members;
    @ManyToOne
    private Course course;
    @Column(nullable = false)
    private String gName;
    
    public CourseGroup(){}
    
    public CourseGroup(Course c, String n, List<GroupUser> userList){
        course = c;
        gName = n;
        members = userList;
    }
    
    public CourseGroup(Long id, Course c, String n, List<GroupUser> userList){
        super(id);
        gName = n;
        course = c;
        members = userList;
    }
    
    public List<GroupUser> getMembers() {
        return members;
    }

    public Course getCourse() {
        return course;
    }

    public String getgName() {
        return gName;
    }  
}
