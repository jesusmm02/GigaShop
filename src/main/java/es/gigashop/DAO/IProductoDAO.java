package es.gigashop.DAO;

import es.gigashop.beans.Filtro;
import es.gigashop.beans.Producto;
import java.sql.SQLException;
import java.util.List;

public interface IProductoDAO {
    
    /**
     * Obtiene todos los productos de la base de datos
     * @return Lista de todos los productos
     */
    public List<Producto> getProductos();
    
    /**
     * Obtiene el producto con el id especificado
     * @param idProducto id del producto que queremos saber el nombre
     * @return Producto con ese id
     */
    public Producto getProductoPorID(Short idProducto);
    
    
    /**
     * Obtiene todas las marcas de productos
     * @return Lista de todas las marcas
     */
    public List<String> getMarcas();
    
    
    public List<Producto> getProductosFiltrados(Filtro filtro);
    
    public List<Producto> buscarProductosNombreDescripcion(String texto);
    
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
