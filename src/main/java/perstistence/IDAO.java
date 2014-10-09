    
package perstistence;

<<<<<<< HEAD:src/main/java/perstistence/IDAO.java
import perstistence.IEntity;
=======
import util.AbstractEntity;
>>>>>>> 2f9540183fb431be75232ed4c116cdb6ab72afb8:src/main/java/util/IDAO.java
import java.util.List;

/**
 * Blabla comments
 *
 * @author 
 * @param <T> type of elements in container
 * @param <K> K is type of id (primary key)
 */
public interface IDAO<T, K> {

    public void create(T t);

    public void delete(K id);

    public void update(T t);

    public T find(K id);

    public List<T> findAll();

    public List<T> findRange(int first, int n );

    public int count();
   
}
