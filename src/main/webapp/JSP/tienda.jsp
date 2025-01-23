<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giga Shop</title>
        <link rel="stylesheet" href="${bootstrap}" />
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <style>
            .navbar {
                margin-bottom: 20px;
            }
            .filter-section {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
            }
            .filter-section h5 {
                margin-bottom: 20px;
            }
            .product-card img {
                max-height: 200px;
                object-fit: cover;
            }
            .error {
                color: red;
                font-weight: bold;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <!-- Cabecera -->
        <jsp:include page="/INC/cabecera.inc"/>

        <div class="container mt-3">

            <!-- Barra de navegación y botones -->
            <%@ include file="/INC/barra.jsp" %>

            <!-- Mensaje flotante de aviso -->
            <c:if test="${not empty añadido || not empty elimCarrito || not empty prodElim || not empty usuRegistrado || not empty login || not empty logout}">
                <div id="mensajeFlotante" class="mensaje-flotante">
                    <c:choose>
                        <c:when test="${not empty añadido}">
                            ${añadido}
                        </c:when>
                        <c:when test="${not empty elimCarrito}">
                            ${elimCarrito}
                        </c:when>
                        <c:when test="${not empty prodElim}">
                            ${prodElim}
                        </c:when>
                        <c:when test="${not empty usuRegistrado}">
                            ${usuRegistrado}
                        </c:when>
                        <c:when test="${not empty login}">
                            ${login}
                        </c:when>   
                        <c:when test="${not empty logout}">
                            ${logout}
                        </c:when>
                    </c:choose>
                </div>
            </c:if>

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
                    background-color: #d4edda; /* Verde claro */
                    color: #155724; /* Verde oscuro */
                    padding: 10px 20px;
                    border: 1px solid #c3e6cb;
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
            
        </div>



        <!-- Contenido principal -->
        <div class="container">
            <div class="row">
                <!-- Sección de filtros -->
                <div class="col-md-3">
                    <div class="filter-section">
                        <h5>Filtros</h5>
                        <form action="FrontController" method="POST">

                            <!-- Categorías -->
                            <div class="form-group">
                                <label>Categorías:</label>
                                <c:forEach var="categoria" items="${categorias}">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="categorias" 
                                               value="${categoria.idCategoria}" 
                                               <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.categorias.contains(categoria)}">checked</c:if> />
                                        <label class="form-check-label">${categoria.nombre}</label>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Marcas -->
                            <div class="form-group">
                                <label>Marcas:</label>
                                <c:forEach var="marca" items="${marcas}">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="marcas" 
                                               value="${marca}" 
                                               <c:if test="${filtrosSeleccionados.marcas.contains(marca)}">checked</c:if> />
                                        <label class="form-check-label">${marca}</label>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Rango de precios -->
                            <div class="form-group">
                                <label>Rango de precios:</label>
                                <c:forEach var="rango" items="${['0-100', '101-200', '201-300', '301-500', '+500']}">
                                    <div class="form-check">
                                        <input type="radio" class="form-check-input" name="rangoPrecio" value="${rango}"
                                               <c:if test="${filtrosSeleccionados != null && 
                                                             (rango == '0-100' && filtrosSeleccionados.precioMin == '0' && filtrosSeleccionados.precioMax == '100' ||
                                                             rango == '101-200' && filtrosSeleccionados.precioMin == '101' && filtrosSeleccionados.precioMax == '200' ||
                                                             rango == '201-300' && filtrosSeleccionados.precioMin == '201' && filtrosSeleccionados.precioMax == '300' ||
                                                             rango == '301-500' && filtrosSeleccionados.precioMin == '301' && filtrosSeleccionados.precioMax == '500' ||
                                                             rango == '+500' && filtrosSeleccionados.precioMin == '500') }">checked</c:if>
                                                     />
                                               <label class="form-check-label">${rango} €</label>
                                    </div>
                                </c:forEach>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">Aplicar filtros</button>

                        </form>
                    </div>
                </div>


                <!-- Lista de productos -->
                <div class="col-md-9">
                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty productos}">
                                <c:forEach var="producto" items="${productos}">
                                    <div class="col-md-4">
                                        <!-- Tarjeta de producto con enlace al modal -->
                                        <div class="card product-card mb-4"  
                                             data-bs-toggle="modal" 
                                             data-bs-target="#modalProducto${producto.idProducto}" 
                                             style="cursor: pointer;">
                                            <img src="${contexto}/IMG/productos/${producto.imagen}.jpg" class="card-img-top">
                                            <div class="card-body">
                                                <h5 class="card-title">${producto.nombre}</h5>
                                                <p class="card-text text-primary">
                                                    <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                </p>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Modal de detalles del producto -->
                                    <div class="modal fade" id="modalProducto${producto.idProducto}" tabindex="-1" aria-labelledby="modalLabel${producto.idProducto}" aria-hidden="true">
                                        <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="modalLabel${producto.idProducto}">${producto.nombre}</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <img src="${contexto}/IMG/productos/${producto.imagen}.jpg" class="img-fluid">
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><strong>Marca:</strong> ${producto.marca}</p>
                                                            <p><strong>Categoría:</strong> ${producto.categoria.nombre}</p>
                                                            <p><strong>Descripción:</strong> ${producto.descripcion}</p>
                                                            <h2 class="text-primary">
                                                                <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                            </h2>
                                                            <!-- Botón para añadir al carrito desde el modal -->
                                                            <form action="Cesta" method="POST">
                                                                <button type="submit" class="btn btn-success w-100 mt-2" name="annadir" value="${producto.idProducto}">
                                                                    Añadir a la cesta
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <!-- Si no se muestra ningún producto en la búsqueda -->
                                <div class="col-12 text-center">
                                    <img src="${contexto}/IMG/no-result-found.jpg" alt="No hay resultados" class="img-fluid mt-5">
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>



            </div>
        </div>

    </body>
</html>
