package util;

import java.io.Serializable;

/**
 * We do good comments
 * 
 * K is type of id (primary key)
 * 
 * @author 
 * @param <K> type for key (later primary key)
 */
public interface IEntity<K> extends Serializable  {
    public K getId();
}
