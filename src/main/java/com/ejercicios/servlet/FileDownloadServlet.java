package com.ejercicios.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;

@WebServlet(name = "FileDownloadServlet", urlPatterns = {"/download"})
public class FileDownloadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fileName = request.getParameter("file");
        if (fileName == null || fileName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre de archivo requerido");
            return;
        }
        
        // Evitar path traversal
        fileName = Paths.get(fileName).getFileName().toString();
        
        Path filePath = Paths.get(getServletContext().getRealPath(""), UPLOAD_DIR, fileName);
        
        if (!Files.exists(filePath) || !filePath.startsWith(Paths.get(getServletContext().getRealPath(""), UPLOAD_DIR))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
            return;
        }
        
        String mimeType = getServletContext().getMimeType(filePath.toString());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        
        response.setContentType(mimeType);
        response.setContentLengthLong(Files.size(filePath));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
        
        try (InputStream input = Files.newInputStream(filePath);
             OutputStream output = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}