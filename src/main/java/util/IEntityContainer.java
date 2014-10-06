    
package util;

import util.IEntity;
import java.util.List;

/**
 * Blabla comments
 *
 * @author 
 * @param <T> type of elements in container
 * @param <K> K is type of id (primary key)
 */
public interface IEntityContainer<T extends IEntity<K>, K> {

    public void create(T t);

    public void delete(K id);

    public void update(T t);

    public T find(K id);

    public List<T> findAll();

    public List<T> findRange(int first, int n );

    public int count();
   
}