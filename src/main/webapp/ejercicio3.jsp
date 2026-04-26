<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 3 - CDI</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #28a745; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #28a745; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #28a745; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; margin: 2px; display: inline-block; }
        a { color: #28a745; }
        .resultado { background: #d4edda; border: 1px solid #c3e6cb; padding: 10px; margin: 10px 0; border-radius: 3px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #28a745; color: white; }
        .error { background: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 3px; color: #721c24; }
    </style>
</head>
<body>
    <h1>Ejercicio 3: CDI (Contexts and Dependency Injection)</h1>
    <p>Este ejercicio demuestra la inyección de dependencias usando Jakarta CDI.</p>
    
    <c:set var="nombre" value="${param.nombre}" />
    <c:set var="num1" value="${param.a != '' ? param.a : 10}" />
    <c:set var="num2" value="${param.b != '' ? param.b : 5}" />
    <c:set var="operacion" value="${param.op}" />
    
    <section>
        <h2>1. Inyección de Servicio con @ApplicationScoped</h2>
        <div class="ejemplo">
            <p>El servicio <code>SaludoService</code> está registrado como <code>@Named("saludoService")</code></p>
            <p>Se inyecta automáticamente y está disponible en EL como <code>#{saludoService}</code></p>
        </div>
        
        <form method="get">
            <label>Nombre: <input type="text" name="nombre" value="${empty nombre ? 'Carlos' : nombre}" size="20"></label>
            <button type="submit" class="btn">Saludar</button>
        </form>
        
        <div class="resultado">
            <strong>Mensaje:</strong> #{saludoService.getSaludo(nombre)}
        </div>
        
        <div class="resultado">
            <strong>Formal:</strong> #{saludoService.getSaludoFormal(nombre)}
        </div>
        
        <div class="resultado">
            <strong>Hora:</strong> #{saludoService.getHoraActual()}
        </div>
    </section>
    
    <section>
        <h2>2. CDI con CalculadoraService</h2>
        <div class="ejemplo">
            <p>Uso de <code>ICalculadoraService</code> inyectado mediante CDI</p>
        </div>
        
        <form method="get">
            <label>Num1: <input type="number" name="a" value="${num1}" size="8"></label>
            <label>Num2: <input type="number" name="b" value="${num2}" size="8"></label>
            <select name="op">
                <option value="suma" ${operacion == 'suma' ? 'selected' : ''}>Suma</option>
                <option value="resta" ${operacion == 'resta' ? 'selected' : ''}>Resta</option>
                <option value="mult" ${operacion == 'mult' ? 'selected' : ''}>Multiplicación</option>
                <option value="div" ${operacion == 'div' ? 'selected' : ''}>División</option>
                <option value="pot" ${operacion == 'pot' ? 'selected' : ''}>Potencia</option>
                <option value="fact" ${operacion == 'fact' ? 'selected' : ''}>Factorial</option>
            </select>
            <button type="submit" class="btn">Calcular</button>
        </form>
        
        <c:if test="${not empty operacion}">
            <div class="resultado">
                <table>
                    <tr><th>Operación</th><th>Expression EL</th><th>Resultado</th></tr>
                    <tr><td>Suma</td><td>${calculadoraService.suma(num1, num2)}</td><td>${calculadoraService.suma(num1, num2)}</td></tr>
                    <tr><td>Resta</td><td>${calculadoraService.resta(num1, num2)}</td><td>${calculadoraService.resta(num1, num2)}</td></tr>
                    <tr><td>Multiplicación</td><td>${calculadoraService.multiplicacion(num1, num2)}</td><td>${calculadoraService.multiplicacion(num1, num2)}</td></tr>
                    <c:if test="${num2 != 0}">
                        <tr><td>División</td><td>${calculadoraService.division(num1, num2)}</td><td><fmt:formatNumber value="${calculadoraService.division(num1, num2)}" pattern="#,##0.00" /></td></tr>
                    </c:if>
                    <tr><td>Potencia (${num1}^${num2})</td><td>${calculadoraService.potencia(num1, num2)}</td><td><fmt:formatNumber value="${calculadoraService.potencia(num1, num2)}" pattern="#,##0.00" /></td></tr>
                    <tr><td>Factorial (${num1})</td><td>${calculadoraService.factorial(num1)}</td><td>${calculadoraService.factorial(num1)}</td></tr>
                </table>
            </div>
        </c:if>
    </section>
    
    <section>
        <h2>3. Scopes CDI</h2>
        <table>
            <tr>
                <th>Scope Anotación</th>
                <th>Descripción</th>
                <th>Disponibilidad</th>
            </tr>
            <tr>
                <td><code>@ApplicationScoped</code></td>
                <td>Una instancia para toda la aplicación</td>
                <td>EL y @Inject</td>
            </tr>
            <tr>
                <td><code>@RequestScoped</code></td>
                <td>Una instancia por solicitud HTTP</td>
                <td>Solo @Inject</td>
            </tr>
            <tr>
                <td><code>@SessionScoped</code></td>
                <td>Una instancia por sesión HTTP</td>
                <td>Solo @Inject (con Serializable)</td>
            </tr>
            <tr>
                <td><code>@Dependent</code></td>
                <td>Misma vida útil que el bean que lo inyecta</td>
                <td>EL y @Inject</td>
            </tr>
        </table>
    </section>
    
    <section>
        <h2>4. Anotaciones CDI Comunes</h2>
        <pre>
@ApplicationScoped    // Scope de aplicación
@Named("miBean")    // Nombre para acceso via EL
@Inject           // Inyectar dependencia
@Produces         // Método que produce objetos
@Alternative      // Implementación alternativa
@Qualifier       // Calificador personalizado
@Interceptor     // Interceptar llamadas
        </pre>
    </section>
    
    <c:set var="info" value="#{saludoService.getInformacion()}" />
    <c:if test="${not empty info}">
        <section>
            <h2>5. Información del BeanInyectado</h2>
            <div class="resultado">
                <p><strong>Bean info:</strong> ${info}</p>
            </div>
        </section>
    </c:if>
    
    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a>
    </p>
</body>
</html>