package com.ejercicios.mail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/enviarEmail")
public class EnviarEmailServlet extends HttpServlet {

    private final EmailService emailService = new EmailService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String para = request.getParameter("para");
        String asunto = request.getParameter("asunto");
        String mensaje = request.getParameter("mensaje");
        String tipo = request.getParameter("tipo");

        boolean enviado = false;
        String resultado;

        if ("html".equals(tipo)) {
            String htmlContent = "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<h2 style='color: #007bff;'>Mensaje desde Jakarta Mail</h2>" +
                    "<p>" + mensaje.replace("\n", "<br>") + "</p>" +
                    "<hr><p><small>Enviado desde la aplicación Jakarta EE</small></p>" +
                    "</body></html>";
            enviado = emailService.enviarEmailConHtml(para, asunto, htmlContent);
        } else {
            enviado = emailService.enviarEmail(para, asunto, mensaje);
        }

        resultado = enviado ? 
            "Email enviado exitosamente a " + para : 
            "Error al enviar el email";

        request.setAttribute("resultado", resultado);
        request.setAttribute("para", para);
        request.setAttribute("asunto", asunto);
        
        request.getRequestDispatcher("/ejercicio10.jsp").forward(request, response);
    }
}