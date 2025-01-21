package es.gigashop.controllers;

import es.gigashop.DAO.IUsuarioDAO;
import es.gigashop.DAOFactory.DAOFactory;
import es.gigashop.beans.Usuario;
import es.gigashop.models.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
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

                            // Iniciar sesión y redirigir al usuario
                            HttpSession session = request.getSession();
                            session.setAttribute("usuario", usuario);
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
                    if (session != null && session.getAttribute("usuario") != null) {
                        Usuario usuario = (Usuario) session.getAttribute("usuario");
                        request.setAttribute("usuario", usuario);

                        // Redirigir a la vista de edición de perfil
                        request.getRequestDispatcher("/JSP/editarPerfil.jsp").forward(request, response);
                    } else {
                        // Si no hay usuario en sesión, redirige al inicio
                        request.getRequestDispatcher("/JSP/tienda.jsp").forward(request, response);
                    }
                    break;
                }
                case "actualizarPerfil": {
                    HttpSession session = request.getSession(false);
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
                            // Comparar directamente con la contraseña del usuario en sesión
                            if (hashedPasswordActual.equals(usuario.getPassword())) {
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
