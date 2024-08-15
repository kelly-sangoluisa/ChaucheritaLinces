<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear un nuevo Usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create.css">
</head>
<body>
    <div class="form-container">
        <h1>Nuevo Usuario</h1>
        <form method="POST" action="../ContabilidadController?ruta=createUser">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" required>

            <label for="clave">Password:</label>
            <input type="password" id="clave" name="clave" required>

            <input type="submit" value="Registrar">
        </form>

        <div class="actions-container">
            <a href="javascript:history.back()">Cancelar</a>
        </div>
    </div>
</body>
</html>
