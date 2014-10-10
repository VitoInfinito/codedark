/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import persistence.AbstractEntity;

/**
 *
 * @author HForsvall
 */
@Entity
public class Group extends AbstractEntity implements Serializable {
   
    @OneToMany
    private List<User> members;
    @ManyToOne
    private Course course;
    private String gName;
    
    public Group(){}
    
    public Group(Course c, String n){
        course = c;
        gName = n;
    }
    
    public List<User> getMembers() {
        return members;
    }

    public Course getCourse() {
        return course;
    }

    public String getgName() {
        return gName;
    }  
}
