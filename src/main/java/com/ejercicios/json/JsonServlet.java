package com.ejercicios.json;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/procesarJson")
public class JsonServlet extends HttpServlet {

    private final ProductoJsonService jsonService = new ProductoJsonService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        String resultado = "";

        switch (accion) {
            case "crear":
                String nombre = request.getParameter("nombre");
                double precio = Double.parseDouble(request.getParameter("precio"));
                String categoria = request.getParameter("categoria");
                boolean disponible = "on".equals(request.getParameter("disponible"));
                
                resultado = jsonService.crearJsonProducto(nombre, precio, categoria, disponible);
                request.setAttribute("resultado", resultado);
                break;
                
            case "crearLista":
                List<Map<String, Object>> productos = new ArrayList<>();
                
                Map<String, Object> p1 = new HashMap<>();
                p1.put("id", 1);
                p1.put("nombre", "Laptop");
                p1.put("precio", 999.99);
                p1.put("categoria", "Electrónica");
                p1.put("disponible", true);
                
                Map<String, Object> p2 = new HashMap<>();
                p2.put("id", 2);
                p2.put("nombre", "Mouse");
                p2.put("precio", 29.99);
                p2.put("categoria", "Accesorios");
                p2.put("disponible", true);
                
                Map<String, Object> p3 = new HashMap<>();
                p3.put("id", 3);
                p3.put("nombre", "Teclado");
                p3.put("precio", 79.99);
                p3.put("categoria", "Accesorios");
                p3.put("disponible", false);
                
                productos.add(p1);
                productos.add(p2);
                productos.add(p3);
                
                resultado = jsonService.crearJsonListaProductos(productos);
                request.setAttribute("resultado", resultado);
                break;
                
            case "crearAnidado":
                resultado = jsonService.crearJsonAnidado();
                request.setAttribute("resultado", resultado);
                break;
                
            case "parsear":
                String jsonInput = request.getParameter("jsonInput");
                try {
                    Map<String, Object> producto = jsonService.parsearJsonProducto(jsonInput);
                    resultado = "Parsed: " + producto.toString();
                } catch (Exception e) {
                    resultado = "Error al parsear: " + e.getMessage();
                }
                request.setAttribute("resultado", resultado);
                break;
        }

        request.getRequestDispatcher("/ejercicio11.jsp").forward(request, response);
    }
}