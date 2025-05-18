package org.alex.dao;

import org.alex.model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDao {

    public int add(Producto emp) throws SQLException;

    public Producto getById(int id) throws SQLException;

    public List<Producto> getAll() throws SQLException;

    public int update(Producto emp) throws SQLException;

    public void delete(int id) throws SQLException;

}
