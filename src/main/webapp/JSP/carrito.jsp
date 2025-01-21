<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Carrito de la compra</title>
        <link rel="stylesheet" href="${bootstrap}" />
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>
        
        <%@ include file="/INC/barra.jsp" %>
        
        <c:if test="${not empty prodElim}">
                <div id="mensajeFlotante" class="mensaje-flotante">
                    ${prodElim}
                </div>
                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        var mensaje = document.getElementById('mensajeFlotante');
                        if (mensaje) {
                            setTimeout(function () {
                                mensaje.style.opacity = '0'; // Oculta el mensaje
                                setTimeout(function () {
                                    mensaje.remove(); // Elimina el mensaje del DOM
                                }, 1000); // Espera 1 segundo después de ocultarlo
                            }, 3000); // Muestra el mensaje durante 3 segundos
                        }
                    });
                </script>
                <style>
                    .mensaje-flotante {
                        position: absolute;
                        right: 20px;
                        margin-top: 10px;
                        background-color: #f8d7da; /* Rojo claro */
                        color: #721c24; /* Rojo oscuro */
                        padding: 10px 20px;
                        border: 1px solid #f5c6cb; /* Borde rojo */
                        border-radius: 5px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        z-index: 1050;
                        font-size: 14px;
                        animation: link 0.5s ease-out;
                    }

                    @keyframes link {
                        from {
                            opacity: 0;
                            transform: translateY(-10px);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }
                </style>
            </c:if>

        <div class="container my-5">
            <h1 class="text-center">Carrito de compra</h1>

            <form action="Cesta" method="POST">
                <!-- Comprobar si hay cookies -->
                <c:if test="${not empty header['Cookie']}">
                    <!-- Extraer la cookie "carrito" -->
                    <c:set var="carritoCookie" value="${header['Cookie']}" />
                    <c:if test="${fn:contains(carritoCookie, 'carrito=')}">
                        <!-- Extraer solo la parte del carrito -->
                        <c:set var="carritoRaw" value="${fn:substringAfter(carritoCookie, 'carrito=')}" />

                        <!-- Mostrar la tabla -->
                        <table class="table table-striped my-4">
                            <thead>
                                <tr>
                                    <th>Imagen</th>
                                    <th>Código</th>
                                    <th>Nombre</th>
                                    <th>Marca</th>
                                    <th>Precio unitario</th>
                                    <th>Cantidad</th>
                                    <th>Importe</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="producto" items="${carrito}">
                                        <tr>
                                            <td></td>
                                            <td>${producto.key}</td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td>${producto.value}</td>
                                            <td></td>
                                            <td>
                                                <form action="Cesta" method="POST">
                                                    <button type="submit" class="btn btn-primary btn-sm" name="accion" value="sumarCant">
                                                        +
                                                    </button>
                                                    <button type="submit" class="btn btn-danger btn-sm" name="accion" value="restarCant">
                                                        -
                                                    </button>
                                                </form>
                                            </td>
                                            <td>
                                                <form action="Cesta" method="POST">
                                                    <input type="hidden" name="idProducto" value="${producto.key}"/>
                                                    <button type="submit" name="accion" value="eliminarProd" class="btn btn-secondary btn-sm">
                                                        Eliminar
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                </c:forEach>

                            </tbody>
                        </table>

                        <!-- Botones separados de la tabla -->
                        <div class="d-flex justify-content-between mt-4">
                            <form action="FrontController" method="POST">
                                <button type="submit" class="btn btn-warning" name="boton" value="eliminarCarro">
                                    Borrar carrito
                                </button>
                                <button type="submit" class="btn btn-success" name="boton" value="comprar"
                                        <c:if test="${empty sessionScope.usuario}"> disabled </c:if>>
                                    Confirmar compra
                                </button>
                            </form>
                        </div>
                    </c:if>
                </c:if>
            </form>
        </div>

    </body>
</html>
