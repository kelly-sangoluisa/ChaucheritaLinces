<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear Cuenta</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/create.css">
</head>
<body>
    <div class="form-container">
        <h1>Crear Nueva Cuenta</h1>
        <form action="ContabilidadController?ruta=createAccount" method="post">
            
            <label for="nombre">Nombre del Propietario:</label>
            <input type="text" id="nombre" name="nombre" value="${usuario.userName}" readonly><br>
            
            <label for="nombreCuenta">Nombre de la Cuenta:</label>
            <input type="text" id="nombreCuenta" name="nombreCuenta" required><br>
            
            <label for="saldo">Saldo:</label>
            <input type="number" id="saldo" name="saldo" value="0" step="0.01" readonly><br>
            
            <input type="submit" value="Crear Cuenta">
        </form>
    </div>
</body>
</html>
