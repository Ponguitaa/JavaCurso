package org.alex.dao;

import org.alex.model.Categoria;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDao {

    public int add(Categoria emp) throws SQLException;

    public Categoria getById(int id) throws SQLException;

    public List<Categoria> getAll() throws SQLException;

    public int update(Categoria emp) throws SQLException;

    public void delete(int id) throws SQLException;

}
