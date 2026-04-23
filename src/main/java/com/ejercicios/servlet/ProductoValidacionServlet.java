package com.ejercicios.servlet;

import com.ejercicios.entity.Producto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "ProductoValidacionServlet", urlPatterns = {"/productos/validar"})
public class ProductoValidacionServlet extends HttpServlet {

    private Validator validator;

    @Override
    public void init() throws ServletException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Validar Producto</title></head><body>");
            out.println("<h1>Bean Validation - Producto</h1>");
            out.println("<form method='post'>");
            out.println("Nombre: <input type='text' name='nombre' placeholder='Mínimo 2 caracteres'><br><br>");
            out.println("Precio: <input type='number' step='0.01' name='precio' placeholder='Mayor a 0'><br><br>");
            out.println("<input type='submit' value='Validar'>");
            out.println("</form>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nombre = request.getParameter("nombre");
        String precioStr = request.getParameter("precio");

        Producto producto = new Producto();
        producto.setNombre(nombre);
        if (precioStr != null && !precioStr.isEmpty()) {
            producto.setPrecio(Double.parseDouble(precioStr));
        }

        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Resultado Validación</title></head><body>");

            if (violations.isEmpty()) {
                out.println("<h2 style='color:green'>✓ Producto válido</h2>");
                out.println("<p>Nombre: " + producto.getNombre() + "</p>");
                out.println("<p>Precio: $" + producto.getPrecio() + "</p>");
            } else {
                out.println("<h2 style='color:red'>✗ Errores de validación:</h2>");
                out.println("<ul style='color:red'>");
                for (ConstraintViolation<Producto> v : violations) {
                    out.println("<li>" + v.getPropertyPath() + ": " + v.getMessage() + "</li>");
                }
                out.println("</ul>");
            }

            out.println("<br><a href='" + request.getContextPath() + "/productos/validar'>Volver</a>");
            out.println("</body></html>");
        }
    }

    @Override
    public void destroy() {
    }
}