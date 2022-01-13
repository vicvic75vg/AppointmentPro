package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Customer;
import javafx.collections.ObservableList;

/**
 * The Interface for the DAO
 * @param <T> The model of the DAO
 */
public interface DAO<T> {
   void save(T t);

   boolean update( T t);
   boolean delete(T t);
}
