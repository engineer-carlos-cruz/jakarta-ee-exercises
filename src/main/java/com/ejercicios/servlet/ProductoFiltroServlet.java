package com.ejercicios.servlet;

import com.ejercicios.entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductoFiltroServlet", urlPatterns = {"/productos/buscar"})
public class ProductoFiltroServlet extends HttpServlet {

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

        String nombre = request.getParameter("nombre");
        String precioMinStr = request.getParameter("precioMin");
        String precioMaxStr = request.getParameter("precioMax");
        String ordenarPor = request.getParameter("ordenarPor");
        String direccion = request.getParameter("direccion");

        EntityManager em = emf.createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> root = query.from(Producto.class);

            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }

            if (precioMinStr != null && !precioMinStr.isEmpty()) {
                Double precioMin = Double.parseDouble(precioMinStr);
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), precioMin));
            }

            if (precioMaxStr != null && !precioMaxStr.isEmpty()) {
                Double precioMax = Double.parseDouble(precioMaxStr);
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), precioMax));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            if (ordenarPor != null && !ordenarPor.isEmpty()) {
                if ("DESC".equalsIgnoreCase(direccion)) {
                    query.orderBy(cb.desc(root.get(ordenarPor)));
                } else {
                    query.orderBy(cb.asc(root.get(ordenarPor)));
                }
            } else {
                query.orderBy(cb.asc(root.get("id")));
            }

            List<Producto> productos = em.createQuery(query).getResultList();

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"productos\":[");

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
            json.append("\"total\":").append(productos.size());
            json.append("}");

            try (PrintWriter out = response.getWriter()) {
                out.print(json.toString());
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Parámetros numéricos inválidos\"}");
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