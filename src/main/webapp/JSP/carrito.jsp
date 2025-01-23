<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <title>GigaShop - Carrito de la compra</title>
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
                        <c:forEach var="linea" items="${pedido.lineasPedidos}">
                            <tr>
                                <!-- Mostrar la imagen del producto -->
                                <td>
                                    <img src="${contexto}/IMG/productos/${linea.producto.imagen}.jpg" class="card-img-top" alt="${linea.producto.nombre}" style="max-width: 100px;">
                                </td>
                                
                                <!-- Mostrar el código del producto -->
                                <td>${linea.producto.idProducto}</td>
                                
                                <!-- Mostrar el nombre del producto -->
                                <td>${linea.producto.nombre}</td>
                                
                                <!-- Mostrar la marca del producto -->
                                <td>${linea.producto.marca}</td>
                                
                                <!-- Mostrar el precio unitario del producto -->
                                <td><fmt:formatNumber value="${linea.producto.precio}" type="currency" /></td>
                                
                                <!-- Mostrar la cantidad de producto -->
                                <td>${linea.cantidad}</td>
                                
                                <!-- Mostrar el importe (precio unitario * cantidad) -->
                                <td><fmt:formatNumber value="${linea.producto.precio * linea.cantidad}" type="currency" /></td>
                                
                                <!-- Mostrar los botones de acciones (+ , -) -->
                                <td>
                                    <form action="Cesta" method="POST">
                                        <input type="hidden" name="idProducto" value="${linea.producto.idProducto}" />
                                        <button type="submit" class="btn btn-primary btn-sm" name="accion" value="sumarCant"> + </button>
                                        <button type="submit" class="btn btn-danger btn-sm" name="accion" value="restarCant"
                                                <c:if test="${linea.cantidad == 1}">disabled</c:if>> - </button>
                                    </form>
                                </td>

                                <!-- Botón que elimina producto del carrito -->
                                <td>
                                    <form action="Cesta" method="POST">
                                            <input type="hidden" name="idProducto" value="${linea.producto.idProducto}" />
                                        <button type="submit" name="accion" value="eliminarProd" class="btn btn-secondary btn-sm">
                                            Eliminar
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Tabla para los totales -->
                <table class="table w-50 ml-auto bg-light shadow-sm border rounded">
                    <tbody>
                        <!-- Base imponible -->
                        <tr>
                            <td class="text-right font-weight-bold border-0">Base imponible:</td>
                            <td class="text-right border-0">
                                <c:set var="baseImponible" value="0" />
                                <c:forEach var="linea" items="${pedido.lineasPedidos}">
                                    <c:set var="baseImponible" value="${baseImponible + (linea.producto.precio * linea.cantidad)}" />
                                </c:forEach>
                                <fmt:formatNumber value="${baseImponible}" type="currency" />
                            </td>
                        </tr>
                        
                        <!-- IVA -->
                        <tr>
                            <td class="text-right font-weight-bold">IVA (21%):</td>
                            <td class="text-right">
                                <fmt:formatNumber value="${baseImponible * 0.21}" type="currency" />
                            </td>
                        </tr>
                        
                        <!-- Total a pagar -->
                        <tr class="bg-secondary text-white">
                            <td class="text-right font-weight-bold h5 mb-0">Total a pagar:</td>
                            <td class="text-right font-weight-bold">
                                <fmt:formatNumber value="${baseImponible * 1.21}" type="currency" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!-- Botones para eliminar o comprar carrito -->
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
            </form>
        </div>
    </body>
</html>
