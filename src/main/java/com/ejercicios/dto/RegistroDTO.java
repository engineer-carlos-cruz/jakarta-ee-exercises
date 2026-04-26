package com.ejercicios.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class RegistroDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obrigatório")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obrigatória")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String password;

    @NotBlank(message = "Confirmar contraseña es obrigatório")
    private String confirmarPassword;

    @NotNull(message = "La edad esatória")
    @Min(value = 18, message = "Debes tener al menos 18 años")
    @Max(value = 120, message = "La edad no puede ser mayor a 120")
    private Integer edad;

    @Pattern(regexp = "^\\d{5}$", message = "El código postal debe tener 5 dígitos")
    private String codigoPostal;

    @NotBlank(message = "Debes aceptar los términos")
    private String terminos;

    public RegistroDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmarPassword() { return confirmarPassword; }
    public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getTerminos() { return terminos; }
    public void setTerminos(String terminos) { this.terminos = terminos; }
}