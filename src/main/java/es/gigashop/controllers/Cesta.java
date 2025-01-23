package es.gigashop.controllers;

import es.gigashop.DAO.IPedidoDAO;
import es.gigashop.DAOFactory.DAOFactory;
import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Usuario;
import es.gigashop.models.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import es.gigashop.DAO.ILineaPedidoDAO;
import es.gigashop.DAO.IProductoDAO;
import es.gigashop.beans.Producto;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Map<Short, Integer> carrito;

        carrito = Utils.obtenerCarritoDesdeCookie(request);

        System.out.println(carrito);

        if (carrito == null) {
            carrito = new HashMap<>();
        }

        try {
            if ("sumarCant".equals(accion)) {
                String idProductoStr = request.getParameter("idProducto");
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    short idProducto = Short.parseShort(idProductoStr);

                    // Incrementa la cantidad del producto en el carrito
                    carrito.put(idProducto, carrito.getOrDefault(idProducto, 0) + 1);

                    // Actualiza el carrito en sesión y la cookie
                    session.setAttribute("carrito", carrito);
                    Utils.actualizarCookie(response, carrito);

                    // Sincroniza el pedido
                    Pedido pedido = Utils.recuperarPedidoDeSesion(session);
                    Utils.actualizarPedidoDesdeCarrito(pedido, carrito);
                    pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                    pedido.setIva(pedido.getImporte() * 0.21);
                    session.setAttribute("pedido", pedido);

                    url = "/JSP/carrito.jsp";
                }
            } else if ("restarCant".equals(accion)) {
                String idProductoStr = request.getParameter("idProducto");
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    short idProducto = Short.parseShort(idProductoStr);

                    // Decrementa la cantidad del producto en el carrito
                    if (carrito.containsKey(idProducto)) {
                        int cantidadActual = carrito.get(idProducto);
                        if (cantidadActual > 1) {
                            carrito.put(idProducto, cantidadActual - 1);
                        } else {
                            // Opcional: elimina el producto si llega a 0
                            carrito.remove(idProducto);
                        }

                        // Actualiza el carrito en sesión y la cookie
                        session.setAttribute("carrito", carrito);
                        Utils.actualizarCookie(response, carrito);

                        // Sincroniza el pedido
                        Pedido pedido = Utils.recuperarPedidoDeSesion(session);
                        Utils.actualizarPedidoDesdeCarrito(pedido, carrito);
                        pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                        pedido.setIva(pedido.getImporte() * 0.21);
                        session.setAttribute("pedido", pedido);

                        url = "/JSP/carrito.jsp";
                    }
                }
            } else if ("eliminarProd".equals(accion)) {
                // Acción: Eliminar producto del carrito
                String idProductoStr = request.getParameter("idProducto");
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    short idProducto = Short.parseShort(idProductoStr);

                    // Elimina completamente el producto del carrito
                    carrito.remove(idProducto);  // Elimina el producto por su id

                    Pedido pedido = Utils.recuperarPedidoDeSesion(session);

                    // Lista de productos de mi carrito
                    List<LineaPedido> lineasPedidos = pedido.getLineasPedidos();

                    Iterator<LineaPedido> iterator = lineasPedidos.iterator();
                    while (iterator.hasNext()) {
                        LineaPedido lpe = iterator.next();
                        if (lpe.getProducto().getIdProducto() == idProducto) {
                            iterator.remove(); // Elimina el producto de forma segura
                        }
                    }

                    pedido.setLineasPedidos(lineasPedidos);
                    pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                    pedido.setIva(pedido.getImporte() * 0.21);
                    session.setAttribute("pedido", pedido);

                    Utils.actualizarCookie(response, carrito);

                    if (carrito.isEmpty()) {
                        session.removeAttribute("pedido");
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

                    Short idProducto = Short.parseShort(idProductoStr);

                    Usuario usuario = (Usuario) session.getAttribute("usuario");

                    if (usuario != null) {
                        DAOFactory daoF = DAOFactory.getDAOFactory();
                        IPedidoDAO pdiDAO = daoF.getPedidoDAO();
                        IProductoDAO proDAO = daoF.getProductoDAO();

                        Pedido pedido = (Pedido) session.getAttribute("pedido");

                        if (pedido == null) {
                            pedido = pdiDAO.obtenerPedidoPorUsuario(usuario.getIdUsuario());
                        }

                        if (pedido == null) {
                            pedido = new Pedido();
                            List<LineaPedido> lineaspedidos = new ArrayList();
                            pedido.setFecha(new Date());
                            pedido.setEstado(Pedido.Estado.c);
                            pedido.setUsuario(usuario);
                            pedido.setImporte(0.0);
                            pedido.setIva(0.0);
                            pedido.setLineasPedidos(lineaspedidos);

                            // Inserta el nuevo pedido en la base de datos
                            Short idPedido = pdiDAO.insertarPedido(pedido);

                            System.out.println("ID Pedido que devuelve el método dao: " + idPedido);

                            pedido.setIdPedido(idPedido);
                        
                        } else {
                            
                            // Si ya existe un pedido, recupera sus líneas de pedido
                            List<LineaPedido> lineaspedidos = pdiDAO.obtenerLineasPedido(pedido.getIdPedido());
                            
                            // Completa los detalles del producto para cada línea de pedido
                            for (LineaPedido linea : lineaspedidos) {
                                Producto producto = proDAO.getProductoPorID(linea.getProducto().getIdProducto());
                                linea.setProducto(producto);
                            }
                            
                            pedido.setLineasPedidos(lineaspedidos);
                        }

                        ILineaPedidoDAO lnpDAO = daoF.getLineaPedidoDAO();
                        LineaPedido lineaPedido = lnpDAO.obtenerLineaPedido(pedido.getIdPedido(), idProducto);

                        if (pedido.getLineasPedidos() == null) {
                            pedido.setLineasPedidos(new ArrayList<LineaPedido>());
                        }
                        
                        if (lineaPedido == null) {
                            lineaPedido = new LineaPedido();
                            lineaPedido.setPedido(pedido);
                            Producto producto = proDAO.getProductoPorID(idProducto);
                            lineaPedido.setProducto(producto);
                            lineaPedido.setCantidad((byte) 1);
                            pedido.getLineasPedidos().add(lineaPedido);
                        } else {
                            // Incrementar la cantidad si ya existe
                            byte cantidadActual = lineaPedido.getCantidad();
                            lineaPedido.setCantidad((byte)(cantidadActual + 1));
                        }

                        // Inserta o actualiza la línea de pedido en la base de datos
                        lnpDAO.insertarOActualizarLineaPedido(lineaPedido);

                        // Actualiza la sesión y el importe del pedido
                        pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                        pedido.setIva(pedido.getImporte() * 0.21);
                        session.setAttribute("pedido", pedido);
                        
                        System.out.println("Pedido recuperado: " + pedido);
                        System.out.println("Líneas de pedido: " + pedido.getLineasPedidos());
                        System.out.println("Producto añadido ID: " + idProducto);

                    } else {

                        idProducto = Short.parseShort(idProductoStr);

                        // Incrementa la cantidad si ya existe, o lo añade si no
                        carrito.put(idProducto, carrito.getOrDefault(idProducto, 0) + 1);

                        // Actualiza la sesión y la cookie
                        session.setAttribute("carrito", carrito);
                        Utils.actualizarCookie(response, carrito);

                        Pedido pedido = Utils.recuperarPedidoDeSesion(session);
                        pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                        pedido.setIva(pedido.getImporte() * 0.21);
                        session.setAttribute("pedido", pedido);

                        Utils.actualizarPedidoDesdeCarrito(pedido, carrito);

                        // Mensaje de confirmación
                        request.setAttribute("añadido", "Se ha añadido un producto al carrito.");
                    }
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
