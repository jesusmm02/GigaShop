package es.gigashop.controllers;

import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAO.IUsuarioDAO;
import es.gigashop.DAOFactory.DAOFactory;

import es.gigashop.beans.Producto;
import es.gigashop.beans.Usuario;

import es.gigashop.models.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Enumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
public class RegistroController extends HttpServlet {

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
        request.getRequestDispatcher(".").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "JSP/tienda.jsp";

        Usuario usuario;
        DAOFactory daoF = DAOFactory.getDAOFactory();
        IUsuarioDAO usuDAO = daoF.getUsuarioDAO();

        // Primero comprobamos que todos los campos estén rellenos y que la contraseña sea la misma
        Enumeration<String> parametros = request.getParameterNames();
        String error = Utils.comprobarCampos(parametros, request);

        if (!error.equals("n")) {
            /*
                     * En el caso de que exista error se realizan las siguientes funciones:
                     *   - Cargamos un atributo de petición explicando lo acontecido
                     *   - Dirigimos el flujo hacia el formulario de entrada de datos para insertar
             */
            url = "/JSP/registro.jsp";
            String aviso = "Las contraseñas no son iguales";
            if (error.equals("v")) {
                aviso = "Todos los campos obligatorios";
            }
            request.setAttribute("errorCreate", aviso);

        } else {

            try {
                usuario = new Usuario();
                BeanUtils.populate(usuario, request.getParameterMap());

                if (usuDAO.registrarUsuario(usuario)) {
                    request.setAttribute("errorCreate", "El username ya existe en nuestra base de datos");
                    url = "/JSP/registro.jsp";
                } else {
                    request.setAttribute("usuario", usuario);
                    url = "/JSP/tienda.jsp";
                }

            } catch (IllegalAccessException | InvocationTargetException e) {

                // Existe un error al utilizar la clase BeanUtils. Escribimos el logger y se visualiza error500.jsp
                Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, e);

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
