package com.ejercicios.servlet;

import com.ejercicios.entity.Usuario;
import com.ejercicios.security.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Usuario y contraseña requeridos\"}");
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("entityManagerFactory");
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class);
            query.setParameter("username", username);
            Usuario usuario = query.getSingleResult();

            if (usuario != null && usuario.getPassword().equals(password)) {
                String token = JwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());
                
                Map<String, String> result = new HashMap<>();
                result.put("token", token);
                result.put("username", usuario.getUsername());
                result.put("rol", usuario.getRol().name());

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"token\": \"" + token + "\", \"username\": \"" + 
                    usuario.getUsername() + "\", \"rol\": \"" + usuario.getRol().name() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Credenciales inválidas\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Credenciales inválidas\"}");
        } finally {
            em.close();
        }
    }
}