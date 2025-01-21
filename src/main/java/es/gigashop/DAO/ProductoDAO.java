package es.gigashop.DAO;

import es.gigashop.beans.Categoria;
import es.gigashop.beans.Filtro;
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
        String sql = "SELECT p.*, c.Nombre as NombreCategoria "
                + "FROM productos p "
                + "JOIN categorias c ON p.IdCategoria = c.IdCategoria "
                + "ORDER BY RAND() LIMIT 9";
        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();
            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getShort("IdProducto"));
                producto.setNombre(resultado.getString("Nombre"));
                producto.setPrecio(resultado.getDouble("Precio"));
                producto.setImagen(resultado.getString("Imagen"));
                producto.setDescripcion(resultado.getString("Descripcion"));
                producto.setMarca(resultado.getString("Marca"));

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(resultado.getByte("IdCategoria"));
                categoria.setNombre(resultado.getString("NombreCategoria")); // Usamos el alias
                producto.setCategoria(categoria);

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
    public List<String> getMarcas() {
        List<String> marcas = new ArrayList<>();
        Connection connection;
        ResultSet resultado;
        PreparedStatement preparada;
        String sql = "SELECT DISTINCT marca FROM productos";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                // Agrega directamente la marca (String) a la lista
                marcas.add(resultado.getString("marca"));
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }

        return marcas;
    }

    @Override
public List<Producto> getProductosFiltrados(Filtro filtro) {
    List<Producto> productos = new ArrayList<>();
    Connection connection;
    ResultSet resultado;
    PreparedStatement preparada;

    StringBuilder sql = new StringBuilder(
            "SELECT p.*, c.Nombre as NombreCategoria "
            + "FROM productos p "
            + "JOIN categorias c ON p.IdCategoria = c.IdCategoria "
            + "WHERE 1=1");

    // Aplicar filtro de categoría (si hay)
    if (filtro.getCategorias() != null && !filtro.getCategorias().isEmpty()) {
        sql.append(" AND p.IdCategoria IN (");
        for (int i = 0; i < filtro.getCategorias().size(); i++) {
            sql.append("?");
            if (i < filtro.getCategorias().size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
    }

    // Aplicar filtro de precio (min y max)
    if (filtro.getPrecioMin() != null && filtro.getPrecioMax() != null) {
        sql.append(" AND p.Precio BETWEEN ? AND ?");
    } else if (filtro.getPrecioMin() != null) {
        sql.append(" AND p.Precio >= ?");
    } else if (filtro.getPrecioMax() != null) {
        sql.append(" AND p.Precio <= ?");
    }

    // Aplicar filtro de marca (si hay)
    if (filtro.getMarcas() != null && !filtro.getMarcas().isEmpty()) {
        sql.append(" AND p.Marca IN (");
        for (int i = 0; i < filtro.getMarcas().size(); i++) {
            sql.append("?");
            if (i < filtro.getMarcas().size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
    }

    System.out.println("Consulta SQL generada: " + sql);  // Imprimir para verificar la consulta

    try {
        connection = ConnectionFactory.getConnection();
        preparada = connection.prepareStatement(sql.toString());
        int index = 1;

        // Rellenar parámetros de categoría
        if (filtro.getCategorias() != null && !filtro.getCategorias().isEmpty()) {
            for (Categoria categoria : filtro.getCategorias()) {
                preparada.setInt(index++, categoria.getIdCategoria());
            }
        }

        // Rellenar parámetros de precio mínimo y máximo
        if (filtro.getPrecioMin() != null && filtro.getPrecioMax() != null) {
            preparada.setDouble(index++, filtro.getPrecioMin());
            preparada.setDouble(index++, filtro.getPrecioMax());
        } else if (filtro.getPrecioMin() != null) {
            preparada.setDouble(index++, filtro.getPrecioMin());
        } else if (filtro.getPrecioMax() != null) {
            preparada.setDouble(index++, filtro.getPrecioMax());
        }

        // Rellenar parámetros de marca
        if (filtro.getMarcas() != null && !filtro.getMarcas().isEmpty()) {
            for (String marca : filtro.getMarcas()) {
                preparada.setString(index++, marca);
            }
        }

        // Ejecutar consulta
        resultado = preparada.executeQuery();
        while (resultado.next()) {
            Producto producto = new Producto();
            producto.setIdProducto(resultado.getShort("IdProducto"));
            producto.setNombre(resultado.getString("Nombre"));
            producto.setPrecio(resultado.getDouble("Precio"));
            producto.setMarca(resultado.getString("Marca"));
            producto.setDescripcion(resultado.getString("Descripcion"));
            producto.setImagen(resultado.getString("Imagen"));

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(resultado.getByte("IdCategoria"));
            categoria.setNombre(resultado.getString("NombreCategoria"));
            producto.setCategoria(categoria);

            productos.add(producto);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return productos;
}




    @Override
    public List<Producto> buscarProductosNombreDescripcion(String texto) {
        List<Producto> productos = new ArrayList<>();
        Connection connection = null;
        String sql = "SELECT * FROM PRODUCTOS WHERE nombre LIKE ? OR descripcion LIKE ?";

        try {
            connection = ConnectionFactory.getConnection();  // Establecer la conexión a la base de datos
            try (PreparedStatement ps = connection.prepareStatement(sql)) {  // Se asegura de que PreparedStatement se cierre automáticamente
                String query = "%" + texto + "%";  // Añadir comodines para búsqueda parcial
                ps.setString(1, query);
                ps.setString(2, query);

                try (ResultSet resultado = ps.executeQuery()) {  // Se asegura de que el ResultSet se cierre automáticamente
                    while (resultado.next()) {
                        Producto producto = new Producto();
                        producto.setIdProducto(resultado.getShort("idProducto"));
                        producto.setNombre(resultado.getString("nombre"));
                        producto.setDescripcion(resultado.getString("descripcion"));
                        producto.setPrecio(resultado.getDouble("precio"));
                        producto.setImagen(resultado.getString("imagen"));
                        producto.setMarca(resultado.getString("marca"));

                        Categoria categoria = new Categoria();
                        categoria.setIdCategoria(resultado.getByte("idCategoria"));
                        categoria.setNombre(resultado.getString("nombre"));
                        producto.setCategoria(categoria);

                        productos.add(producto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejar la excepción de la base de datos
        } finally {
            // Asegurarse de cerrar la conexión si está abierta
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // Manejar excepciones al cerrar la conexión
            }
        }

        return productos;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
