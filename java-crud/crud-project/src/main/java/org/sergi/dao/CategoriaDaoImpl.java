package org.alex.dao;

import org.alex.model.Categoria;
import org.alex.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private static CategoriaDaoImpl instance;

    static {
        instance = new CategoriaDaoImpl();
    }

    private CategoriaDaoImpl() {}

    public static CategoriaDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Categoria cat) throws SQLException {

        String sql = """
                INSERT INTO categoria (nombre)
                VALUES (?)
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cat.getNombre());

            result = pstm.executeUpdate();

        }

        return result;
    }

    @Override
    public Categoria getById(int id) throws SQLException {
        Categoria result = null;
        String sql = "SELECT * FROM categoria WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    result = new Categoria();
                    result.setId(rs.getInt("id")); // <- faltaba esto
                    result.setNombre(rs.getString("nombre"));
                }
            }
        }

        return result;
    }


    @Override
    public List<Categoria> getAll() throws SQLException {

        String sql  = "SELECT * FROM categoria";
        List<Categoria> result = new ArrayList<>();

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {

            Categoria cat;

            while(rs.next()) {
                cat = new Categoria();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                result.add(cat);
            }
        }

        return result;
    }

    @Override
    public int update(Categoria cat) throws SQLException {

        String sql = """
                UPDATE categoria SET
                    nombre = ?
                WHERE id = ?
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cat.getNombre());
            pstm.setInt(2, cat.getId());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM categoria WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.executeUpdate();
        }

    }
}
