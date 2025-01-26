package es.gigashop.controllers;

import es.gigashop.DAO.IUsuarioDAO;
import es.gigashop.DAOFactory.DAOFactory;

import es.gigashop.beans.Usuario;
import es.gigashop.models.Utils;
import java.io.File;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.beanutils.BeanUtils;

@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class RegistroController extends HttpServlet {

    private static final String UPLOAD_DIR = "IMG/avatares";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(".").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (null == accion) {
            // Registrar un nuevo usuario

            String url = "JSP/tienda.jsp";

            Usuario usuario;
            DAOFactory daoF = DAOFactory.getDAOFactory();
            IUsuarioDAO usuDAO = daoF.getUsuarioDAO();
            HttpSession session = request.getSession();

            // Comprobamos que todos los campos estén rellenos y que las contraseñas coincidan
            Enumeration<String> parametros = request.getParameterNames();
            String error = Utils.comprobarCampos(parametros, request);

            if (!error.equals("n")) {
                /*
                * En el caso de que exista error se realizan las siguientes funciones:
                *   - Cargamos un atributo de petición con un mensaje de aviso
                *   - Dirigimos el flujo hacia el formulario de registro para que se muestren los mensajes dependiendo del error
                 */
                String aviso = "Las contraseñas no son iguales";
                if (error.equals("v")) {
                    aviso = "Todos los campos son obligatorios";
                }
                request.setAttribute("errorCreate", aviso);
                url = "/JSP/registro.jsp";
            } else { // Si no hay errores
                try {
                    usuario = new Usuario();
                    BeanUtils.populate(usuario, request.getParameterMap());

                    // Manejar subida del avatar
                    Part filePart = request.getPart("avatar");

                    if (filePart != null && filePart.getSize() > 0) {

                        // Obtener el nombre del archivo original
                        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                        // Obtener la extensión del archivo (por ejemplo, .jpg, .png, etc.)
                        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

                        // Generar nombre único para el archivo
                        String uniqueFileName = String.valueOf(System.currentTimeMillis()) + fileExtension;

                        // Establecer el directorio de subida
                        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }

                        // Ruta completa para guardar el archivo
                        String filePath = uploadPath + File.separator + uniqueFileName;

                        // Guardar archivo
                        filePart.write(filePath);

                        // Guardar la ruta del avatar en el usuario (solo el nombre del archivo)
                        usuario.setAvatar(uniqueFileName);
                    } else {
                        usuario.setAvatar("default.jpg");
                    }

                    // Establecer la fecha del último acceso
                    Timestamp ahora = new Timestamp(new Date().getTime());
                    usuario.setUltimoAcceso(ahora);

                    if (usuDAO.registrarUsuario(usuario)) { // Si el método devuelve emailRepetido = TRUE
                        request.setAttribute("errorCreate", "El usuario ya existe");
                        url = "/JSP/registro.jsp";
                    } else {
                        //session.removeAttribute("pedido");
                        session.setAttribute("usuario", usuario); // Mete en sesión el nuevo usuario
                        request.setAttribute("usuRegistrado", "Se ha registrado el usuario <strong>" + usuario.getNombre() + "</strong> correctamente");
                        url = "/JSP/tienda.jsp";
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Existe un error al utilizar la clase BeanUtils. Escribimos el logger y se visualiza error500.jsp
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            request.getRequestDispatcher(url).forward(request, response);
        } else {
            // Acciones AJAX de comprobar correo y generar letra del dni
            switch (accion) {
                case "comprobarEmail": {
                    // Caso 1: Comprobar si el correo está disponible
                    String email = request.getParameter("email");

                    // Llamamos al DAO para verificar si el correo ya existe
                    DAOFactory daoF = DAOFactory.getDAOFactory();
                    IUsuarioDAO usuDAO = daoF.getUsuarioDAO();
                    boolean disponible = usuDAO.emailDisponible(email);

                    // Devolvemos un JSON indicando si el correo está disponible o no
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"disponible\": " + disponible + "}");

                    break;
                }
                case "generarDni": {
                    String numeros = request.getParameter("numeros");
                    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

                    try {
                        int posicion = Integer.parseInt(numeros) % 23;
                        String letra = String.valueOf(letras.charAt(posicion));

                        // Responder con la letra calculada
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"data\": \"" + letra + "\"}");
                    } catch (NumberFormatException e) {
                        Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, e);

                        // Enviar error en la respuesta
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"error\": \"Formato de DNI inválido\"}");
                    }
                    break;
                }
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
    }
}
