package es.gigashop.models;

import java.util.Enumeration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class Utils {

    /**
     * Recupera el carrito almacenado en las cookies del usuario.
     *
     * @param request El objeto HttpServletRequest que contiene las cookies del
     * cliente.
     * @return Un mapa que representa el carrito con `idProducto` como clave y
     * `cantidad` como valor, o un mapa vacío si no se encuentra la cookie.
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
     * @param cookieValue El valor de la cookie (un String que contiene los
     * datos del carrito).
     * @return Un mapa con `idProducto` como clave y `cantidad` como valor.
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
     * Convierte un mapa que representa el carrito en un String para almacenarlo
     * en una cookie.
     *
     * @param carrito Un mapa con `idProducto` como clave y `cantidad` como
     * valor.
     * @return Un String que representa los datos del carrito.
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
     * Actualiza la cookie "carrito" en la respuesta HTTP con los datos proporcionados en el mapa del carrito.
     * @param response el objeto HttpServletResponse utilizado para agregar la cookie a la respuesta.
     * @param carrito un mapa que representa el carrito de compras, donde la clave es el ID del producto 
     *                y el valor que es la cantidad.
     */
    public static void actualizarCookie(HttpServletResponse response, Map<Short, Integer> carrito) {
        String carritoStr = convertirCarritoACookie(carrito);
        Cookie cookieCarrito = new Cookie("carrito", carritoStr);
        cookieCarrito.setMaxAge(172800); // 2 días
        response.addCookie(cookieCarrito);
    }
    
    /**
     * Comprueba que algún parámetro que se envie no esté vacío y que las contraseñas coincidan.
     * @param parametros enumeration que contiene los nombres de los parámetros enviados.
     * @param request el objeto HttpServletRequest que contiene los datos de la solicitud HTTP.
     * @return un código de error:
 *         <ul>
 *             <li>"n" si no se encuentran errores.</li>
 *             <li>"v" si algún campo obligatorio está vacío.</li>
 *             <li>"c" si las contraseñas no coinciden.</li>
 *         </ul>
     */
    public static String comprobarCampos(Enumeration<String> parametros, HttpServletRequest request) {
        String error = "n";
        while (parametros.hasMoreElements() && error.equals("n")) {
            String nombre = parametros.nextElement();
            if (request.getParameter(nombre).length() == 0) {
                error = "v";
            }
        }
        if(error.equals("n")) {
            if(!request.getParameter("password").equals(request.getParameter("confirmPassword"))) {
                error = "c";
            }
        }
        return error;
    }
}
