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
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
                                                                <c:set var="baseImponible" value="0" />
                                                                <c:forEach var="linea" items="${pedido.lineasPedidos}">
                                                                    <c:set var="baseImponible" value="${baseImponible + (linea.producto.precio * linea.cantidad)}" />
                                                                </c:forEach>
                                                                <fmt:formatNumber value="${baseImponible}" type="currency" currencySymbol="€"/>
                                                            </td>
                                                        </tr>

                                                        <!-- IVA -->
                                                        <tr>
                                                            <td class="text-right font-weight-bold">IVA (21%):</td>
                                                            <td class="text-right">
                                                                <fmt:formatNumber value="${baseImponible * 0.21}" type="currency" currencySymbol="€"/>
                                                            </td>
                                                        </tr>

                                                        <!-- Total a pagar -->
                                                        <tr class="bg-secondary text-white">
                                                            <td class="text-right font-weight-bold h5 mb-0">Total a pagar:</td>
                                                            <td class="text-right font-weight-bold">
                                                                <fmt:formatNumber value="${baseImponible * 1.21}" type="currency" currencySymbol="€"/>
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
