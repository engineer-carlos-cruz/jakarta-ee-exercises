<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 11 - Jakarta JSON</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 50px auto; padding: 20px; }
        .panel { background: #f5f5f5; padding: 20px; border-radius: 5px; margin: 20px 0; }
        input, textarea { padding: 8px; margin: 5px 0; width: 100%; box-sizing: border-box; }
        textarea { height: 150px; font-family: monospace; }
        button { background: #6f42c1; color: white; padding: 10px 20px; border: none; cursor: pointer; margin: 5px; }
        .resultado { background: #1e1e1e; color: #0f0; padding: 15px; margin: 20px 0; border-radius: 5px; font-family: monospace; white-space: pre-wrap; }
        .info { background: #e7f3ff; padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 14px; }
        a { color: #007bff; }
        .acciones { margin: 10px 0; }
    </style>
</head>
<body>
    <h1>Ejercicio 11: Jakarta JSON Processing</h1>

    <% String resultado = (String) request.getAttribute("resultado"); %>
    <% if (resultado != null) { %>
        <div class="resultado"><%= resultado %></div>
    <% } %>
    
    <div class="panel">
        <h3>1. Crear JSON de Producto</h3>
        <form method="post" action="${pageContext.request.contextPath}/procesarJson">
            <input type="hidden" name="accion" value="crear">
            <label>Nombre:</label>
            <input type="text" name="nombre" required value="Smartphone Pro">
            <label>Precio:</label>
            <input type="number" name="precio" step="0.01" required value="699.99">
            <label>Categoría:</label>
            <input type="text" name="categoria" required value="Electrónica">
            <label><input type="checkbox" name="disponible" checked> Disponible</label><br><br>
            <button type="submit">Crear JSON</button>
        </form>
    </div>
    
    <div class="panel">
        <h3>2. Crear JSON Array de Productos</h3>
        <form method="post" action="${pageContext.request.contextPath}/procesarJson">
            <input type="hidden" name="accion" value="crearLista">
            <button type="submit">Generar Lista JSON</button>
        </form>
    </div>
    
    <div class="panel">
        <h3>3. Crear JSON Anidado Complejo</h3>
        <form method="post" action="${pageContext.request.contextPath}/procesarJson">
            <input type="hidden" name="accion" value="crearAnidado">
            <button type="submit">Generar JSON Anidado</button>
        </form>
    </div>
    
    <div class="panel">
        <h3>4. Parsear JSON</h3>
        <form method="post" action="${pageContext.request.contextPath}/procesarJson">
            <input type="hidden" name="accion" value="parsear">
            <label>JSON a parsear:</label>
            <textarea name="jsonInput" placeholder='{"nombre": "Producto", "precio": 100.0, "categoria": "Test", "disponible": true}'></textarea>
            <button type="submit">Parsear</button>
        </form>
    </div>
    
    <div class="info">
        <h3>Información:</h3>
        <p>Este ejercicio demuestra el uso de <strong>Jakarta JSON Processing</strong> (JSON-P) para:</p>
        <ul>
            <li>Crear objetos y arrays JSON</li>
            <li>Parsear y leer documentos JSON</li>
            <li>Construir JSON anidado con objetos dentro de objetos</li>
            <li>Trabajar con diferentes tipos de datos JSON</li>
        </ul>
    </div>
    
    <hr>
    <p><a href="${pageContext.servletContext.contextPath}/index.jsp">Volver al inicio</a></p>
</body>
</html>