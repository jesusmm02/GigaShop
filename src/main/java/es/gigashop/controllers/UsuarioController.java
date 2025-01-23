package es.gigashop.controllers;

import es.gigashop.DAO.IPedidoDAO;
import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAO.IUsuarioDAO;
import es.gigashop.DAOFactory.DAOFactory;
import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Producto;
import es.gigashop.beans.Usuario;
import es.gigashop.models.Utils;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jesús
 */
@WebServlet(name = "UsuarioController", urlPatterns = {"/UsuarioController"})
public class UsuarioController extends HttpServlet {

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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String accion = request.getParameter("accion");

        if (null == accion) {
            // Si la acción no coincide con las esperadas, redirigir al inicio
            request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
        } else {
            switch (accion) {
                case "login": {
                    DAOFactory daoF = DAOFactory.getDAOFactory();
                    IUsuarioDAO usuarioDAO = daoF.getUsuarioDAO();

                    Usuario usuario = usuarioDAO.obtenerUsuarioPorEmail(email);
                    if (usuario != null) {
                        // Convertir la contraseña ingresada a MD5 y comparar con la base de datos
                        String hashedPassword = Utils.hashMD5(password);
                        if (hashedPassword.equals(usuario.getPassword())) {
                            // Actualizar la fecha de último acceso
                            Timestamp ahora = new Timestamp(new Date().getTime());
                            usuario.setUltimoAcceso(ahora);

                            // Llamar al DAO para actualizar la información en la base de datos
                            usuarioDAO.actualizarUsuario(usuario);

                            // Meter en sesión el usuario logueado
                            HttpSession session = request.getSession();
                            session.setAttribute("usuario", usuario);

                            // LÓGICA PARA CARGAR EL CARRITO
                            IPedidoDAO pdiDAO = daoF.getPedidoDAO();
                            IProductoDAO proDAO = daoF.getProductoDAO();

                            // Recuperar el carrito del usuario
                            Pedido pedido = pdiDAO.obtenerPedidoPorUsuario(usuario.getIdUsuario());

                            if (pedido != null) {
                                // Recuperar las líneas del pedido
                                List<LineaPedido> lineasPedidos = pdiDAO.obtenerLineasPedido(pedido.getIdPedido());
                                for (LineaPedido linea : lineasPedidos) {
                                    // Completar los datos del producto
                                    Producto producto = proDAO.getProductoPorID(linea.getProducto().getIdProducto());
                                    linea.setProducto(producto);
                                }
                                pedido.setLineasPedidos(lineasPedidos);
                                session.setAttribute("pedido", pedido);
                            } else {
                                // Crear un carrito vacío si no existe
                                pedido = new Pedido();
                                List<LineaPedido> lineaspedidos = new ArrayList();
                                pedido.setFecha(new Date());
                                pedido.setEstado(Pedido.Estado.c);
                                pedido.setUsuario(usuario);
                                pedido.setImporte(0.0);
                                pedido.setIva(0.0);
                                pedido.setLineasPedidos(lineaspedidos);
                                session.setAttribute("pedido", pedido);
                            }

                            request.setAttribute("login", "Se ha logueado el usuario " + "<strong>" + usuario.getNombre() + "</strong>");
                            request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "La contraseña es incorrecta.");
                            request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", "El correo proporcionado no está registrado.");
                        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
                    }
                }

                break;
                case "editarPerfil": {
                    HttpSession session = request.getSession(false);

                    // Si hay usuario en sesión, entonces puedo actualizarlo
                    if (session != null && session.getAttribute("usuario") != null) {
                        Usuario usuario = (Usuario) session.getAttribute("usuario");
                        // Crea un atributo de petición usuario para poder mostrar sus parámetros en la vista de edición
                        request.setAttribute("usuario", usuario);

                        // Redirigir a la vista de edición de perfil
                        request.getRequestDispatcher("/JSP/editarPerfil.jsp").forward(request, response);
                    } else {
                        // Si no hay usuario en sesión, redirige al inicio de la aplicación
                        request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
                    }
                    break;
                }
                case "actualizarPerfil": {
                    HttpSession session = request.getSession(false);

                    // Si usuario está en sesión
                    if (session != null && session.getAttribute("usuario") != null) {
                        Usuario usuario = (Usuario) session.getAttribute("usuario");
                        DAOFactory daoF = DAOFactory.getDAOFactory();
                        IUsuarioDAO usuDAO = daoF.getUsuarioDAO();

                        // Recuperar datos del formulario
                        String nombre = request.getParameter("nombre");
                        String apellidos = request.getParameter("apellidos");
                        String telefono = request.getParameter("telefono");
                        String direccion = request.getParameter("direccion");
                        String codigoPostal = request.getParameter("codigoPostal");
                        String provincia = request.getParameter("provincia");
                        String localidad = request.getParameter("localidad");

                        // Contraseña
                        String passwordActual = request.getParameter("passwordActual");
                        String passwordNueva1 = request.getParameter("passwordNueva1");
                        String passwordNueva2 = request.getParameter("passwordNueva2");

                        // Validar y actualizar datos del usuario
                        usuario.setNombre(nombre);
                        usuario.setApellidos(apellidos);
                        usuario.setTelefono(telefono);
                        usuario.setDireccion(direccion);
                        usuario.setCodigoPostal(codigoPostal);
                        usuario.setProvincia(provincia);
                        usuario.setLocalidad(localidad);

                        // Validar cambio de contraseña
                        if (passwordActual != null && !passwordActual.isEmpty()) {
                            String hashedPasswordActual = Utils.hashMD5(passwordActual);

                            // Compara con la contraseña del usuario en sesión
                            if (hashedPasswordActual.equals(usuario.getPassword())) {

                                // Si la contraseñaNueva 1 coincide con la contraseñaNueva2
                                if (passwordNueva1 != null && passwordNueva1.equals(passwordNueva2)) {
                                    // Hashear la nueva contraseña antes de guardarla
                                    String hashedNuevaPassword = Utils.hashMD5(passwordNueva1);
                                    usuario.setPassword(hashedNuevaPassword);
                                } else {
                                    request.setAttribute("error", "Las contraseñas nuevas no coinciden");
                                    request.getRequestDispatcher("/JSP/editarPerfil.jsp").forward(request, response);
                                    return;
                                }
                            } else {
                                request.setAttribute("error", "La contraseña actual no es correcta");
                                request.getRequestDispatcher("/JSP/editarPerfil.jsp").forward(request, response);
                                return;
                            }
                        }

                        // Actualizar el usuario en la base de datos
                        if (usuDAO.actualizarUsuario(usuario)) {
                            session.setAttribute("usuario", usuario); // Actualizar usuario en sesión
                            request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Error al actualizar los datos. Inténtalo más tarde.");
                            request.getRequestDispatcher("/JSP/editarPerfil.jsp").forward(request, response);
                        }
                    } else {
                        request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
                    }
                    break;
                }
                default:
                    // Si la acción no coincide con las esperadas, redirigir al inicio
                    request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
                    break;
            }
        }
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
