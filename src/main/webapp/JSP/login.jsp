<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <title>Giga Shop - Login</title>
        <link rel="stylesheet" href="${bootstrap}" />
    </head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <h1 class="text-center mb-4">Iniciar sesión</h1>

                <form action="LoginController" method="POST" class="needs-validation" novalidate>
                    <div class="form-group">
                        <label for="email">Correo Electrónico:</label>
                        <input type="email" id="email" name="email" class="form-control" required>
                        <div class="invalid-feedback">Por favor, introduce un correo válido.</div>
                    </div>

                    <div class="form-group">
                        <label for="password">Contraseña:</label>
                        <input type="password" id="password" name="password" class="form-control" required>
                        <div class="invalid-feedback">Por favor, introduce tu contraseña.</div>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">Iniciar sesión</button>
                </form>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger mt-3" role="alert">
                        <p>${error}</p>
                    </div>
                </c:if>

                <p class="text-center mt-3">¿No tienes cuenta? <a href="register.jsp">Regístrate aquí</a>.</p>
            </div>
        </div>
    </div>
</body>
</html>
