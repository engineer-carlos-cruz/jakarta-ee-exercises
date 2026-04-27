<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 12 - Jakarta Concurrency</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 50px auto; padding: 20px; }
        .panel { background: #f5f5f5; padding: 20px; border-radius: 5px; margin: 20px 0; }
        button { background: #fd7e14; color: white; padding: 10px 20px; border: none; cursor: pointer; }
        .resultado { background: #fff3cd; border: 1px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 5px; }
        .info { background: #e7f3ff; padding: 15px; margin: 20px 0; border-radius: 5px; font-size: 14px; }
        a { color: #007bff; }
        ul { list-style: none; padding: 0; }
        li { padding: 5px 0; }
        li a { text-decoration: none; padding: 8px 15px; display: inline-block; background: #007bff; color: white; border-radius: 4px; }
        li a:hover { background: #0056b3; }
    </style>
</head>
<body>
    <h1>Ejercicio 12: Jakarta Concurrency</h1>

    <%= request.getAttribute("resultado") != null ? request.getAttribute("resultado") : "" %>
    
    <div class="panel">
        <h3>Acciones Disponibles:</h3>
        <ul>
            <li><a href="${pageContext.request.contextPath}/concurrencia?accion=tarea">Ejecutar tarea asíncrona</a></li>
            <li style="margin-top: 10px;"><a href="${pageContext.request.contextPath}/concurrencia?accion=estado">Ver estado del servidor</a></li>
        </ul>
    </div>
    
    <div class="info">
        <h3>Información:</h3>
        <p>Este ejercicio demuestra el uso de <strong>Jakarta Concurrency</strong>:</p>
        <ul>
            <li><strong>@Asynchronous</strong>: Ejecución de métodos de manera asíncrona</li>
            <li><strong>ManagedScheduledExecutorService</strong>: Programación de tareas periódicas</li>
            <li><strong>AtomicInteger/AtomicLong</strong>: Operaciones atómicas para contadores</li>
            <li><strong>Thread management</strong>: Gestión de threads en entorno Java EE</li>
        </ul>
        <p>La tarea programada se ejecuta cada 5 segundos automáticamente.</p>
    </div>
    
    <hr>
    <p><a href="${pageContext.servletContext.contextPath}/index.jsp">Volver al inicio</a></p>
</body>
</html>