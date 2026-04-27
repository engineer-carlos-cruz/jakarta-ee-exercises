package com.ejercicios.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private Set<String> allowedExtensions;

    @Override
    public void init() throws ServletException {
        allowedExtensions = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "pdf", "txt", "doc", "docx", "xls", "xlsx", "zip"
        ));
        
        Path uploadPath = Paths.get(getServletContext().getRealPath(""), UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new ServletException("No se pudo crear directorio de upload", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String descripcion = request.getParameter("descripcion");
        Part filePart = request.getPart("archivo");
        
        if (filePart == null || filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty()) {
            out.println("<p style='color:red;'>Error: No se seleccionó ningún archivo.</p>");
            out.println("<p><a href='ejercicio7.jsp'>Volver</a></p>");
            return;
        }
        
        String fileName = getSubmittedFileName(filePart);
        String extension = getFileExtension(fileName).toLowerCase();
        
        if (!allowedExtensions.contains(extension)) {
            out.println("<p style='color:red;'>Error: Extensión '" + extension + "' no permitida.</p>");
            out.println("<p>Extensiones permitidas: " + allowedExtensions + "</p>");
            out.println("<p><a href='ejercicio7.jsp'>Volver</a></p>");
            return;
        }
        
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Path filePath = Paths.get(getServletContext().getRealPath(""), UPLOAD_DIR, uniqueFileName);
        
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Guardar metadata en sesión
        HttpSession session = request.getSession();
        List<Map<String, String>> archivos = (List<Map<String, String>>) session.getAttribute("archivosSubidos");
        if (archivos == null) {
            archivos = new ArrayList<>();
            session.setAttribute("archivosSubidos", archivos);
        }
        
        Map<String, String> archivo = new HashMap<>();
        archivo.put("nombre", fileName);
        archivo.put("guardado", uniqueFileName);
        archivo.put("tamano", formatFileSize(filePart.getSize()));
        archivo.put("tipo", filePart.getContentType());
        archivo.put("descripcion", descripcion != null ? descripcion : "");
        archivo.put("fecha", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        archivos.add(0, archivo);
        
        response.sendRedirect(request.getContextPath() + "/ejercicio7.jsp?uploaded=true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/ejercicio7.jsp");
    }

    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }
}