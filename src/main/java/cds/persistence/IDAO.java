    
package cds.persistence;

import java.util.List;

/**
 * Generic class for interaction with database
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
