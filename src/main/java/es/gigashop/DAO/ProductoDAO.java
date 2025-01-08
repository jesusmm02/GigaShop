package es.gigashop.DAO;

import es.gigashop.beans.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDAO implements IProductoDAO {
    
    @Override
    public List<Producto> getProductos() {
        List<Producto> productos = new ArrayList<>();
        Connection connection;
        ResultSet resultado;
        PreparedStatement preparada;
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT 8";
        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setImagen(resultado.getString("imagen"));
                
                productos.add(producto);
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }
        return productos;
    }
    
    @Override
    public Producto getProductoPorID(Short idProducto) {
        Producto producto = null;
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;
        String sql = "SELECT * FROM productos WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idProducto);
            resultado = preparada.executeQuery();

            resultado.next();
            producto = new Producto();
            producto.setIdProducto(resultado.getShort(1));
            producto.setNombre(resultado.getString(2));

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }
        return producto;
    }
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
   
}
