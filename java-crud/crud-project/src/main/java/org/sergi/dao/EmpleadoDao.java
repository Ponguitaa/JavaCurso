package org.sergi.dao;

import org.sergi.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface EmpleadoDao {

    public int add(Empleado emp) throws SQLException;

    public Empleado getById(int id) throws SQLException;

    public List<Empleado> getAll() throws SQLException;

    public int update(Empleado emp) throws SQLException;

    public void delete(int id) throws SQLException;

}