package com.ejercicios.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
@Named("saludoService")
public class SaludoService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public String getSaludo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Hola amigo!";
        }
        return "Hola, " + nombre + "!";
    }
    
    public String getSaludoFormal(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Buen día, señor/señora.";
        }
        return "Buen día, " + nombre + ".";
    }
    
    public String getHoraActual() {
        return "Hora actual: " + LocalDateTime.now().format(formatter);
    }
    
    public String getInformacion() {
        return "CDI - SaludoService (ApplicationScoped)";
    }
}