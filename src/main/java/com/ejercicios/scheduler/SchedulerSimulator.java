package com.ejercicios.scheduler;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.*;

@WebListener
public class SchedulerSimulator implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private ServletContext context;
    private int visitasTotales = 0;
    private int contadorHeartbeat = 0;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        context.setAttribute("scheduler_visitas", visitasTotales);
        context.setAttribute("scheduler_heartbeat", contadorHeartbeat);
        context.setAttribute("scheduler_inicio", System.currentTimeMillis());
        
        scheduler = Executors.newScheduledThreadPool(1);
        
        // Tarea cada 5 segundos
        scheduler.scheduleAtFixedRate(() -> {
            contadorHeartbeat++;
            context.setAttribute("scheduler_heartbeat", contadorHeartbeat);
            long uptime = (System.currentTimeMillis() - (long) context.getAttribute("scheduler_inicio")) / 1000;
            System.out.println("[Scheduler] Latido #" + contadorHeartbeat + " | Uptime: " + uptime + "s | Visitas: " + visitasTotales);
        }, 5, 5, TimeUnit.SECONDS);
        
        System.out.println("[Scheduler] Inicializado - Timer cada 5 segundos");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
            System.out.println("[Scheduler] Detenido - Heartbeats totales: " + contadorHeartbeat);
        }
    }

    public static void registrarVisita(ServletContext context) {
        int visitas = (int) context.getAttribute("scheduler_visitas");
        context.setAttribute("scheduler_visitas", visitas + 1);
    }
    
    public static int getVisitas(ServletContext context) {
        Integer visitas = (Integer) context.getAttribute("scheduler_visitas");
        return visitas != null ? visitas : 0;
    }
    
    public static int getHeartbeats(ServletContext context) {
        Integer heartbeats = (Integer) context.getAttribute("scheduler_heartbeat");
        return heartbeats != null ? heartbeats : 0;
    }
}