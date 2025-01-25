package es.gigashop.models;

import es.gigashop.DAO.IProductoDAO;
import es.gigashop.DAOFactory.DAOFactory;

import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import es.gigashop.beans.Producto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class Utils {

    /**
     * Recupera el carrito almacenado en las cookies del usuario.
     *
     * Busca la cookie "carrito" y convierte su valor en un mapa que representa
     * el carrito.
     *
     * @param request El objeto HttpServletRequest que contiene las cookies del
     * cliente.
     * @return Un mapa con 'idProducto' como clave y 'cantidad' como valor, o un
     * mapa vacío si no existe la cookie.
     */
    public static Map<Short, Integer> obtenerCarritoDesdeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("carrito".equals(cookie.getName())) {
                    // Convierte el valor de la cookie en un mapa (carrito)
                    return convertirCookieACarrito(cookie.getValue());
                }
            }
        }
        // Si no se encuentra la cookie, devuelve un mapa vacío
        return new HashMap<>();
    }

    /**
     * Convierte el valor de una cookie en un mapa que representa el carrito de
     * compras.
     *
     * El formato del valor de la cookie es un lista de pares
     * 'idProducto-cantidad' separados por un punto y coma (;). Este método
     * interpreta esa cadena y la convierte en un mapa con las claves y valores
     * correspondientes.
     *
     * @param cookieValue El valor de la cookie (un String que contiene los
     * datos del carrito).
     * @return Un mapa con 'idProducto' como clave y 'cantidad' como valor.
     */
    public static Map<Short, Integer> convertirCookieACarrito(String cookieValue) {
        Map<Short, Integer> carrito = new HashMap<>();

        if (cookieValue != null && !cookieValue.isEmpty()) {
            String[] productos = cookieValue.split(";");
            for (String producto : productos) {
                String[] atributos = producto.split("-");
                if (atributos.length == 2) {
                    try {
                        short idProducto = Short.parseShort(atributos[0]);
                        int cantidad = Integer.parseInt(atributos[1]);
                        carrito.put(idProducto, cantidad);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return carrito;
    }

    /**
     * Convierte un mapa de carrito en una cadena de texto para almacenarlo en
     * una cookie.
     *
     * Toma un mapa que representa un carrito y lo convierte en una cadena de
     * texto con el formato 'idProducto-cantidad', separando múltiples productos
     * con dos puntos (:)
     *
     * @param carrito Un mapa con 'idProducto' como clave y 'cantidad' como
     * valor.
     * @return Un String que representa el carrito.
     */
    public static String convertirCarritoACookie(Map<Short, Integer> carrito) {
        StringBuilder cookieValue = new StringBuilder();

        for (Map.Entry<Short, Integer> entry : carrito.entrySet()) {
            if (cookieValue.length() > 0) {
                cookieValue.append(":");
            }
            cookieValue.append(entry.getKey()).append("-").append(entry.getValue());
        }

        return cookieValue.toString();
    }

    /**
     * Actualiza la cookie "carrito" en la respuesta HTTP.
     *
     * Toma un mapa que representa el carrito, lo convierte a formato de cadena
     * utilizando el método 'convertirCarritoACookie()' y actualiza la cookie en
     * la respuesta HTTP.
     *
     * @param response el objeto HttpServletResponse para agregar la cookie.
     * @param carrito un mapa que representa el carrito.
     */
    public static void actualizarCookie(HttpServletResponse response, Map<Short, Integer> carrito) {
        String carritoStr = convertirCarritoACookie(carrito);
        Cookie cookieCarrito = new Cookie("carrito", carritoStr);
        cookieCarrito.setMaxAge(172800); // 2 días
        response.addCookie(cookieCarrito);
    }

    /**
     * Comprueba que los parámetros de la solicitud no estén vacíos y que las
     * contraseñas coincidan.
     *
     * Recorre los parámetros de la solicitud y verifica si alguno está vacío.
     * También verifica que los valores de "password" y "confirmPassword" sean
     * iguales. Devuelve un código de error basado en la validación.
     *
     * @param parametros nombres de los parámetros enviados en la solicitud.
     * @param request el objeto HttpServletRequest que contiene los datos de la
     * solicitud.
     * @return un código de error:
     * <ul>
     * <li>"n": No hay errores.</li>
     * <li>"v": Algún campo está vacío.</li>
     * <li>"c": Las contraseñas no son iguales.</li>
     * </ul>
     */
    public static String comprobarCampos(Enumeration<String> parametros, HttpServletRequest request) {
        String error = "n";

        // Campos críticos a validar (sin incluir el archivo aquí)
        String[] camposCriticos = {
            "nombre", "apellidos", "email", "password", "confirmPassword",
            "nif", "telefono", "direccion", "codigoPostal", "provincia", "localidad"
        };

        // Verificar campos críticos
        for (String campo : camposCriticos) {
            String valor = request.getParameter(campo);
            if (valor == null || valor.trim().isEmpty()) {
                error = "v"; // Campo vacío
                break;
            }
        }

        // Verificar el campo de archivo
        if (error.equals("n")) {
            try {
                Part avatarPart = request.getPart("avatar");
                if (avatarPart == null || avatarPart.getSize() <= 0) {
                    request.setAttribute("avatar", "default.jpg");
                } else {
                    System.out.println("Archivo recibido: " + avatarPart.getSubmittedFileName());
                }
            } catch (Exception e) {
                e.printStackTrace(); // Muestra detalles del error en la consola
                error = "a"; // Error al procesar el archivo
            }
        }

        // Verificar contraseñas si no hay otros errores
        if (error.equals("n")) {
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (!password.equals(confirmPassword)) {
                error = "c"; // Contraseñas no coinciden
            }
        }

        return error;
    }

    /**
     * Calcula el hash MD5 de una cadena de texto.
     *
     * Utiliza el algoritmo MD5 para calcular un hash hexadecimal de una cadena
     * de entrada. Si ocurre algún error con el algoritmo, lanza una excepción
     * de tiempo de ejecución.
     *
     * @param input la cadena de texto a hashear.
     * @return el hash MD5 en formato hexadecimal.
     */
    public static String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular MD5", e);
        }
    }

    /**
     * Actualiza las líneas de pedido en base al contenido del carrito: - Si el
     * producto ya está en el pedido, actualiza su cantidad. - Si no está en el
     * pedido, crea una nueva línea de pedido y la agrega.
     *
     * @param pedido objeto pedido a sincronizar.
     * @param carrito un mapa que representa el carrito
     */
    public static void actualizarPedidoDesdeCarrito(Pedido pedido, Map<Short, Integer> carrito) {
        IProductoDAO prodDAO = DAOFactory.getDAOFactory().getProductoDAO();
        // Crear un mapa para las líneas de pedido existentes por ID de producto
        Map<Short, LineaPedido> lineasExistentes = new HashMap<>();
        for (LineaPedido linea : pedido.getLineasPedidos()) {
            lineasExistentes.put(linea.getProducto().getIdProducto(), linea);
        }
        // Iterar sobre el carrito
        for (Map.Entry<Short, Integer> entry : carrito.entrySet()) {
            Short idProducto = entry.getKey();
            Integer cantidadEnCarrito = entry.getValue();
            // Verificar si ya existe una línea de pedido para este producto
            if (lineasExistentes.containsKey(idProducto)) {
                // Actualizar la cantidad existente
                LineaPedido lineaExistente = lineasExistentes.get(idProducto);
                lineaExistente.setCantidad(cantidadEnCarrito.byteValue());
            } else {
                // Crear una nueva línea de pedido si no existe
                Producto producto = prodDAO.getProductoPorID(idProducto);
                LineaPedido nuevaLinea = new LineaPedido();
                nuevaLinea.setProducto(producto);
                nuevaLinea.setCantidad(cantidadEnCarrito.byteValue());
                nuevaLinea.setPedido(pedido);
                // Agregar la nueva línea al pedido
                pedido.getLineasPedidos().add(nuevaLinea);
            }
        }
    }

    /**
     * Recupera un pedido de la sesión del usuario.
     *
     * Busca un objeto Pedido en la sesión. Si no lo encuentra, crea un nuevo
     * pedido y una lista de líneas de pedido, y lo devuelve.
     *
     * @param session obejto HttpSession que contiene los datos de la sesión.
     * @return objeto pedido.
     */
    public static Pedido recuperarPedidoDeSesion(HttpSession session) {
        List<LineaPedido> lineasPedido = new ArrayList<>();
        Pedido pedido = (Pedido) session.getAttribute("pedido");

        if (pedido == null) {
            pedido = new Pedido();
            pedido.setEstado(Pedido.Estado.c);
            pedido.setFecha(new Date());
            pedido.setLineasPedidos(lineasPedido);
        }

        return pedido;
    }

    /**
     * Calcula el importe total de una lista de líneas de pedido.
     *
     * Recorre una lista de líneas de pedido y calcula el importe total
     * multiplicando el precio del producto por la cantidad en cada línea.
     *
     * @param lineasPedidos lista de objetos LineaPedido.
     * @return importe total.
     */
    public static double calcularImporteTotal(List<LineaPedido> lineasPedidos) {
        if (lineasPedidos == null) {
            return 0; // Si no hay líneas, el importe total es 0
        }

        double importeTotal = 0;
        for (LineaPedido linea : lineasPedidos) {
            if (linea.getProducto() != null && linea.getProducto().getPrecio() != null) {
                importeTotal += linea.getProducto().getPrecio() * linea.getCantidad();
            }
        }
        return importeTotal;
    }

}
