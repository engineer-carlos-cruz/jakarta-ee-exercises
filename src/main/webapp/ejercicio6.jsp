<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 6 - Listeners</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #6f42c1; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #6f42c1; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #6f42c1; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; }
        a { color: #6f42c1; }
        .info { background: #d1ecf1; border: 1px solid #bee5eb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .stats { background: #e2e3e5; padding: 20px; border-radius: 5px; text-align: center; }
        .stats-number { font-size: 48px; color: #6f42c1; font-weight: bold; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #6f42c1; color: white; }
    </style>
</head>
<body>
    <h1>Ejercicio 6: Servlet Listeners</h1>
    <p>Este ejercicio demuestra el uso de listeners para detectar eventos del ciclo de vida.</p>

    <c:set var="inicioAplicacion" value="${applicationScope.inicioAplicacion}" />
    <c:set var="contadorRequests" value="${applicationScope.contadorRequests}" />
    <c:set var="contadorSesiones" value="${applicationScope.contadorSesiones}" />

    <section>
        <h2>1. Estadísticas de la Aplicación</h2>
        <div class="stats">
            <div style="display:inline-block;margin:20px;">
                <div class="stats-number">${contadorRequests != null ? contadorRequests : 0}</div>
                <div>Total Requests</div>
            </div>
            <div style="display:inline-block;margin:20px;">
                <div class="stats-number">${contadorSesiones != null ? contadorSesiones : 0}</div>
                <div>Sesiones Activas</div>
            </div>
        </div>
        
        <c:if test="${not empty inicioAplicacion}">
            <c:set var="uptime" value="<%= System.currentTimeMillis() - ((Long)request.getAttribute(\"inicioAplicacion\") != null ? (Long)request.getAttribute(\"inicioAplicacion\") : System.currentTimeMillis()) %>" />
            <div class="info">
                <p><strong> aplicación iniciada:</strong> <fmt:formatDate value="${new java.util.Date(inicioAplicacion)}" type="both" /></p>
            </div>
        </c:if>
    </section>

    <section>
        <h2>2. Información de tu Sesión</h2>
        <table>
            <tr>
                <th>Propiedad</th>
                <th>Valor</th>
            </tr>
            <tr>
                <td>ID de Sesión</td>
                <td><code>${pageContext.session.id}</code></td>
            </tr>
            <tr>
                <td>Creada</td>
                <td><c:if test="${not empty session.fechaCreacion}">
                    <fmt:formatDate value="${new java.util.Date(session.fechaCreacion)}" type="both" />
                </c:if></td>
            </tr>
            <tr>
                <td>Max Inactive Interval</td>
                <td>${pageContext.session.maxInactiveInterval} segundos</td>
            </tr>
            <tr>
                <td>Is New</td>
                <td>${pageContext.session.isNew()}</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>3. Atributos de tu Sesión</h2>
        <table>
            <tr>
                <th>Atributo</th>
                <th>Valor</th>
            </tr>
            <c:forEach var="attr" items="${sessionScope}">
                <tr>
                    <td><code>${attr.key}</code></td>
                    <td>${attr.value}</td>
                </tr>
            </c:forEach>
        </table>
    </section>

    <section>
        <h2>4. Listeners Definidos</h2>
        <table>
            <tr>
                <th>Listener</th>
                <th>Interfaz</th>
                <th>Descripción</th>
            </tr>
            <tr>
                <td><strong>AppContextListener</strong></td>
                <td><code>ServletContextListener</code></td>
                <td>Inicializa/limpia recursos al iniciar/finalizar aplicación</td>
            </tr>
            <tr>
                <td><strong>SessionListener</strong></td>
                <td><code>HttpSessionListener</code></td>
                <td>Cuenta sesiones activas, detecta creación/destrucción</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>5. Tipos de Listeners</h2>
        <table>
            <tr>
                <th>Interfaz</th>
                <th>Evento</th>
            </tr>
            <tr>
                <td><code>ServletContextListener</code></td>
                <td>contextInitialized, contextDestroyed</td>
            </tr>
            <tr>
                <td><code>ServletContextAttributeListener</code></td>
                <td>attributeAdded, removed, replaced</td>
            </tr>
            <tr>
                <td><code>HttpSessionListener</code></td>
                <td>sessionCreated, sessionDestroyed</td>
            </tr>
            <tr>
                <td><code>HttpSessionAttributeListener</code></td>
                <td>attributeAdded, removed, replaced</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>6. Anatomía de un Listener</h2>
        <pre>
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

@WebListener
public class MiListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Se ejecuta cuando se crea una nueva sesión
        HttpSession session = se.getSession();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Se ejecuta cuando se destruye una sesión
    }
}
        </pre>
    </section>

    <section>
        <h2>7. Acciones</h2>
        <p>
            <a href="?accion=incrementar" class="btn">Incrementar Contador</a>
            <a href="?accion=invalidar" class="btn">Invalidar Sesión</a>
            <a href="?accion=nueva" class="btn">Nueva Sesión</a>
        </p>
        
        <c:set var="accion" value="${param.accion}" />
        <c:if test="${accion == 'incrementar'}">
            <c:set var="contador" value="${sessionScope.contador != null ? sessionScope.contador : 0}" />
            <c:set var="nuevoContador" value="${contador + 1}" scope="session" />
            <div class="info">
                <p>Contador en sesión: <strong>${nuevoContador}</strong></p>
            </div>
        </c:if>
        <c:if test="${accion == 'invalidar'}">
            <% session.invalidate(); %>
            <div class="info">
                <p>Sesión invalidada. <a href="?accion=nueva">Crear nueva</a></p>
            </div>
        </c:if>
    </section>

    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio3.jsp">Ejercicio 3 - CDI</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio4.jsp">Ejercicio 4 - Bean Validation</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio5.jsp">Ejercicio 5 - Filters</a>
    </p>
</body>
</html>