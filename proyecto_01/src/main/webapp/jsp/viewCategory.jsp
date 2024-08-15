<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalles de Categoría</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/viewCategory.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
            <div class="title-header">
                <h1>Detalles de la Categoría</h1>
            </div>
            <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
        </div>

        <div class="filtro-fecha">
            <%@include file="../template/fecha.html" %>
        </div>
        

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Nombre de la Categoría</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${categoria.nameCategory}</td>
                        <td>${total}</td>
                    </tr>
                </tbody>
            </table>
        </div>

    
        <div class="table-responsive">
            <h2>Movimientos Asociados</h2>
            <table>
                <thead>
                    <tr>
                        <th>Concepto</th>
                        <th>Fecha</th>
                        <th>Monto</th>
                        <th>Cuenta Origen</th>
                        <th>Cuenta Destino</th>
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
                            <td>
                                <a href="ContabilidadController?ruta=UpdateFormMovement&idMovimiento=${movimiento.idMovement}" class="editar">Editar</a>
                                <a href="#" class="eliminarMovimiento" data-idCategoria="${categoria.idCategory}" data-id="${movimiento.idMovement}" data-nombre="${movimiento.concept}">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="actions-container">
            <a href="ContabilidadController?ruta=showdashboard">Regresar</a>
        </div>
    </div>

    
</body>
<script type="text/javascript">

    document.addEventListener("DOMContentLoaded", function() {
        document.querySelectorAll(".deleteMovement").forEach(function(eliminarLink) {
            eliminarLink.addEventListener("click", function(event) {
                event.preventDefault();
                
                var idMovimiento = this.getAttribute("data-id");
                var conceptoMovimiento = this.getAttribute("data-nombre");
                var  idCategoria =  this.getAttribute("data-idCategoria");
                var origen = "mostrarCategoria";
                
                var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");
                
                if (confirmacion) {
                    // Redirige al controlador para eliminar la categoría
                    window.location.href = "ContabilidadController?ruta=deleteMovement&idMovimiento=" + idMovimiento + "&idCategoria="+ idCategoria + "&vistaOrigen=" + origen;
                }
            });
        });
    });
    
    </script>
</html>
