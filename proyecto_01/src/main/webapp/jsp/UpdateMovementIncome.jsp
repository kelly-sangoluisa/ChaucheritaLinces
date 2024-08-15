<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Actualizar Movimiento Ingreso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
    <div class="container">
        <h1>Actualizar Movimiento Ingreso</h1>
        <form action="ContabilidadController?ruta=updateMovement" method="post">
            <input type="hidden" name="idMovimiento" value="${movimiento.idMovement}">
            
            <label for="concepto">Concepto:</label>
            <input type="text" id="concepto" name="concepto" value="${movimiento.concept}" required>
            
            <label for="monto">Monto:</label>
            <input type="number" id="monto" name="monto" value="${movimiento.amount}" step="0.01" required>
            
            <label for="fecha">Fecha y Hora:</label>
            <input type="datetime-local" id="fecha" name="fecha" value="${fechaFormateada}" required>

            <!-- Para Ingreso, solo se muestra la cuenta de destino -->
            <label for="cuentaDestino">Cuenta de Destino:</label>
            <select id="cuentaDestino" name="cuentaDestino" required>
                <c:forEach var="cuenta" items="${cuentasDestino}">
                    <option value="${cuenta.idAccount}" ${cuenta.idAccount == destinoId ? 'selected' : ''}>
                        ${cuenta.nameAccount}
                    </option>
                </c:forEach>
            </select>

            <label for="categoria">Categoría:</label>
            <select id="categoria" name="categoria" required>
                <c:forEach var="categoria" items="${categoria}">
                    <option value="${categoria.idCategory}" ${categoria.idCategory == categoriaId ? 'selected' : ''}>
                        ${categoria.nameCategory}
                    </option>
                </c:forEach>
            </select>
            
            <input type="submit" value="Actualizar Movimiento">
        </form>
        <a href="javascript:history.back()" class="cancel-button">Cancelar</a>
    </div>
</body>
</html>
