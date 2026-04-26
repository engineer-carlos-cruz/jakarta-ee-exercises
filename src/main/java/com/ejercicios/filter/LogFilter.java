package com.ejercicios.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[LogFilter] Inicializado");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        long tiempoActual = System.currentTimeMillis();
        
        System.out.println("========================================");
        System.out.println("[LogFilter] " + new Date());
        System.out.println("[LogFilter] Método: " + httpRequest.getMethod());
        System.out.println("[LogFilter] URL: " + httpRequest.getRequestURL());
        System.out.println("[LogFilter] URI: " + httpRequest.getRequestURI());
        System.out.println("[LogFilter] QueryString: " + httpRequest.getQueryString());
        System.out.println("[LogFilter] RemoteAddr: " + httpRequest.getRemoteAddr());
        System.out.println("[LogFilter] Session: " + httpRequest.getSession(false));
        
        System.out.println("[LogFilter] Headers:");
        httpRequest.getHeaderNames().asIterator().forEachRemaining(name -> 
            System.out.println("  " + name + ": " + httpRequest.getHeader(name))
        );
        
        request.setAttribute("tiempoInicio", tiempoActual);
        
        chain.doFilter(request, response);
        
        System.out.println("[LogFilter] Status: " + httpResponse.getStatus());
        System.out.println("[LogFilter] ContentType: " + httpResponse.getContentType());
        System.out.println("========================================");
    }

    @Override
    public void destroy() {
        System.out.println("[LogFilter] Destruido");
    }
}