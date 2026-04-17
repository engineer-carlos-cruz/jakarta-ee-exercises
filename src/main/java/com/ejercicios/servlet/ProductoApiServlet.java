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
import java.util.List;

@WebServlet(name = "ProductoApiServlet", urlPatterns = {"/api/productos"})
public class ProductoApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rol = (String) request.getAttribute("rol");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("entityManagerFactory");
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p", Producto.class);
            List<Producto> productos = query.getResultList();

            out.print("[");
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                out.print("{\"id\": " + p.getId() + ", \"nombre\": \"" + p.getNombre() + "\", \"precio\": " + p.getPrecio() + "}");
                if (i < productos.size() - 1) out.print(", ");
            }
            out.print("]");
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rol = (String) request.getAttribute("rol");

        if (!"ADMIN".equals(rol)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Solo admins pueden crear productos\"}");
            return;
        }

        String nombre = request.getParameter("nombre");
        String precioStr = request.getParameter("precio");

        if (nombre == null || precioStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Nombre y precio requeridos\"}");
            return;
        }

        Double precio = Double.parseDouble(precioStr);

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("entityManagerFactory");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Producto producto = new Producto(nombre, precio);
            em.persist(producto);
            em.getTransaction().commit();

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\": \"Producto creado\", \"id\": " + producto.getId() + "}");
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al crear producto\"}");
        } finally {
            em.close();
        }
    }
}