package com.ejercicios.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/ws/notificaciones")
@ApplicationScoped
public class NotificacionWebSocket {

    private static final Set<Session> sesiones = Collections.synchronizedSet(new HashSet<>());
    private static final Logger LOGGER = Logger.getLogger(NotificacionWebSocket.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        sesiones.add(session);
        LOGGER.info("Sesión conectada: " + session.getId());
        
        try {
            session.getBasicRemote().sendText("{\"tipo\":\"CONECTADO\",\"mensaje\":\"Conectado al servidor de notificaciones\",\"sesion\":\"" + session.getId() + "\"}");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error al enviar mensaje de bienvenida", e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Mensaje recibido de " + session.getId() + ": " + message);
        
        try {
            session.getBasicRemote().sendText("{\"tipo\":\"ECO\",\"mensaje\":\"" + message + "\"}");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error al enviar eco", e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sesiones.remove(session);
        LOGGER.info("Sesión desconectada: " + session.getId() + " - Razón: " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.log(Level.SEVERE, "Error en sesión: " + session.getId(), throwable);
    }

    public static void broadcasting(String tipo, String mensaje) {
        String json = "{\"tipo\":\"" + tipo + "\",\"mensaje\":\"" + mensaje + "\"}";
        
        for (Session session : sesiones) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error al enviar a sesión: " + session.getId(), e);
                }
            }
        }
    }

    public static int getSesionesConectadas() {
        return sesiones.size();
    }
}