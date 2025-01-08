package es.gigashop.controllers;

import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAOFactory.DAOFactory;

import es.gigashop.beans.Producto;

import es.gigashop.models.Utils;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "FrontController", urlPatterns = {"/FrontController"})
public class FrontController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url;
        List<Producto> productos; // Lista de productos

        // Obtiene la sesión actual
        HttpSession session = request.getSession();

        DAOFactory daoF = DAOFactory.getDAOFactory();
        IProductoDAO prodDAO = daoF.getProductoDAO();
        productos = prodDAO.getProductos();
        session.setAttribute("productosAlt", productos);

        // Recupera o inicializa el carrito (Map<Integer, Integer>)
        Map<Short, Integer> carrito = (Map<Short, Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }

        url = "JSP/tienda.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "JSP/tienda.jsp";
        String boton = request.getParameter("boton");
        
        // Obtiene la sesión actual
        HttpSession session = request.getSession();

        if (boton != null ) {
            
        
            switch (boton) {
                case "login":
                    url = "/JSP/login.jsp";
                    break;
                case "registro":
                    url = "/JSP/registro.jsp";
                    break;
                case "carrito":
                    url = "/JSP/carrito.jsp";
                    break;
                case "eliminarCarro":
                    Map<Short, Integer> carritoVacio = new HashMap();

                    session.setAttribute("carrito", carritoVacio);

                    Utils.actualizarCookie(response, carritoVacio);

                    break;
            }
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
