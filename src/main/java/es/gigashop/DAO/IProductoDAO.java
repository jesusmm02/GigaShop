package es.gigashop.DAO;

import es.gigashop.beans.Producto;
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
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
