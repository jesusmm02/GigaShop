<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Barra</title>
        <link rel="stylesheet" href="${bootstrap}" />
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container">
                <!-- Barra de búsqueda -->
                <form class="form-inline my-2 my-lg-0" action="${contexto}/FrontController" method="POST" style="max-width: 400px; width: 100%;">
                    <input class="form-control mr-sm-2" type="search" placeholder="Buscar productos..." 
                           name="busqueda" value="${busquedaActual != null ? busquedaActual : ''}">
                    <button class="btn btn-outline-primary my-2 my-sm-0" type="submit">Buscar</button>
                </form>

                <!-- Botones de usuario -->
                <div class="ml-auto d-flex align-items-center">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario}">
                            <form action="FrontController" method="POST" class="mr-2">
                                <button type="submit" class="btn btn-outline-danger" name="boton" value="logout">Cerrar sesión</button>
                            </form>
                            <div class="d-flex align-items-center">
                                <!-- Formulario para redirigir a la edición del perfil usando POST -->
                                <form action="${contexto}/UsuarioController" method="POST" class="d-flex align-items-center">
                                    <input type="hidden" name="accion" value="editarPerfil">
                                    <button type="submit" class="d-flex align-items-center btn btn-link text-decoration-none p-0">
                                        <img src="../IMG/productos/default.jpg" class="rounded-circle" style="width: 40px; height: 40px;">
                                        <div class="d-flex flex-column ml-2">
                                            <span>${sessionScope.usuario.nombre}</span>
                                            <fmt:formatDate value="${sessionScope.usuario.ultimoAcceso}" pattern="dd-MM-yyyy HH:mm" var="fechaFormateada" />
                                            <small class="text-muted">Último acceso: ${fechaFormateada}</small>
                                        </div>
                                    </button>
                                </form>
                            </div>
                        </c:when>


                        <c:otherwise>
                            <form action="FrontController" method="POST" class="mr-2">
                                <button type="submit" class="btn btn-outline-primary" name="boton" value="login">Login</button>
                                <button type="submit" class="btn btn-outline-secondary" name="boton" value="registro">Registrarse</button>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <!-- Botón del carrito -->
                    <form action="FrontController" method="POST" class="ml-3">
                        <button type="submit" class="btn btn-outline-success" name="boton" value="carrito"
                                <c:if test="${empty sessionScope.pedido.lineasPedidos and empty cookie.carrito}">disabled</c:if>>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cart" viewBox="0 0 16 16">
                            <path d="M0 1.5A.5.5 0 0 1 .5 1h1a.5.5 0 0 1 .485.379L2.89 5H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 14H4a.5.5 0 0 1-.491-.408L1.01 1.607 1.607 2H13a.5.5 0 0 1 0 1H2.89l-.48 2.405L1.607 2.121.5 1h-1zm4 11a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm8 0a1 1 0 1 0 0 2 1 1 0 0 0 0-2z"/>
                            </svg>
                        </button>
                    </form>
                </div>
            </div>
        </nav>
    </body>
</html>
