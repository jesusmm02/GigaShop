<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bootstrap" value="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" scope="application" />
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <meta http-equiv="refresh" content="1;url=${contexto}/FrontController">
        <title>Giga Shop</title>
        <link rel="stylesheet" href="${bootstrap}" />
        <style>
            .contenedorPortada {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                width: 100vw;
                margin: 0;
                padding: 0;
            }
            h1 {
                color: #04254b;
            }
        </style>
    </head>
    <body>
        <div class="contenedorPortada">
            <img src="IMG/logotipo.png" class="img-fluid w-60">
            <h1>GIGA <br> 
                SHOP</h1>
        </div>
    </body>
</html>
