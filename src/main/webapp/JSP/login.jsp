<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
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
        <jsp:include page="/INC/cabecera.inc"/>

        <div class="container mt-3">

            <%@ include file="/INC/barra.jsp" %>

            <c:if test="${not empty error}">
                <div id="mensajeFlotante" class="mensaje-flotante">
                    ${error}
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
        </div>

        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <h1 class="text-center mb-4">Iniciar sesión</h1>

                    <form action="UsuarioController" method="POST" class="needs-validation">
                        <div class="form-group">
                            <label for="email">Correo Electrónico:</label>
                            <input type="email" id="email" name="email" class="form-control">
                            <div class="invalid-feedback">Por favor, introduce un correo válido.</div>
                        </div>

                        <div class="form-group">
                            <label for="password">Contraseña:</label>
                            <input type="password" id="password" name="password" class="form-control">
                            <div class="invalid-feedback">Por favor, introduce tu contraseña.</div>
                        </div>

                        <button type="submit" name="accion" value="login" class="btn btn-primary btn-block">Iniciar sesión</button>
                    </form>
                    
                </div>
            </div>
        </div>
    </body>
</html>
