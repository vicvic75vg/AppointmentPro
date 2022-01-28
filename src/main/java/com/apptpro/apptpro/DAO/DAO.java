package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Customer;
import javafx.collections.ObservableList;

/**
 * The Interface for the DAO
 * @param <T> The model of the DAO
 */
public interface DAO<T> {
   /**
    * Defines a save() method to be implemented
    * @param t The generic type
    */
   void save(T t);

   /**
    * Defines an update() method to be implemented
    * @param t The generic type to pass
    * @return Returns true on successful update
    */
   boolean update( T t);

   /**
    * Defines the delete() method to be implemented
    * @param t The generic type to pass
    * @return Returns true on successful delete
    */
   boolean delete(T t);
}
