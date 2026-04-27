package com.ejercicios.servlet;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.*;

@WebServlet(name = "AsyncServlet", urlPatterns = {"/async"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        
        if ("progress".equals(action)) {
            handleProgress(request, response);
            return;
        }
        
        if ("start".equals(action)) {
            handleAsyncTask(request, response);
            return;
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Async Servlet</title></head><body>");
        out.println("<h2>Async Servlet - Procesamiento Asíncrono</h2>");
        out.println("<form action='async' method='get'>");
        out.println("<input type='hidden' name='action' value='start'>");
        out.println("<label>Segundos de demora: <input type='number' name='segundos' value='5' min='1' max='30'></label><br><br>");
        out.println("<button type='submit'>Iniciar Tarea Asíncrona</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    private void handleProgress(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String taskId = request.getParameter("taskId");
        Integer progress = (Integer) getServletContext().getAttribute("task_" + taskId);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (progress == null) {
            out.print("{\"progress\": 100, \"status\": \"completed\"}");
        } else {
            out.print("{\"progress\":" + progress + ", \"status\": \"running\"}");
        }
    }

    private void handleAsyncTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String segundosStr = request.getParameter("segundos");
        int segundos = 5;
        try {
            segundos = Integer.parseInt(segundosStr);
            if (segundos < 1) segundos = 1;
            if (segundos > 30) segundos = 30;
        } catch (NumberFormatException e) {
            segundos = 5;
        }
        
        String taskId = String.valueOf(System.currentTimeMillis());
        final int totalSegundos = segundos;
        
        getServletContext().setAttribute("task_" + taskId, 0);
        getServletContext().setAttribute("task_status_" + taskId, "running");
        
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(60000);
        
        asyncContext.addListener(new jakarta.servlet.AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("[AsyncServlet] Tarea completada: " + taskId);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("[AsyncServlet] Timeout: " + taskId);
                getServletContext().removeAttribute("task_" + taskId);
                getServletContext().removeAttribute("task_status_" + taskId);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                System.out.println("[AsyncServlet] Error: " + taskId);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("[AsyncServlet] Inicio async: " + taskId);
            }
        });
        
        executor.submit(() -> {
            try {
                for (int i = 0; i <= totalSegundos; i++) {
                    if (!"running".equals(getServletContext().getAttribute("task_status_" + taskId))) {
                        break;
                    }
                    
                    getServletContext().setAttribute("task_" + taskId, i * 100 / totalSegundos);
                    
                    if (i < totalSegundos) {
                        Thread.sleep(1000);
                    }
                }
                
                getServletContext().setAttribute("task_status_" + taskId, "completed");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<title>Tarea Asíncrona</title>");
        out.println("<meta http-equiv='refresh' content='1'>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }");
        out.println(".progress-container { background: #e0e0e0; width: 100%; height: 30px; border-radius: 5px; }");
        out.println(".progress-bar { background: #28a745; height: 100%; border-radius: 5px; transition: width 0.3s; }");
        out.println(".status { text-align: center; margin: 20px 0; }");
        out.println("a { color: #28a745; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h2>Procesando tarea...</h2>");
        out.println("<div class='progress-container'>");
        
        int currentProgress = (Integer) getServletContext().getAttribute("task_" + taskId);
        out.println("<div class='progress-bar' style='width: " + currentProgress + "%'></div>");
        out.println("</div>");
        out.println("<div class='status'>" + currentProgress + "% completado</div>");
        
        if (currentProgress < 100) {
            out.println("<form action='async' method='get'>");
            out.println("<input type='hidden' name='action' value='start'>");
            out.println("<button type='submit'>Nueva tarea</button>");
            out.println("</form>");
        } else {
            out.println("<p><strong>¡Tarea completada!</strong></p>");
            out.println("<p><a href='async'>Nueva tarea</a></p>");
            getServletContext().removeAttribute("task_" + taskId);
            getServletContext().removeAttribute("task_status_" + taskId);
        }
        
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }
}