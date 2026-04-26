<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 5 - Filters</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #17a2b8; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #17a2b8; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #17a2b8; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; }
        a { color: #17a2b8; }
        .info { background: #d1ecf1; border: 1px solid #bee5eb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #17a2b8; color: white; }
    </style>
</head>
<body>
    <h1>Ejercicio 5: Servlet Filters (Filtros HTTP)</h1>
    <p>Este ejercicio demuestra el uso de Jakarta Servlet Filters para interceptar peticiones HTTP.</p>
    
    <p class="info">
        <strong>Nota:</strong> Los filtros ya están activos. Revisa la consola para ver los logs.
    </p>

    <c:set var="tiempoInicio" value="${request.getAttribute('tiempoInicio')}" />
    
    <c:if test="${not empty tiempoInicio}">
        <c:set var="tiempoFin" value="<%= System.currentTimeMillis() %>" />
        <c:set var="duracion" value="${tiempoFin - tiempoInicio}" />
        <section>
            <h2>Tiempo de Procesamiento</h2>
            <div class="info">
                <p><strong>Tiempo inicio:</strong> ${tiempoInicio}</p>
                <p><strong>Tiempo fin:</strong> ${tiempoFin}</p>
                <p><strong>Duración:</strong> ${duracion} ms</p>
            </div>
        </section>
    </c:if>
    
    <section>
        <h2>1. Información de la Petción</h2>
        <table>
            <tr>
                <th>Propiedad</th>
                <th>Valor</th>
            </tr>
            <tr>
                <td>Método HTTP</td>
                <td><code>${pageContext.request.method}</code></td>
            </tr>
            <tr>
                <td>URL</td>
                <td><code>${pageContext.request.requestURL}</code></td>
            </tr>
            <tr>
                <td>URI</td>
                <td><code>${pageContext.request.requestURI}</code></td>
            </tr>
            <tr>
                <td>Context Path</td>
                <td><code>${pageContext.request.contextPath}</code></td>
            </tr>
            <tr>
                <td>Servlet Path</td>
                <td><code>${pageContext.request.servletPath}</code></td>
            </tr>
            <tr>
                <td>Remote Address</td>
                <td><code>${pageContext.request.remoteAddr}</code></td>
            </tr>
            <tr>
                <td>Remote Host</td>
                <td><code>${pageContext.request.remoteHost}</code></td>
            </tr>
            <tr>
                <td>Protocol</td>
                <td><code>${pageContext.request.protocol}</code></td>
            </tr>
            <tr>
                <td>Session ID</td>
                <td><code>${pageContext.request.session.id}</code></td>
            </tr>
        </table>
    </section>
    
    <section>
        <h2>2. Headers de la Petción</h2>
        <table>
            <tr>
                <th>Header</th>
                <th>Valor</th>
            </tr>
            <c:forEach var="header" items="${header}">
                <tr>
                    <td><code>${header.key}</code></td>
                    <td>${header.value}</td>
                </tr>
            </c:forEach>
        </table>
    </section>
    
    <section>
        <h2>3. Parámetros de la Petción</h2>
        <table>
            <tr>
                <th>Parámetro</th>
                <th>Valor</th>
            </tr>
            <c:forEach var="param" items="${param}">
                <tr>
                    <td><code>${param.key}</code></td>
                    <td>${param.value}</td>
                </tr>
            </c:forEach>
        </table>
    </section>
    
    <section>
        <h2>4. Filtros Definidos</h2>
        <table>
            <tr>
                <th>Filter</th>
                <th>Descripción</th>
                <th>URL Pattern</th>
            </tr>
            <tr>
                <td><strong>LogFilter</strong></td>
                <td>Registra info de requests en consola</td>
                <td><code>/*</code> (todas las URLs)</td>
            </tr>
            <tr>
                <td><strong>EncodingFilter</strong></td>
                <td>Establece UTF-8 para request/response</td>
                <td><code>/*</code></td>
            </tr>
            <tr>
                <td><strong>TiempoFilter</strong></td>
                <td>Mide tiempo de procesamiento</td>
                <td><code>/*</code></td>
            </tr>
        </table>
    </section>
    
    <section>
        <h2>5. Anatomía de un Filter</h2>
        <pre>
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class MiFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        // Se ejecuta al iniciar el filtro
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        // Pre-procesamiento
        // Modificar request o response
        
        chain.doFilter(req, res); // Pasar al siguiente filtro/servlet
        
        // Post-procesamiento
        // Modificar response
    }

    @Override
    public void destroy() {
        // Se ejecuta al destruir el filtro
    }
}
        </pre>
    </section>
    
    <section>
        <h2>6. Filtros Comunes</h2>
        <table>
            <tr>
                <th>Tipo</th>
                <th>Descripción</th>
            </tr>
            <tr>
                <td>Authentication Filter</td>
                <td>Verifica credenciales JWT/Basic Auth</td>
            </tr>
            <tr>
                <td>CORS Filter</td>
                <td>Maneja headers CORS</td>
            </tr>
            <tr>
                <td>Compression Filter</td>
                <td>Comprime respuestas GZIP</td>
            </tr>
            <tr>
                <td>Cache Filter</td>
                <td>Controla caché del navegador</td>
            </tr>
            <tr>
                <td>Security Filter</td>
                <td>Valida headers de seguridad</td>
            </tr>
        </table>
    </section>
    
    <c:set var="nota" value="El orden de ejecución de filtros depende del mapeo en web.xml o @WebFilter" />
    <div class="info" style="background:#fff3cd;border-color:#ffc107;">
        <strong>Nota:</strong> ${nota}
    </div>
    
    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio3.jsp">Ejercicio 3 - CDI</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio4.jsp">Ejercicio 4 - Bean Validation</a>
    </p>
</body>
</html>