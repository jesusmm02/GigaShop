<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/INC/metas.inc"/>
    <title>Giga Shop - Registro</title>
    <link rel="stylesheet" href="${bootstrap}" />
</head>
<body>
    <jsp:include page="/INC/cabecera.inc"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <h1 class="text-center mb-4">Registro de usuario</h1>

                <form action="RegistroController" method="POST">
                    <div class="form-group">
                        <label for="email">Correo Electrónico:</label>
                        <input type="email" id="email" name="email" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="password">Contraseña:</label>
                        <input type="password" id="password" name="password" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirmar Contraseña:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="nombre">Nombre:</label>
                        <input type="text" id="nombre" name="nombre" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="apellidos">Apellidos:</label>
                        <input type="text" id="apellidos" name="apellidos" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="nif">NIF:</label>
                        <input type="text" id="nif" name="nif" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="telefono">Teléfono:</label>
                        <input type="text" id="telefono" name="telefono" class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="telefono">Dirección:</label>
                        <input type="text" id="direccion" name="direccion" class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="telefono">Código Postak:</label>
                        <input type="text" id="codigoPostal" name="codigoPostal" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="provincia">Localidad:</label>
                        <input type="text" id="localidad" name="localidad" class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="localidad">Provincia:</label>
                        <input type="text" id="provincia" name="provincia" class="form-control">
                    </div>

                    <div class="dato">
                        <p class="error"><c:out value="${errorCreate}" default=""/></p>
                    </div>
                    
                    <div class="dato">
                        <p class="aviso"><c:out value="${aviso}" default=""/></p>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">Crear cuenta</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
