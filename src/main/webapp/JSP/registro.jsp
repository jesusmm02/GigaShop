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
        <script src="${contexto}/JS/validadorCorreo.js" defer></script>
        <script src="${contexto}/JS/generadorDni.js" defer></script>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>

        <div class="container mt-3">
            
            <%@ include file="/INC/barra.jsp" %>

            <!-- Mensaje flotante -->
            <c:if test="${not empty errorCreate}">
                <div id="mensajeFlotante" class="mensaje-flotante">
                    ${errorCreate}
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
                    background-color: #f8d7da;
                    color: #721c24;
                    border-color: #f5c6cb;
                    position: fixed; /* Posición fija en la pantalla */
                    top: 70px; /* Ajusta este valor según la altura de tu barra */
                    right: 20px;
                    padding: 10px 20px;
                    border-radius: 5px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    z-index: 1050;
                    font-size: 14px;
                    animation: link 0.5s ease-out;
                    border: 1px solid transparent;
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



        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <h1 class="text-center mb-4">Registro de usuario</h1>

                    <form action="RegistroController" method="POST">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="email">Correo Electrónico:</label>
                                    <input type="email" id="email" name="email" class="form-control">
                                    <span id="estado"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="password">Contraseña:</label>
                                    <input type="password" id="password" name="password" class="form-control">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="nombre">Nombre:</label>
                                    <input type="text" id="nombre" name="nombre" class="form-control">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="confirmPassword">Confirmar Contraseña:</label>
                                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="apellidos">Apellidos:</label>
                                    <input type="text" id="apellidos" name="apellidos" class="form-control">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="nif">NIF:</label>
                                    <input type="text" id="dni" name="dni" class="form-control">
                                    <p id="mensaje"></p>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="telefono">Teléfono:</label>
                                    <input type="tel" id="telefono" name="telefono" class="form-control">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="direccion">Dirección:</label>
                                    <input type="text" id="direccion" name="direccion" class="form-control">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="codigoPostal">Código Postal:</label>
                                    <input type="text" id="codigoPostal" name="codigoPostal" class="form-control">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="provincia">Provincia:</label>
                                    <input type="text" id="provincia" name="provincia" class="form-control">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="localidad">Localidad:</label>
                                    <input type="text" id="localidad" name="localidad" class="form-control">
                                </div>
                            </div>

                            <!--
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="avatar">Avatar</label>
                                    <input type="file" id="avatar" name="avatar">
                                </div>
                            </div>
                            -->
                        </div>


                        <button type="submit" class="btn btn-primary btn-block mb-4">Crear cuenta</button>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
