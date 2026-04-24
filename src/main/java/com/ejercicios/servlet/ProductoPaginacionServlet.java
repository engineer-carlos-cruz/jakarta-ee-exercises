package com.ejercicios.servlet;

import com.ejercicios.entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductoPaginacionServlet", urlPatterns = {"/productos/pagina"})
public class ProductoPaginacionServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = (EntityManagerFactory) getServletContext().getAttribute("entityManagerFactory");
        if (emf == null) {
            emf = jakarta.persistence.Persistence.createEntityManagerFactory("miPU");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int page = 0;
        int size = 10;

        try {
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");

            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            if (sizeParam != null && !sizeParam.isEmpty()) {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Parámetros de paginación inválidos\"}");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Producto> query = em.createQuery(
                    "SELECT p FROM Producto p ORDER BY p.id", Producto.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            TypedQuery<Long> countQuery = em.createQuery(
                    "SELECT COUNT(p) FROM Producto p", Long.class);
            Long totalElements = countQuery.getSingleResult();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            List<Producto> productos = query.getResultList();

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"content\":[");
            
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                if (i > 0) json.append(",");
                json.append("{");
                json.append("\"id\":").append(p.getId()).append(",");
                json.append("\"nombre\":\"").append(p.getNombre()).append("\",");
                json.append("\"precio\":").append(p.getPrecio());
                json.append("}");
            }
            
            json.append("],");
            json.append("\"page\":").append(page).append(",");
            json.append("\"size\":").append(size).append(",");
            json.append("\"totalElements\":").append(totalElements).append(",");
            json.append("\"totalPages\":").append(totalPages);
            json.append("}");

            try (PrintWriter out = response.getWriter()) {
                out.print(json.toString());
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && !emf.isOpen()) {
            emf.close();
        }
    }
}