<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 1 - Introducción a JSP</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        form { background: #f5f5f5; padding: 20px; border-radius: 5px; }
        input { padding: 8px; margin: 5px 0; width: 100%; box-sizing: border-box; }
        button { background: #007bff; color: white; padding: 10px 20px; border: none; cursor: pointer; }
        .info { background: #e7f3ff; padding: 15px; margin: 20px 0; border-radius: 5px; }
        .contador { color: #666; font-size: 14px; }
    </style>
</head>
<body>
    <h1>Ejercicio 1: Introducción a JSP</h1>
    
    <%
    String nombre = request.getParameter("nombre");
    String email = request.getParameter("email");
    Boolean submetido = request.getParameter("nombre") != null;
    
    if (submetido && nombre != null && !nombre.isEmpty()) {
        session.setAttribute("nombreUsuario", nombre);
        session.setAttribute("emailUsuario", email);
        Integer visitas = (Integer) session.getAttribute("visitas");
        session.setAttribute("visitas", (visitas == null ? 1 : visitas + 1));
    }
    %>
    
    <% if (submetido && nombre != null && !nombre.isEmpty()) { %>
        <div class="info">
            <h2>Bienvenido, ${nombreUsuario}!</h2>
            <p>Email: ${emailUsuario}</p>
            <p class="contador">Visitas a esta página: ${visitas}</p>
            <p><a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a></p>
        </div>
    <% } else { %>
        <form method="post" action="${pageContext.request.contextPath}/index.jsp">
            <h3>Ingresa tus datos</h3>
            <label>Nombre:</label>
            <input type="text" name="nombre" required>
            
            <label>Email:</label>
            <input type="email" name="email" required>
            
            <button type="submit">Enviar</button>
        </form>
        
        <div class="info">
            <h3>Objetos Implícitos Disponibles:</h3>
            <ul>
                <li><strong>pageContext</strong>: ${pageContext}</li>
                <li><strong>request</strong>: ${pageContext.request}</li>
                <li><strong>response</strong>: ${pageContext.response}</li>
                <li><strong>session</strong>: ${pageContext.session}</li>
                <li><strong>servletContext</strong>: ${pageContext.servletContext}</li>
            </ul>
            <h3>Expression Language (EL):</h3>
            <ul>
                <li><strong>param.nombre</strong>: "${param.nombre}"</li>
                <li><strong>param.email</strong>: "${param.email}"</li>
                <li><strong>sessionScope.nombreUsuario</strong>: "${sessionScope.nombreUsuario}"</li>
                <li><strong>header.host</strong>: "${header.host}"</li>
            </ul>
        </div>
    <% } %>
    
    <hr>
    <p><small>Contexto: ${pageContext.servletContext.contextPath}</small></p>
</body>
</html>