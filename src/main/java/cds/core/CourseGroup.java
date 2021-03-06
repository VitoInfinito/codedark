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
import java.util.ArrayList;

/**
 *  Entity describing a Group within in a course containing users.
 * 
 * @author codedark
 */
@Entity
public class CourseGroup extends AbstractEntity<Long> {
   
    @OneToMany
    private List<GroupUser> members;
    @ManyToOne
    private GroupUser owner;
    @ManyToOne
    private Course course;
    @Column(nullable = false)
    private String gName;
    private int maxNbr;
    
    public CourseGroup(){}
    
    public CourseGroup(Course c, String n, GroupUser user, int max){
        course = c;
        gName = n;
        members = new ArrayList<>();
        owner = user;
        maxNbr = max;
        members.add(owner);
    }
    
    public CourseGroup(Long id, Course c, String n, List<GroupUser> memberList, GroupUser user, int max){
        super(id);
        course = c;
        gName = n;
        owner = user;
        members = memberList;
        maxNbr = max;
    }
    
    public CourseGroup(Long id, Course c, String n, GroupUser user, int max){
        super(id);
        gName = n;
        course = c;
        members = new ArrayList<GroupUser>();
        owner = user;
        maxNbr = max;
        members.add(owner);
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
    
    public GroupUser getOwner() {
        return owner;
    }
    
    public int getmaxNbr(){
        return maxNbr;
    }

    @Override
    public String toString() {
        return "CourseGroup{ " + "members=" 
                + members.toString() + ", owner=" 
                + owner.toString() + ", course=" 
                + course.toString() + ", gName=" 
                + gName + '}';
    }

    public void setMembers(List<GroupUser> members) {
        this.members = members;
    }

    public void setOwner(GroupUser owner) {
        this.owner = owner;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public void setmaxNbr(int maxNbr) {
        this.maxNbr = maxNbr;
    }
    
    
    
}
