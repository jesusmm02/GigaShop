package es.gigashop.controllers;

import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAO.ProductoDAO;
import es.gigashop.DAOFactory.DAOFactory;
import es.gigashop.beans.Categoria;
import es.gigashop.beans.Filtro;
import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;

import es.gigashop.beans.Producto;

import es.gigashop.models.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

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

        if (session.getAttribute("productos") == null) {
            DAOFactory daoF = DAOFactory.getDAOFactory();
            IProductoDAO prodDAO = daoF.getProductoDAO();
            productos = prodDAO.getProductos();
            session.setAttribute("productos", productos);
        }

        Pedido pedido = Utils.recuperarPedidoDeSesion(session);

        Map<Short, Integer> carrito;
        carrito = Utils.obtenerCarritoDesdeCookie(request);
        if (carrito == null) {
            carrito = new HashMap<>();
        }

        Utils.actualizarPedidoDesdeCarrito(pedido, carrito);

        // Intentar calcular el importe total
        pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));

        pedido.setIva(pedido.getImporte() * 0.21);

        session.setAttribute("pedido", pedido);

        url = "JSP/tienda.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "JSP/tienda.jsp"; // Página de resultados de productos
        String boton = request.getParameter("boton");

        // Obtiene la sesión actual
        HttpSession session = request.getSession();

        // Verificar si hay un término de búsqueda
        String busqueda = request.getParameter("busqueda");

        if (busqueda != null && busqueda.length() > 1) {
            // Si hay un término de búsqueda válido (más de 1 letra)
            ProductoDAO prodDAO = new ProductoDAO();
            List<Producto> productos = prodDAO.buscarProductosNombreDescripcion(busqueda);

            request.setAttribute("productos", productos); // Resultados de búsqueda
            request.setAttribute("busquedaActual", busqueda); // Mantener el valor en el input del formulario
        } else {
            // Si no hay búsqueda, procesamos los filtros
            if (boton != null) {
                switch (boton) {
                    case "login":
                        url = "/JSP/login.jsp";
                        break;
                    case "registro":
                        url = "/JSP/registro.jsp";
                        break;
                    case "logout":

                        session = request.getSession(false); // Obtener la sesión actual sin crear una nueva

                        if (session != null) {
                            session.removeAttribute("usuario");
                        }

                        request.setAttribute("logout", "Se ha cerrado la sesión.");

                        url = "/JSP/tienda.jsp";
                        break;

                    case "carrito":
                        url = "/JSP/carrito.jsp";
                        break;
                    case "eliminarCarro":
                        Map<Short, Integer> carritoVacio = new HashMap<>();
                        session.removeAttribute("pedido");
                        session.removeAttribute("carrito");
                        Utils.actualizarCookie(response, carritoVacio);
                        request.setAttribute("elimCarrito", "Se ha eliminado el carrito");
                        break;
                    case "verDetalles":
                        // Procesar ver detalles del producto
                        String idProductoStr = request.getParameter("idProducto");

                        if (idProductoStr != null) {
                            try {
                                int idProducto = Integer.parseInt(idProductoStr);

                                List<Producto> productos = (List<Producto>) session.getAttribute("productos");

                                if (productos != null) {
                                    Producto productoSeleccionado = null;
                                    for (Producto p : productos) {
                                        if (p.getIdProducto() == idProducto) {
                                            productoSeleccionado = p;
                                            break;
                                        }
                                    }

                                    if (productoSeleccionado != null) {
                                        request.setAttribute("producto", productoSeleccionado);
                                        url = "JSP/producto.jsp";
                                    } else {
                                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado");
                                        return;
                                    }
                                } else {
                                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lista de productos no disponible en sesión");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto inválido");
                                return;
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto no proporcionado");
                            return;
                        }
                        break;
                }
            } else {
                // Procesa filtros como categorías, marcas y rango de precios
                String[] categoriasSeleccionadas = request.getParameterValues("categorias");
                String[] marcasSeleccionadas = request.getParameterValues("marcas");
                String rangoPrecio = request.getParameter("rangoPrecio");

                Filtro filtro = new Filtro();

                // Procesar categorías seleccionadas
                if (categoriasSeleccionadas != null && categoriasSeleccionadas.length > 0) {
                    List<Categoria> categorias = new ArrayList<>();
                    for (String categoriaId : categoriasSeleccionadas) {
                        Categoria categoria = new Categoria();
                        categoria.setIdCategoria(Byte.parseByte(categoriaId));
                        categorias.add(categoria);
                    }
                    filtro.setCategorias(categorias);
                }

                // Procesar marcas seleccionadas
                if (marcasSeleccionadas != null && marcasSeleccionadas.length > 0) {
                    filtro.setMarcas(Arrays.asList(marcasSeleccionadas));
                }

                // Procesar el rango de precio
                if (rangoPrecio != null && !rangoPrecio.isEmpty()) {
                    if (rangoPrecio.equals("+500")) {
                        filtro.setPrecioMin("500");
                        filtro.setPrecioMax(null); // No hay límite superior
                    } else if (rangoPrecio.contains("-")) {
                        String[] precios = rangoPrecio.trim().split("-");
                        if (precios.length == 2) {
                            try {
                                String minStr = precios[0].trim();
                                String maxStr = precios[1].trim();

                                double min = Double.parseDouble(minStr);
                                double max = Double.parseDouble(maxStr);

                                filtro.setPrecioMin(String.valueOf(min));
                                filtro.setPrecioMax(String.valueOf(max));

                            } catch (NumberFormatException e) {
                                request.setAttribute("error", "Los valores del rango no son números válidos.");
                            }
                        } else {
                            request.setAttribute("error", "El rango de precios debe tener dos valores.");
                        }
                    }
                }

                // Obtener productos filtrados
                ProductoDAO prodDAO = new ProductoDAO();
                List<Producto> productos = prodDAO.getProductosFiltrados(filtro);

                // Enviar los productos filtrados y el filtro a la vista
                session.setAttribute("productos", productos);
                request.setAttribute("filtrosSeleccionados", filtro);

            }
        }

        // Redirige a la página correspondiente
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
