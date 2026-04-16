package com.ejercicios.servlet;

import com.ejercicios.entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("miPU");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            
            em.persist(new Producto("Laptop", 999.99));
            em.persist(new Producto("Mouse", 25.50));
            em.persist(new Producto("Teclado", 45.00));
            
            em.getTransaction().commit();
            
            List<Producto> productos = em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
            
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Productos</title></head><body>");
                out.println("<h1>Lista de Productos</h1>");
                out.println("<table border='1'><tr><th>ID</th><th>Nombre</th><th>Precio</th></tr>");
                for (Producto p : productos) {
                    out.println("<tr><td>" + p.getId() + "</td><td>" + p.getNombre() + "</td><td>$" + p.getPrecio() + "</td></tr>");
                }
                out.println("</table>");
                out.println("</body></html>");
            }
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }
}