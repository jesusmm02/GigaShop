package es.gigashop.DAO;

import es.gigashop.beans.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CategoriaDAO implements ICategoriaDAO {
    
    @Override
    public List<Categoria> getCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        Connection connection;
        ResultSet resultado;
        PreparedStatement preparada;
        String sql = "SELECT DISTINCT c.idCategoria, c.nombre, c.imagen " +
                     "FROM categorias c INNER JOIN productos p ON c.idCategoria = p.idCategoria";
        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(resultado.getByte("idCategoria"));
                categoria.setNombre(resultado.getString("nombre"));
                
                categorias.add(categoria);
            }

        } catch (SQLException e) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }
        return categorias;
    }
    
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
 
}