package com.ejercicios.service;

import com.ejercicios.entity.Usuario;
import com.ejercicios.entity.Rol;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioService {

    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioService() {
        usuarios.add(new Usuario("admin", "1234", Rol.ADMIN));
        usuarios.add(new Usuario("user", "pass", Rol.USER));
    }

    public Usuario buscarPorUsername(String username) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
    }
}