package com.ejercicios.servlet;

import com.ejercicios.entity.Producto;
import com.ejercicios.entity.Rol;
import com.ejercicios.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class InicializadorDatos implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jakarta-ee-exercises");
        sce.getServletContext().setAttribute("entityManagerFactory", emf);

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<Long> countUsers = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class);
            Long count = countUsers.getSingleResult();

            if (count == 0) {
                Usuario admin = new Usuario("admin", "password123", Rol.ADMIN);
                Usuario user = new Usuario("user", "password123", Rol.USER);
                em.persist(admin);
                em.persist(user);

                em.persist(new Producto("Laptop", 999.99));
                em.persist(new Producto("Mouse", 29.99));
                em.persist(new Producto("Teclado", 79.99));

                em.getTransaction().commit();
                System.out.println("=== Datos de prueba inicializados ===");
                System.out.println("Usuario admin: admin / password123 (ADMIN)");
                System.out.println("Usuario user: user / password123 (USER)");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EntityManagerFactory emf = (EntityManagerFactory) sce.getServletContext().getAttribute("entityManagerFactory");
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}