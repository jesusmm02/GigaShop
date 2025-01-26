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
        <script src="${contexto}/JS/validarAvatar.js" defer></script>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>
        <%@ include file="/INC/barra.jsp" %>
        
        <c:if test="${not empty error}">
            <div id="mensajeFlotante" class="mensaje-flotante">
                <c:choose>
                        <c:when test="${not empty error}">
                            ${error}
                        </c:when>
                    </c:choose>
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

        <div class="container mt-5">
            <h2>Editar Perfil</h2>
            <form action="UsuarioController" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="accion" value="actualizarPerfil">


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Email</strong></label>
                        <input type="email" class="form-control" value="${usuario.email}" readonly>
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>NIF</strong></label>
                        <input type="text" class="form-control" value="${usuario.nif}" readonly>
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Nombre</strong></label>
                        <input type="text" class="form-control" name="nombre" value="${usuario.nombre}">
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>Apellidos</strong></label>
                        <input type="text" class="form-control" name="apellidos" value="${usuario.apellidos}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Teléfono</strong></label>
                        <input type="tel" class="form-control" name="telefono" value="${usuario.telefono}">
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>Dirección</strong></label>
                        <input type="text" class="form-control" name="direccion" value="${usuario.direccion}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Código Postal</strong></label>
                        <input type="text" class="form-control" name="codigoPostal" value="${usuario.codigoPostal}">
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>Provincia</strong></label>
                        <input type="text" class="form-control" name="provincia" value="${usuario.provincia}">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Localidad</strong></label>
                        <input type="text" class="form-control" name="localidad" value="${usuario.localidad}">
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>Contraseña Actual</strong></label>
                        <input type="password" class="form-control" name="passwordActual">
                    </div>
                </div>


                <div class="row">
                    <div class="form-group col-md-6">
                        <label><strong>Nueva Contraseña</strong></label>
                        <input type="password" class="form-control" name="passwordNueva1">
                    </div>
                    <div class="form-group col-md-6">
                        <label><strong>Repetir Nueva Contraseña</strong></label>
                        <input type="password" class="form-control" name="passwordNueva2">
                    </div>
                </div>

                <div class="row justify-content-center">
                    <div class="form-group col-md-6 text-center">
                        <label for="avatar">
                            <c:if test="${not empty usuario.avatar}">
                                <div class="form-group">
                                    <label><strong>Avatar Actual</strong></label>
                                    <p>${usuario.avatar}</p> <!-- Nombre del avatar actual -->
                                </div>
                            </c:if>
                        </label>
                        <div class="border p-3 rounded bg-light">
                            <!-- Input de archivo dentro de un cuadro -->
                            <input type="file" id="avatar" name="avatar" accept="image/*" value="${usuario.avatar}" class="form-control-file mx-auto">
                        </div>
                        <p id="avatarError" style="color: red"></p>
                    </div>
                </div>

                <!-- Botones -->
                <div class="row">
                    <div class="col-md-12 text-center">
                        <button type="submit" class="btn btn-primary" name="accion" value="actualizarPerfil">Guardar Cambios</button>
                        <a href="FrontController" class="btn btn-secondary">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>

        <!-- Modal para pedidos finalizados -->
        <div class="modal fade" id="modalPedidosFinalizados" tabindex="-1" aria-labelledby="modalPedidosFinalizadosLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalPedidosFinalizadosLabel">Pedidos Finalizados</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <c:if test="${not empty sessionScope.pedidosFinalizados}">
                            <div class="accordion" id="accordionPedidos">
                                <c:forEach var="pedido" items="${sessionScope.pedidosFinalizados}">
                                    <div class="accordion-item">
                                        <h2 class="accordion-header" id="heading${pedido.idPedido}">
                                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                                    data-bs-target="#collapse${pedido.idPedido}" aria-expanded="false"
                                                    aria-controls="collapse${pedido.idPedido}">
                                                Pedido #${pedido.idPedido} - Fecha: ${pedido.fecha}
                                            </button>
                                        </h2>
                                        <div id="collapse${pedido.idPedido}" class="accordion-collapse collapse"
                                             aria-labelledby="heading${pedido.idPedido}" data-bs-parent="#accordionPedidos">
                                            <div class="accordion-body">
                                                <table class="table table-striped">
                                                    <thead>
                                                        <tr>
                                                            <th></th>
                                                            <th>Producto</th>
                                                            <th>Precio unitario</th>
                                                            <th>Cantidad</th>
                                                            <th>Total</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="linea" items="${pedido.lineasPedidos}">
                                                            <tr>
                                                                <td><img src="${contexto}/IMG/productos/${linea.producto.imagen}.jpg" class="img-fluid" style="max-width: 50px; height: auto;"/></td>
                                                                <td>${linea.producto.nombre}</td>                                 
                                                                <td>
                                                                    <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€"/>
                                                                </td>
                                                                <td>${linea.cantidad}</td>
                                                                <td>
                                                                    <fmt:formatNumber value="${linea.cantidad * linea.producto.precio}" type="currency" currencySymbol="€"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>

                                                <!-- Tabla de Totales -->
                                                <table class="table w-50 ml-auto bg-light shadow-sm border rounded">
                                                    <tbody>
                                                        <!-- Base imponible -->
                                                        <tr>
                                                            <td class="text-right font-weight-bold border-0">Base imponible:</td>
                                                            <td class="text-right border-0">
                                                                <fmt:formatNumber value="${pedido.importe}" type="currency" currencySymbol="€"/>
                                                            </td>
                                                        </tr>

                                                        <!-- IVA -->
                                                        <tr>
                                                            <td class="text-right font-weight-bold">IVA (21%):</td>
                                                            <td class="text-right">
                                                                <fmt:formatNumber value="${pedido.importe * 0.21}" type="currency" currencySymbol="€"/>
                                                            </td>
                                                        </tr>

                                                        <!-- Total a pagar -->
                                                        <tr class="bg-secondary text-white">
                                                            <td class="text-right font-weight-bold h5 mb-0">Total a pagar:</td>
                                                            <td class="text-right font-weight-bold">
                                                                <fmt:formatNumber value="${pedido.importe * 1.21}" type="currency" currencySymbol="€"/>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>

                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                        <c:if test="${empty sessionScope.pedidosFinalizados}">
                            <p>No hay pedidos finalizados disponibles.</p>
                        </c:if>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
