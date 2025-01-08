<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                                    <th>Nombre</th>
                                    <th>Cantidad</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Dividir los productos por dos puntos -->
                                <c:forEach var="producto" items="${carrito}">
                                        <tr>
                                            <td>${producto.key}</td>
                                            <td>${producto.value}</td>
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
                                <button type="submit" class="btn btn-success" name="boton" value="comprar">
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
