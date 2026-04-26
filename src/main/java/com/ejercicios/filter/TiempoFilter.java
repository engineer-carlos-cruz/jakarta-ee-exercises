package com.ejercicios.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

public class TiempoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[TiempoFilter] Inicializado");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        long tiempoInicio = System.currentTimeMillis();
        request.setAttribute("tiempoInicio", tiempoInicio);
        
        chain.doFilter(request, response);
        
        long tiempoFin = System.currentTimeMillis();
        long duracion = tiempoFin - tiempoInicio;
        
        String uri = httpRequest.getRequestURI();
        String metodo = httpRequest.getMethod();
        
        StringBuilder info = new StringBuilder();
        info.append("<div style='background:#f0f0f0;padding:10px;margin:10px 0;border-left:4px solid #17a2b8;'>");
        info.append("<strong>Tiempo de procesamiento:</strong> ").append(duracion).append(" ms<br>");
        info.append("<strong>URL:</strong> ").append(uri).append("<br>");
        info.append("<strong>Método:</strong> ").append(metodo);
        info.append("</div>");
        
        PrintWriter out = response.getWriter();
        String content = response.getContentType();
        
        if (content != null && content.contains("text/html")) {
            response.setContentType("text/html;charset=UTF-8");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[TiempoFilter] Destruido");
    }
}