package persistence;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import javax.persistence.MappedSuperclass;

/**
 * We do good comments
 * 
 * K is type of id (primary key)
 * 
 * @author 
 * @param <K> type for key (later primary key)
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 
   
    protected AbstractEntity(){
    }
    
    protected AbstractEntity(Long id){
        this.id = id;
    }
    
    public Long getId(){
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEntity other = (AbstractEntity) obj;
        return Objects.equals(this.id, other.id);
    }
}
