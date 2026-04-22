package com.ejercicios.servlet;

import com.ejercicios.entity.Usuario;
import com.ejercicios.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
public class UsuarioServlet extends HttpServlet {

    @Inject
    private UsuarioService usuarioService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<Usuario> usuarios = usuarioService.listarTodos();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Usuarios CDI</title></head><body>");
            out.println("<h1>Lista de Usuarios (CDI)</h1>");
            out.println("<p>Servicio inyectado: " + usuarioService.getClass().getName() + "</p>");
            out.println("<table border='1'><tr><th>ID</th><th>Username</th><th>Password</th><th>Rol</th></tr>");
            int i = 0;
            for (Usuario u : usuarios) {
                out.println("<tr><td>" + (++i) + "</td><td>" + u.getUsername() + "</td><td>" + u.getPassword() + "</td><td>" + u.getRol() + "</td></tr>");
            }
            out.println("</table>");
            out.println("<h2>Agregar Usuario</h2>");
            out.println("<form method='post'>");
            out.println("Username: <input type='text' name='username'><br>");
            out.println("Password: <input type='text' name='password'><br>");
            out.println("Rol: <select name='rol'><option>ADMIN</option><option>USUARIO</option></select><br>");
            out.println("<input type='submit' value='Agregar'>");
            out.println("</form>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");

        if (username != null && password != null) {
            Usuario nuevo = new Usuario(username, password, com.ejercicios.entity.Rol.valueOf(rol));
            usuarioService.agregar(nuevo);
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}