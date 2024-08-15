<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/viewDashboard.css">
</head>
<body>
    <br>
    
    <div class="encabezado">
        <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
        <div class="title-encabezado">
            <h1>Estado de la Contabilidad</h1>
            <c:if test="${param.mensaje != null}">
                <p style="color: green;">${param.mensaje}</p>
            </c:if>
        </div>
        <img src="images/logo-Chaucherita.webp" alt="Chaucherita Linces Logo">
 
    </div>

    <br>

    <div class="filtro-fecha">
        <%@include file="../template/fecha.html" %>
    </div>
    

    <div class="footer-buttons">
        <button id="create-Account" onclick="location.href='ContabilidadController?ruta=showFormAccount'">Crear Cuenta</button>
        <button id="create-Category" onclick="location.href='ContabilidadController?ruta=showFormCategory'">Crear Categoría</button>
        <button id="log-out" onclick="location.href='ContabilidadController?ruta=cerrarSesion'">Cerrar Sesión</button>
    </div>

    <br>
    <div class="container">
        <div class="section">
            <h2 id="title-consolidated">Consolidado de las Cuentas</h2>
            <div class="table-responsive">
                <table>
                    <thead class="th-consolidated">
                        <tr>
                            <th>Nombre</th>
                            <th>Saldo</th>
                            <th colspan="3">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cuenta" items="${cuentas}">
                            <tr class="tr-consolidated">
                                <td><a href="ContabilidadController?ruta=showaccount&cuentaId=${cuenta.idAccount}">${cuenta.nameAccount}</a></td>
                                <td>${cuenta.total}</td>
                                <td><a href="ContabilidadController?ruta=registerFormIncome&cuentaId=${cuenta.idAccount}&origen=showdashboard">Registrar un Nuevo Ingreso</a></td>
                                <td><a href="ContabilidadController?ruta=registerFormOutcome&cuentaId=${cuenta.idAccount}&origen=showdashboard">Registrar un Nuevo Egreso</a></td>
                                <td><a href="ContabilidadController?ruta=registerFormTransference&cuentaId=${cuenta.idAccount}&origen=showdashboard">Registrar una Nueva Transferencia</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section">
            <h2 id="title-income">Categoría de Ingreso</h2>
            <div class="table-responsive">
                <table>
                    <thead class="th-income">
                        <tr>
                            <th>Nombre de Categoría</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="categoria" items="${ingresos}">
                            <tr class="tr-income">
                                <td><a href="ContabilidadController?ruta=showcategory&categoriaId=${categoria.idCategory}">${categoria.nameCategory}</a></td>
                                <td>${totalIngresos[categoria.nameCategory]}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section">
            <h2 id="title-outcome">Categoría de Egreso</h2>
            <div class="table-responsive">
                <table>
                    <thead class="th-outcome">
                        <tr>
                            <th>Nombre de Categoría</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="categoria" items="${egresos}">
                            <tr class="tr-outcome">
                                <td><a href="ContabilidadController?ruta=showcategory&categoriaId=${categoria.idCategory}">${categoria.nameCategory}</a></td>
                                <td>${totalEgresos[categoria.nameCategory]}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section">
            <h2 id="title-trans">Categoría de Transferencia</h2>
            <div class="table-responsive">
                <table>
                    <thead class="th-trans">
                        <tr>
                            <th>Nombre de Categoría</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="categoria" items="${transferencias}">
                            <tr class="tr-trans">
                                <td><a href="ContabilidadController?ruta=showcategory&categoriaId=${categoria.idCategory}">${categoria.nameCategory}</a></td>
                                <td>${totalTransferencias[categoria.nameCategory]}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section section-full">
            <h2 id="title-consolidated">Movimientos</h2>
            <div class="table-responsive">
                <table>
                    <thead class="th-consolidated">
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
                                    <div class="actions">
                                        <a href="ContabilidadController?ruta=UpdateFormMovement&idMovimiento=${movimiento.idMovement}" class="editar">Editar</a>
                                        <a href="#" class="eliminarMovimiento" data-id="${movimiento.idMovement}" data-nombre="${movimiento.concept}">Eliminar</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll(".eliminarCuenta").forEach(function(eliminarLink) {
                eliminarLink.addEventListener("click", function(event) {
                    event.preventDefault();
                    
                    var idCuenta = this.getAttribute("data-id");
                    var nombreCuenta = this.getAttribute("data-nombre");
                   
                    
                    var confirmacion = confirm("¿Desea eliminar esta cuenta: " + nombreCuenta + "?");
                    
                    if (confirmacion) {
                        // Redirige al controlador para eliminar la cuenta
                        window.location.href = "ContabilidadController?ruta=eliminarCuenta&idCuenta=" + idCuenta ;
                    }
                });
            });
        });
        
        
        
        
        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll(".eliminarCategoria").forEach(function(eliminarLink) {
                eliminarLink.addEventListener("click", function(event) {
                    event.preventDefault();
                    
                    var idCategoria = this.getAttribute("data-id");
                    var nombreCategoria = this.getAttribute("data-nombre");
                    
                    var confirmacion = confirm("¿Desea eliminar esta categoría: " + nombreCategoria + "?");
                    
                    if (confirmacion) {
                        // Redirige al controlador para eliminar la categoría
                        window.location.href = "ContabilidadController?ruta=eliminarCategoria&idCategoria=" + idCategoria;
                    }
                });
            });
        });
        
        
        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll(".deleteMovement").forEach(function(eliminarLink) {
                eliminarLink.addEventListener("click", function(event) {
                    event.preventDefault();
                    
                    var idMovimiento = this.getAttribute("data-id");
                    var conceptoMovimiento = this.getAttribute("data-nombre");
                    var origen = "verDashboard";
                    
                    var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");
                    
                    if (confirmacion) {
                        // Redirige al controlador para eliminar la categoría
                        window.location.href = "ContabilidadController?ruta=deleteMovement&idMovimiento=" + idMovimiento + "&vistaOrigen=" + encodeURIComponent(origen);
                    }
                });
            });
        });
        
        
    </script>
</body>
</html>
