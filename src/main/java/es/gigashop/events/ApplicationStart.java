package es.gigashop.events;

import es.gigashop.DAO.ICategoriaDAO;
import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAOFactory.DAOFactory;

import es.gigashop.beans.Categoria;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ApplicationStart implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        List<Categoria> categorias; // Lista de categorias

        DAOFactory daoF = DAOFactory.getDAOFactory();
        ICategoriaDAO categDAO = daoF.getCategoriaDAO();
        categorias = categDAO.getCategorias();
        
        ServletContext contexto = sce.getServletContext();
        
        contexto.setAttribute("categorias", categorias);
        
        
        
        List<String> marcas; // Lista de marcas

        IProductoDAO prodDAO = daoF.getProductoDAO();
        marcas = prodDAO.getMarcas();
        
        contexto.setAttribute("marcas", marcas);
        
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext contexto = sce.getServletContext();
        System.out.println("Aplicacion del contexto " + contexto.getContextPath() + " deteniéndose.");
        contexto.removeAttribute("categorias");
        contexto.removeAttribute("marcas");
    }
  
}
