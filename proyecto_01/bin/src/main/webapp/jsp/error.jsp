<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
</head>
<body>
    <div class="container">
        <h1>Ha ocurrido un error</h1>
        <p>
            <%-- Muestra el mensaje de error que se pasó desde el controlador --%>
            <%= request.getAttribute("errorMessage") %>
        </p>
        <div class="actions">
            <%-- Botón para regresar a la página anterior --%>
            <a href="javascript:history.back()">Regresar</a>
            
            <%-- Opción para volver al dashboard o a otra página, si corresponde --%>
            <a href="javascript:history.go(-2)">Cancelar</a>
        </div>
    </div>
</body>
</html>
