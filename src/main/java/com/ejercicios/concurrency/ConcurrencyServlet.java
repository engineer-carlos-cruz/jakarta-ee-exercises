package com.ejercicios.concurrency;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.concurrent.ManagedThreadFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/concurrencia")
public class ConcurrencyServlet extends HttpServlet {

    private final AtomicInteger contadorVisitas = new AtomicInteger(0);
    private final AtomicLong ultimaEjecucion = new AtomicLong(0);
    private final AtomicInteger tareasProgramadas = new AtomicInteger(0);
    private ScheduledExecutorService scheduler;

    @Override
    public void init() throws ServletException {
        super.init();
        scheduler = Executors.newScheduledThreadPool(2);
        
        scheduler.scheduleAtFixedRate(() -> {
            tareasProgramadas.incrementAndGet();
            ultimaEjecucion.set(System.currentTimeMillis());
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        contadorVisitas.incrementAndGet();
        
        String accion = request.getParameter("accion");
        StringBuilder resultado = new StringBuilder();
        
        resultado.append("<h2>Ejecución: ").append(contadorVisitas.get()).append("</h2>");
        resultado.append("<p><strong>Thread actual:</strong> ").append(Thread.currentThread().getName()).append("</p>");
        resultado.append("<p><strong>Hora del servidor:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>");
        
        switch (accion != null ? accion : "") {
            case "tarea":
                String taskResult = ejecutarTareaAsincrona();
                resultado.append("<div class='resultado'>").append(taskResult).append("</div>");
                break;
                
            case "estado":
                resultado.append("<h3>Estado del Servidor:</h3>");
                resultado.append("<ul>");
                resultado.append("<li>Contador de visitas: ").append(contadorVisitas.get()).append("</li>");
                resultado.append("<li>Tareas programadas ejecutadas: ").append(tareasProgramadas.get()).append("</li>");
                resultado.append("<li>Última ejecución de tarea: ").append(
                    ultimaEjecucion.get() > 0 ? 
                    LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(ultimaEjecucion.get()), java.time.ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "N/A").append("</li>");
                resultado.append("<li>Threads activos: ").append(Thread.activeCount()).append("</li>");
                resultado.append("</ul>");
                break;
                
            default:
                resultado.append("<p>Selecciona una acción para ejecutar:</p>");
                resultado.append("<ul>");
                resultado.append("<li><a href='?accion=tarea'>Ejecutar tarea asíncrona</a></li>");
                resultado.append("<li><a href='?accion=estado'>Ver estado del servidor</a></li>");
                resultado.append("</ul>");
        }

        request.setAttribute("resultado", resultado.toString());
        request.getRequestDispatcher("/ejercicio12.jsp").forward(request, response);
    }

    private String ejecutarTareaAsincrona() {
        String threadName = Thread.currentThread().getName();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Tarea Asíncrona Ejecutada</h3>");
        sb.append("<p>Ejecutada en thread: <strong>").append(threadName).append("</strong></p>");
        sb.append("<p>Timestamp: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))).append("</p>");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        sb.append("<p>Operación completada exitosamente</p>");
        
        return sb.toString();
    }
}