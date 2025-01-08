<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <title>Giga Shop</title>
        <link rel="stylesheet" href="${bootstrap}" />
        <style>
            .btn:disabled {
                background-color: green;
                opacity: 0.3;
                color: white;
                border-color: green;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>

        <!-- Barra superior -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container">
                
                <!-- Barra de búsqueda -->
                <form class="form-inline my-2 my-lg-0" action="${contexto}/FrontController" method="GET" style="max-width: 400px; width: 100%;">
                    <input class="form-control mr-sm-2" type="search" placeholder="Buscar por descripción..." aria-label="Buscar" name="busqueda">
                    <button class="btn btn-outline-primary my-2 my-sm-0" type="submit">Buscar</button>
                </form>

                <!-- Botones -->
                <div class="d-flex align-items-center ml-auto">
                    <form action="FrontController" method="POST" class="mr-2">
                        <button type="submit" class="btn btn-outline-primary" name="boton" value="login">Login</button>
                        <button type="submit" class="btn btn-outline-secondary" name="boton" value="registro">Registrarse</button>
                        <button type="submit" class="btn btn-outline-success" name="boton" value="carrito"
                                <c:if test="${empty sessionScope.carrito}">disabled</c:if>>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cart" viewBox="0 0 16 16">
                                    <path d="M0 1.5A.5.5 0 0 1 .5 1h1a.5.5 0 0 1 .485.379L2.89 5H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 14H4a.5.5 0 0 1-.491-.408L1.01 1.607 1.607 2H13a.5.5 0 0 1 0 1H2.89l-.48 2.405L1.607 2.121.5 1h-1zm4 11a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm8 0a1 1 0 1 0 0 2 1 1 0 0 0 0-2z"/>
                                    </svg>
                                </button>
                    </form>

                    <!-- Icono de perfil de usuario -->
                    <div class="d-flex align-items-center ml-3">
                        <img src="../IMG/productos/defaulf.jpg" class="rounded-xl d-block">
                        <span class="ml-2 font-weight-bold">
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuario.nombre}">
                                    ${sessionScope.usuario.nombre}
                                </c:when>
                                <c:otherwise>
                                    Anónimo
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    
                </div>
            </div>
        </nav>



        <div class="container mt-4">
            <div class="row">
                <!-- Filtro en el lado izquierdo -->
                <div class="col-md-3" style="padding-left: 10px;">
                    <div class="form-group mb-3">
                        <label class="mr-5">Categoría:</label>
                        <select class="form-select" name="categorias">
                            <option selected>*Categorías*</option>
                            <option value="1">Placas Base</option>
                            <option value="2">Procesadores</option>
                            <option value="3">Discos Duros</option>
                            <option value="4">Intel Placas</option>
                            <option value="5">Amd Placas</option>
                            <option value="7">Memoria Ram</option>
                            <option value="9">Gráficas</option>
                            <option value="11">Sata</option>
                            <option value="13">Ssd</option>
                            <option value="14">Externos</option>
                            <option value="18">NVidia</option>
                            <option value="19">Intel Socket 1150</option>
                            <option value="20">Amd Socket AM3</option>
                            <option value="21">Ddr3</option>
                            <option value="22">Ddr4</option>
                            <option value="23">Otros</option>
                            <option value="24">Cajas</option>
                            <option value="25">Fuentes</option>
                            <option value="26">Periféricos</option>
                            <option value="27">Servicios</option>
                            <option value="28">Sobremesa</option>
                            <option value="29">Amd</option>
                            <option value="30">Portátiles</option>
                        </select>
                    </div>

                    <div class="form-group mb-3">
                        <label class="mr-5">Rango de precio:</label>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1">
                            <label class="form-check-label">
                                0 - 100 €
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" checked>
                            <label class="form-check-label">
                                101 - 200 €
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault3">
                            <label class="form-check-label">
                                201 - 300 €
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault4">
                            <label class="form-check-label">
                                301 - 500 €
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault5">
                            <label class="form-check-label">
                                Más de 500 €
                            </label>
                        </div>
                    </div>

                    <div class="form-group mb-3">
                        <label class="mr-5">Marca:</label>
                        <select class="form-select">
                            <option selected>*Marcas*</option>
                            <option value="1">Acer</option>
                            <option value="2">AeroCool</option>
                            <option value="3">Amd</option>
                            <option value="4">Apple</option>
                            <option value="5">ASRock</option>
                            <option value="6">Asus</option>
                            <option value="7">Corsair</option>
                            <option value="8">Creative</option>
                            <option value="9">Crucial</option>
                            <option value="10">G.Skill</option>
                            <option value="11">GigaByte</option>
                            <option value="12">Hitachi</option>
                            <option value="13">MSI</option>
                            <option value="14">Nullware</option>
                            <option value="15">PcCom</option>
                            <option value="16">Rapoo</option>
                            <option value="17">Sapphire</option>
                            <option value="18">Seagaye</option>
                            <option value="19">Sharkoon</option>
                            <option value="20">Thermaltake</option>
                            <option value="21">Toshiba</option>
                            <option value="22">WD</option>
                        </select>
                    </div>
                    <div class="dato">
                        <p class="error"><c:out value="${error}" default=""/></p>
                    </div>
                </div>

                <!-- Productos -->
                <div class="col-md-9">
                    <div class="container mt-4">
                        <div class="row">

                            <c:forEach var="producto" items="${productosAlt}">
                                <div class="col-md-4 mb-4">
                                    <div class="card" style="width: 100%; margin: auto;">
                                        <img src="${contexto}/IMG/productos/<c:out value='${producto.imagen}.jpg'/>" class="card-img-top">
                                        <div class="card-body">
                                            <h5 class="card-title"><c:out value="${producto.nombre}"/></h5>
                                            <p class="card-text text-primary">
                                                <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€" />
                                            </p>
                                            <form action="Cesta" method="POST">
                                                <button type="submit" name="annadir" value="${producto.idProducto}" class="btn btn-success">Añadir a la cesta</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                        </div>
                    </div>
                </div>


            </div>
        </div>

    </body>
</html>
