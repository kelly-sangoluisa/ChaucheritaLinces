<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle de Cuenta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/viewAccount.css">
</head>
<body>

    <div class="container">

        <div class="header">
            <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
            <div class="title-header">
                <h1>Detalles de la Cuenta</h1>
                <!-- Mostrar mensaje de éxito si está presente -->
                <c:if test="${param.mensaje != null}">
                    <p style="color: green;">${param.mensaje}</p>
                </c:if>
            </div>
            <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
        </div>

        <div class="filtro-fecha">
            <%@include file="../template/fecha.html" %>
        </div>

        <div class="actions-container">
            <a id="new-income" href="ContabilidadController?ruta=registerFormIncome&cuentaId=${cuenta.idAccount}">Registrar un Nuevo Ingreso</a>
            <a id="new-outcome" href="ContabilidadController?ruta=registerFormOutcome&cuentaId=${cuenta.idAccount}">Registrar un Nuevo Egreso</a>
            <a id="new-trans" href="ContabilidadController?ruta=registerFormTransference&cuentaId=${cuenta.idAccount}">Registrar una Nueva Transferencia</a>
            <a id="new-outcome" href="ContabilidadController?ruta=showdashboard">Regresar</a>
        </div>

        <div class="table-container">
            <h2>Cuenta: ${cuenta.nameAccount}</h2>
            <table>
                <thead>
                    <tr>
                        <th>Saldo</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${cuenta.total}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="table-responsive">
            <h2>Movimientos de la Cuenta</h2>
            <table>
                <thead>
                    <tr>
                        <th>Concepto</th>
                        <th>Fecha</th>
                        <th>Monto</th>
                        <th>Cuenta Origen</th>
                        <th>Cuenta Destino</th>
                        <th>Categoría</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="movimiento" items="${movimientos}">
                        <tr>
                            <td>${movimiento.concept}</td>
                            <td>${movimiento.date}</td>
                            <td>${movimiento.amount}</td>
                            <td>${movimiento.origin}</td> 
                            <td>${movimiento.destiny}</td>
                            <td>${movimiento.category}</td>
                            <td>
                                <a href="ContabilidadController?ruta=UpdateFormMovement&idMovimiento=${movimiento.idMovement}" class="editar">Editar</a>
                                <a href="#" class="eliminarMovimiento" data-idCuenta="${cuenta.idAccount}" data-id="${movimiento.idMovement}" data-nombre="${movimiento.concept}">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </div>

<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".eliminarMovimiento").forEach(function(eliminarLink) {
        eliminarLink.addEventListener("click", function(event) {
            event.preventDefault();
            
            var idMovimiento = this.getAttribute("data-id");
            var conceptoMovimiento = this.getAttribute("data-nombre");
            var idCuenta = this.getAttribute("data-idCuenta");
            var origen = "mostrarCuenta";
            
            var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");
            
            if (confirmacion) {
                window.location.href = "ContabilidadController?ruta=deleteMovement&idMovimiento=" + idMovimiento +"&idCuenta="+ idCuenta + "&vistaOrigen=" + origen;
            }
        });
    });
});
</script>

</body>
</html>
