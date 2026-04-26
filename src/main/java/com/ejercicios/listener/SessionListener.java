package com.ejercicios.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        
        Integer contador = (Integer) context.getAttribute("contadorSesiones");
        int total = (contador == null ? 0 : contador) + 1;
        context.setAttribute("contadorSesiones", total);
        
        session.setAttribute("fechaCreacion", System.currentTimeMillis());
        session.setAttribute("idSesion", session.getId());
        
        System.out.println("[SessionListener] Sesión creada: " + session.getId());
        System.out.println("[SessionListener] Sesiones activas: " + total);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        
        Integer contador = (Integer) context.getAttribute("contadorSesiones");
        int total = (contador == null ? 0 : contador) - 1;
        if (total < 0) total = 0;
        context.setAttribute("contadorSesiones", total);
        
        System.out.println("[SessionListener] Sesión destruida: " + session.getId());
        System.out.println("[SessionListener] Sesiones activas: " + total);
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("[SessionListener] Atributo añadido a sesión: " + event.getName());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("[SessionListener] Atributo eliminado de sesión: " + event.getName());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("[SessionListener] Atributo modificado en sesión: " + event.getName());
    }
}