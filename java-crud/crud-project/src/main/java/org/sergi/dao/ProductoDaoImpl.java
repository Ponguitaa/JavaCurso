package org.alex.dao;

import org.alex.model.Producto;
import org.alex.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static ProductoDaoImpl instance;

    static {
        instance = new ProductoDaoImpl();
    }

    private ProductoDaoImpl() {}

    public static ProductoDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Producto prod) throws SQLException {

        String sql = """
                INSERT INTO producto 
                    (nombre, precio, id_categoria)
                VALUES (?, ?, ?)
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, prod.getNombre());
            pstm.setDouble(2, prod.getPrecio());
            pstm.setInt(3, prod.getCategoria().getId());

            System.out.println(prod.getCategoria());

            result = pstm.executeUpdate();

        }

        return result;
    }

    @Override
    public Producto getById(int id) throws SQLException {

        Producto result = null;

        String sql = "SELECT * FROM producto WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {
                    result = new Producto();
                    result.setNombre(rs.getString("nombre"));
                    result.setPrecio(rs.getDouble("precio"));
                    result.setId(rs.getInt("id_categoria"));
                }

            }
        }

        return result;
    }

    @Override
    public List<Producto> getAll() throws SQLException {

        String sql  = "SELECT * FROM producto";
        List<Producto> result = new ArrayList<>();

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {

            Producto emp;

            while(rs.next()) {
                emp = new Producto();
                emp.setNombre(rs.getString("nombre"));
                emp.setPrecio(rs.getDouble("precio"));
                emp.setId(rs.getInt("id_categoria"));
                result.add(emp);
            }
        }

        return result;
    }

    @Override
    public int update(Producto emp) throws SQLException {

        String sql = """
                UPDATE producto SET
                    nombre = ?, precio = ?,
                    id_categoria = ?
                WHERE id = ?
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, emp.getNombre());
            pstm.setDouble(2, emp.getPrecio());
            pstm.setInt(3, emp.getCategoria().getId());
            pstm.setInt(6, emp.getId());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }
}
