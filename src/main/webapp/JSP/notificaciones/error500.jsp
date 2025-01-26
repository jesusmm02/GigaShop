<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/INC/metas.inc"/>
        <title>ERROR 500 - Error del Servidor</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                color: #333;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
                text-align: center;
            }
            .container {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                max-width: 400px;
            }
            h1 {
                font-size: 2em;
                margin-bottom: 10px;
            }
            p {
                margin-bottom: 20px;
            }
            a {
                display: inline-block;
                background-color: #007BFF;
                color: white;
                text-decoration: none;
                padding: 10px 20px;
                font-size: 1em;
                border-radius: 5px;
                transition: background-color 0.3s;
            }
            a:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>
        <div class="container">
            <h1>500 - Error del Servidor</h1>
            <p>Ha ocurrido un error inesperado en el servidor. Por favor, inténtelo de nuevo más tarde.</p>
            <a href="${contexto}/FrontController">Volver al Inicio</a>
        </div>
    </body>
</html>
