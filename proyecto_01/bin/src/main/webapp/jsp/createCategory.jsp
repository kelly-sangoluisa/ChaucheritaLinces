<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear Categoría</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/create.css">
</head>
<body>
    <div class="form-container">
        <h1>Crear Nueva Categoría</h1>
        <form action="ContabilidadController?ruta=createCategory" method="post">
            <input type="hidden" name="ruta" value="crearCategoria">
            
            <label for="tipoCategoria">Tipo de Categoría:</label>
            <select name="tipoCategoria" id="tipoCategoria" required>
                <option value="egreso">Egreso</option>
                <option value="ingreso">Ingreso</option>
            </select>
            <br><br>
            
            <label for="nombreCategoria">Nombre de la Categoría:</label>
            <input type="text" id="nombreCategoria" name="nombreCategoria" required>
            <br><br>
            
            <input type="submit" value="Crear Categoría">
        </form>
    </div>
</body>
</html>
