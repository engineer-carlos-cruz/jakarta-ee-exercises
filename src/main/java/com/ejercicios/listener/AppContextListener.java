package com.ejercicios.listener;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        
        System.out.println("========================================");
        System.out.println("[AppContextListener] Aplicación iniciada");
        System.out.println("[AppContextListener] Nombre: " + context.getServletContextName());
        System.out.println("[AppContextListener] Path: " + context.getContextPath());
        System.out.println("[AppContextListener] Servidor: " + context.getServerInfo());
        
        context.setAttribute("inicioAplicacion", System.currentTimeMillis());
        context.setAttribute("contadorRequests", 0);
        context.setAttribute("contadorSesiones", 0);
        
        Properties config = new Properties();
        try (InputStream is = context.getResourceAsStream("/WEB-INF/config.properties")) {
            if (is != null) {
                config.load(is);
                System.out.println("[AppContextListener] Config cargada");
            }
        } catch (Exception e) {
            System.out.println("[AppContextListener] Sin archivo de config");
        }
        
        System.out.println("[AppContextListener] Recursos inicializados");
        System.out.println("========================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        
        Long inicio = (Long) context.getAttribute("inicioAplicacion");
        Long totalRequests = (Long) context.getAttribute("contadorRequests");
        
        if (inicio != null) {
            long duracion = System.currentTimeMillis() - inicio;
            System.out.println("========================================");
            System.out.println("[AppContextListener] Aplicación finalizada");
            System.out.println("[AppContextListener] Duración: " + (duracion/1000) + " segundos");
            System.out.println("[AppContextListener] Total requests: " + totalRequests);
            System.out.println("[AppContextListener] Recursos liberados");
            System.out.println("========================================");
        }
    }
}