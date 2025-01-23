<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <title>Giga Shop - Editar perfil</title>
        <link rel="stylesheet" href="${bootstrap}" />
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>
        
        <%@ include file="/INC/barra.jsp" %>
        
        <div class="container mt-5">
            <h2>Editar Perfil</h2>
            <form action="UsuarioController" method="POST">
                <input type="hidden" name="accion" value="actualizarPerfil">


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Email</label>
                        <input type="email" class="form-control" value="${usuario.email}" readonly>
                    </div>
                    <div class="form-group col-md-6">
                        <label>NIF</label>
                        <input type="text" class="form-control" value="${usuario.nif}" readonly>
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Nombre</label>
                        <input type="text" class="form-control" name="nombre" value="${usuario.nombre}">
                    </div>
                    <div class="form-group col-md-6">
                        <label>Apellidos</label>
                        <input type="text" class="form-control" name="apellidos" value="${usuario.apellidos}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Teléfono</label>
                        <input type="tel" class="form-control" name="telefono" value="${usuario.telefono}">
                    </div>
                    <div class="form-group col-md-6">
                        <label>Dirección</label>
                        <input type="text" class="form-control" name="direccion" value="${usuario.direccion}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Código Postal</label>
                        <input type="text" class="form-control" name="codigoPostal" value="${usuario.codigoPostal}">
                    </div>
                    <div class="form-group col-md-6">
                        <label>Provincia</label>
                        <input type="text" class="form-control" name="provincia" value="${usuario.provincia}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Localidad</label>
                        <input type="text" class="form-control" name="localidad" value="${usuario.localidad}">
                    </div>
                    <div class="form-group col-md-6">
                        <label>Contraseña Actual</label>
                        <input type="password" class="form-control" name="passwordActual">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label>Nueva Contraseña</label>
                        <input type="password" class="form-control" name="passwordNueva1">
                    </div>
                    <div class="form-group col-md-6">
                        <label>Repetir Nueva Contraseña</label>
                        <input type="password" class="form-control" name="passwordNueva2">
                    </div>
                </div>

                <c:if test="${not empty error}">
                    <p>${error}</p>
                </c:if>

                <c:if test="${not empty mensaje}">
                    <p>${mensaje}</p>
                </c:if>

                <!-- Botones -->
                <div class="row">
                    <div class="col-md-12 text-center">
                        <button type="submit" class="btn btn-primary" name="accion" value="actualizarPerfil">Guardar Cambios</button>
                        <a href="FrontController" class="btn btn-secondary">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
