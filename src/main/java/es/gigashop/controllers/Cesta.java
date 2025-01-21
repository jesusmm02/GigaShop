package es.gigashop.controllers;

import es.gigashop.models.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "Cesta", urlPatterns = {"/Cesta"})
public class Cesta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(".").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/JSP/tienda.jsp"; // La URL por defecto a la que se redirige al final

        // Obtén la acción que se realiza
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();

        // Recupera el carrito de la sesión
        Map<Short, Integer> carrito = (Map<Short, Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new HashMap<>();
        }

        try {
            if ("eliminarProd".equals(accion)) {
                // Acción: Eliminar producto del carrito
                String idProductoStr = request.getParameter("idProducto");
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    short idProducto = Short.parseShort(idProductoStr);

                    // Elimina completamente el producto del carrito
                    carrito.remove(idProducto);  // Elimina el producto por su id

                    // Actualiza la sesión y la cookie
                    session.setAttribute("carrito", carrito);
                    Utils.actualizarCookie(response, carrito);
                    
                    if (carrito.isEmpty()){
                        request.setAttribute("prodElim", "El carrito se ha vaciado por completo.");
                        url = "JSP/tienda.jsp";
                    } else {
                        // Mensaje de confirmación
                        request.setAttribute("prodElim", "Se ha eliminado el producto del carrito.");
                        url = "JSP/carrito.jsp";
                    }
                }
            } else if (request.getParameter("annadir") != null) {
                // Acción: Añadir producto al carrito
                String idProductoStr = request.getParameter("annadir");
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    short idProducto = Short.parseShort(idProductoStr);

                    // Incrementa la cantidad si ya existe, o lo añade si no
                    carrito.put(idProducto, carrito.getOrDefault(idProducto, 0) + 1);

                    // Actualiza la sesión y la cookie
                    session.setAttribute("carrito", carrito);
                    Utils.actualizarCookie(response, carrito);

                    // Mensaje de confirmación
                    request.setAttribute("añadido", "Se ha añadido un producto al carrito.");
                }
            }
        } catch (NumberFormatException e) {
            // Maneja errores de formato en los parámetros
            request.setAttribute("error", "Error al procesar los datos del producto: " + e.getMessage());
            url = "/JSP/tienda.jsp";  // Redirige a la tienda en caso de error
        }

        // Redirige al URL correspondiente
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
