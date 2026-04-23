package com.ejercicios.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet(name = "ProductoApiClientServlet", urlPatterns = {"/productos/api"})
public class ProductoApiClientServlet extends HttpServlet {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiUrl = "http://localhost:8080/jakarta-ee-exercises/api/productos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String jsonResponse = "";
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            jsonResponse = res.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            jsonResponse = "Error: " + e.getMessage();
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>API REST Productos</title></head><body>");
            out.println("<h1>API REST - ProductoResource (JAX-RS)</h1>");
            out.println("<p>Endpoint: " + apiUrl + "</p>");
            out.println("<h2>Métodos disponibles:</h2>");
            out.println("<ul>");
            out.println("<li>GET /api/productos - Listar todos</li>");
            out.println("<li>GET /api/productos/{id} - Buscar por ID</li>");
            out.println("<li>POST /api/productos - Crear</li>");
            out.println("<li>PUT /api/productos/{id} - Actualizar</li>");
            out.println("<li>DELETE /api/productos/{id} - Eliminar</li>");
            out.println("</ul>");
            out.println("<h2>Respuesta GET:</h2>");
            out.println("<pre>" + jsonResponse + "</pre>");
            out.println("<br><a href='" + request.getContextPath() + "/productos/api'>Actualizar</a>");
            out.println("</body></html>");
        }
    }
}