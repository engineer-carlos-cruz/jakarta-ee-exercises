<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 10 - Jakarta Mail</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        form { background: #f5f5f5; padding: 20px; border-radius: 5px; }
        input, textarea, select { padding: 8px; margin: 5px 0; width: 100%; box-sizing: border-box; }
        textarea { height: 100px; }
        button { background: #28a745; color: white; padding: 10px 20px; border: none; cursor: pointer; }
        .resultado { background: #d4edda; border: 1px solid #c3e6cb; padding: 15px; margin: 20px 0; border-radius: 5px; }
        .error { background: #f8d7da; border: 1px solid #f5c6cb; padding: 15px; margin: 20px 0; border-radius: 5px; }
        .info { background: #e7f3ff; padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 14px; }
        a { color: #007bff; }
    </style>
</head>
<body>
    <h1>Ejercicio 10: Jakarta Mail - Envío de Correos</h1>
    
    <% String resultado = (String) request.getAttribute("resultado"); %>
    <% if (resultado != null) { %>
        <div class="resultado">
            <h3>Resultado:</h3>
            <p><%= resultado %></p>
            <p><strong>Para:</strong> ${para}</p>
            <p><strong>Asunto:</strong> ${asunto}</p>
        </div>
    <% } %>
    
    <form method="post" action="${pageContext.request.contextPath}/enviarEmail">
        <h3>Enviar Email</h3>
        
        <label>Para (email):</label>
        <input type="email" name="para" required placeholder="destinatario@ejemplo.com">
        
        <label>Asunto:</label>
        <input type="text" name="asunto" required placeholder="Asunto del mensaje">
        
        <label>Mensaje:</label>
        <textarea name="mensaje" required placeholder="Escribe tu mensaje aquí..."></textarea>
        
        <label>Tipo de mensaje:</label>
        <select name="tipo">
            <option value="texto">Texto plano</option>
            <option value="html">HTML</option>
        </select>
        
        <button type="submit">Enviar Email</button>
    </form>
    
    <div class="info">
        <h3>Información:</h3>
        <p>Este ejercicio demuestra el uso de <strong>Jakarta Mail</strong> para enviar correos electrónicos.</p>
        <ul>
            <li>Configuración SMTP: smtp.gmail.com:587</li>
            <li>Soporte para emails de texto y HTML</li>
            <li>Autenticación TLS habilitada</li>
        </ul>
        <p><strong>Nota:</strong> Para productivo, configura credenciales válidas en EmailService.java</p>
    </div>
    
    <hr>
    <p><a href="${pageContext.servletContext.contextPath}/index.jsp">Volver al inicio</a></p>
</body>
</html>