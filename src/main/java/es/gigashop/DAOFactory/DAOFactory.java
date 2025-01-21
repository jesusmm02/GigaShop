package es.gigashop.DAOFactory;

import es.gigashop.DAO.ICategoriaDAO;
import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAO.IUsuarioDAO;


public abstract class DAOFactory {

    /**
     * Una clase abstracta por cada tabla de la base de datos
     * @return Interface de las operaciones a realizar con la tabla
     */
    public abstract IProductoDAO getProductoDAO();
    
    /**
     * Clase abstracta de la tabla Usuario
     * @return Interface de las operaciones a realizar con la tabla
     */
    public abstract IUsuarioDAO getUsuarioDAO();
    
    /**
     * Clase abstracta de la tabla Categoria
     * @return Interface de las operaciones a realizar con la tabla
     */
    public abstract ICategoriaDAO getCategoriaDAO();
    

    /**
     * Fábrica abstracta
     * @return Objeto de la fábrica abstracta
     */
    public static DAOFactory getDAOFactory() {
        
        DAOFactory daof = null;

        daof = new MySQLDAOFactory();

        return daof;
    }

}
