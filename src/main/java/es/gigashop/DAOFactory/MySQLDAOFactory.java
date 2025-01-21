package es.gigashop.DAOFactory;


import es.gigashop.DAO.ICategoriaDAO;
import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAO.IUsuarioDAO;

import es.gigashop.DAO.CategoriaDAO;
import es.gigashop.DAO.ProductoDAO;
import es.gigashop.DAO.UsuarioDAO;

/**
 * Fábrica concreta para la fuente de datos MySQL
 */
public class MySQLDAOFactory extends DAOFactory{

    @Override
    public IProductoDAO getProductoDAO() {
        return new ProductoDAO();
    }
    
    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }
    
    @Override
    public ICategoriaDAO getCategoriaDAO() {
        return new CategoriaDAO();
    }
   
}