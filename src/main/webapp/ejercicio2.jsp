<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 2 - JSTL</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #007bff; color: white; }
        .precio { color: green; font-weight: bold; }
        .bajo { color: green; }
        .medio { color: orange; }
        .alto { color: red; }
        .btn { background: #007bff; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; }
        a { color: #007bff; }
    </style>
</head>
<body>
    <h1>Ejercicio 2: JSTL (JSP Standard Tag Library)</h1>
    
    <c:set var="usuario" value="${param.nombre}" />
    <c:set var="productos">
        [{"nombre":"Laptop","precio":1200.00,"stock":5},
         {"nombre":"Mouse","precio":25.50,"stock":0},
         {"nombre":"Teclado","precio":45.00,"stock":15},
         {"nombre":"Monitor","precio":299.99,"stock":3},
         {"nombre":"USB-C Cable","precio":15.00,"stock":50}]
    </c:set>
    
    <c:set var="numero" value="${param.numero != null ? param.numero : 7}" />
    
    <section>
        <h2>1. if - Condicional</h2>
        <c:if test="${not empty param.nombre}">
            <p>Hola, <strong>${param.nombre}</strong>! Bienvenido al sitio.</p>
        </c:if>
        <c:if test="${empty param.nombre}">
            <p>Por favor ingresa tu nombre en la URL: ?nombre=TuNombre</p>
        </c:if>
        <a href="?nombre=Juan" class="btn">Probar con "Juan"</a>
    </section>
    
    <section>
        <h2>2. choose - Condicionales Múltiples</h2>
        <p>Número: ${numero}</p>
        <c:choose>
            <c:when test="${numero >= 10}">
                <span style="color: green; font-weight: bold;">ES MAYOR O IGUAL A 10</span>
            </c:when>
            <c:when test="${numero >= 5}">
                <span style="color: orange; font-weight: bold;">ESTÁ ENTRE 5 Y 9</span>
            </c:when>
            <c:otherwise>
                <span style="color: red; font-weight: bold;">ES MENOR A 5</span>
            </c:otherwise>
        </c:choose>
        <br><br>
        <a href="?numero=3" class="btn">3</a>
        <a href="?numero=7" class="btn">7</a>
        <a href="?numero=12" class="btn">12</a>
    </section>
    
    <section>
        <h2>3. forEach - Iteración</h2>
        <table>
            <tr>
                <th>Producto</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Estado</th>
            </tr>
            <c:forEach var="i" begin="0" end="4">
                <c:set var="producto" value="${['Laptop','Mouse','Teclado','Monitor','USB-C Cable'][i]}" />
                <c:set var="precio" value="${[1200.00,25.50,45.00,299.99,15.00][i]}" />
                <c:set var="stock" value="${[5,0,15,3,50][i]}" />
                <tr>
                    <td>${producto}</td>
                    <td class="precio">$<fmt:formatNumber value="${precio}" pattern="#,##0.00" /></td>
                    <td>${stock}</td>
                    <td>
                        <c:choose>
                            <c:when test="${stock == 0}"><span class="alto">AGOTADO</span></c:when>
                            <c:when test="${stock < 5}"><span class="medio">ÚLTIMAS UNIDADES</span></c:when>
                            <c:otherwise><span class="bajo">DISPONIBLE</span></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </section>
    
    <section>
        <h2>4. forTokens - Iterar por delimitadores</h2>
        <c:set var="colores" value="rojo,verde,azul,amarillo,negro" />
        <p>Colores: 
        <c:forTokens var="color" items="${colores}" delims=",">
            <span style="color:${color}">${color}</span><c:if test="${color != 'negro'}">, </c:if>
        </c:forTokens>
        </p>
    </section>
    
    <section>
        <h2>5. fmt:formatNumber - Formato de números</h2>
        <c:set var="pi" value="${3.14159265}" />
        <c:set var="precio" value="${1234.56}" />
        <c:set var="descuento" value="${0.25}" />
        
        <p>Pi: <fmt:formatNumber value="${pi}" pattern="#.#####" /> (5 decimales)</p>
        <p>Precio: <fmt:formatNumber value="${precio}" type="currency" currencyCode="USD" /> (moneda)</p>
        <p>Descuento: <fmt:formatNumber value="${descuento}" type="percent" /> (porcentaje)</p>
        <p>Entero: <fmt:formatNumber value="${precio}" type="number" /> (número)</p>
    </section>
    
    <section>
        <h2>6. fmt:formatDate - Formato de fechas</h2>
        <c:set var="ahora" value="<%= new java.util.Date() %>" />
        
        <p>Fecha completa: <fmt:formatDate value="${ahora}" type="both" dateStyle="full" timeStyle="full" /></p>
        <p>Fecha corta: <fmt:formatDate value="${ahora}" type="date" dateStyle="short" /></p>
        <p>Hora: <fmt:formatDate value="${ahora}" type="time" timeStyle="short" /></p>
    </section>
    
    <section>
        <h2>7. c:set y c:out - Establecer y mostrar valores</h2>
        <c:set var="mensaje" value="Hola desde JSTL" />
        <p>Con c:out: <c:out value="${mensaje}" /></p>
        <p>Con EL directo: ${mensaje}</p>
        <c:set var="mensaje" value="Mensaje modificado" scope="page" />
        <p>Modificado (page scope): ${mensaje}</p>
    </section>
    
    <section>
        <h2>8. c:url - Construir URLs</h2>
        <p>URL con parámetros: <c:url value="/index.jsp"><c:param name="nombre" value="Admin"/><c:param name="numero" value="42"/></c:url></p>
    </section>
    
    <hr>
    <p><a href="${pageContext.servletContext.contextPath}/index.jsp">Volver al Ejercicio 1</a></p>
</body>
</html>