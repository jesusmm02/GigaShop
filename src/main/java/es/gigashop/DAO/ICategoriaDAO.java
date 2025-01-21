package es.gigashop.DAO;

import es.gigashop.beans.Categoria;
import java.util.List;


public interface ICategoriaDAO {
    
    /**
     * Obtiene todos las categorias de la base de datos
     * @return Lista de todas categorias
     */
    public List<Categoria> getCategorias();
    
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
