<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 7 - File Upload/Download</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #fd7e14; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #fd7e14; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #fd7e14; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; border: none; cursor: pointer; }
        a { color: #fd7e14; }
        .info { background: #d1ecf1; border: 1px solid #bee5eb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .success { background: #d4edda; border: 1px solid #c3e6cb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .error { background: #f8d7da; border: 1px solid #f5c6cb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #fd7e14; color: white; }
        form { background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="file"], input[type="text"], textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 3px; box-sizing: border-box; }
        .nota { color: #666; font-size: 12px; }
    </style>
</head>
<body>
    <h1>Ejercicio 7: File Upload/Download</h1>
    <p>Este ejercicio demuestra cómo subir y descargar archivos usando Jakarta Servlet.</p>

    <c:if test="${param.uploaded == 'true'}">
        <div class="success">
            <strong>¡Archivo subido exitosamente!</strong>
        </div>
    </c:if>

    <section>
        <h2>1. Subir Archivo</h2>
        <div class="ejemplo">
            <p>Usa <code>@MultipartConfig</code> para manejar archivos multipart y <code>Part</code> para obtener el archivo.</p>
        </div>
        
        <form method="post" action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data">
            <label>Descripción (opcional):
            <input type="text" name="descripcion" placeholder="Descripción del archivo"></label>
            
            <label>Seleccionar archivo:
            <input type="file" name="archivo" required></label>
            
            <p class="nota">Extensiones permitidas: jpg, jpeg, png, gif, pdf, txt, doc, docx, xls, xlsx, zip<br>
            Tamaño máximo: 10 MB</p>
            
            <button type="submit" class="btn">Subir Archivo</button>
        </form>
    </section>

    <section>
        <h2>2. Archivos Subidos</h2>
        <c:set var="archivos" value="${sessionScope.archivosSubidos}" />
        
        <c:choose>
            <c:when test="${empty archivos}">
                <p>No hay archivos subidos en esta sesión.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>Nombre</th>
                        <th>Tamaño</th>
                        <th>Tipo</th>
                        <th>Fecha</th>
                        <th>Descripción</th>
                        <th>Acción</th>
                    </tr>
                    <c:forEach var="archivo" items="${archivos}">
                        <tr>
                            <td>${archivo.nombre}</td>
                            <td>${archivo.tamano}</td>
                            <td>${archivo.tipo}</td>
                            <td>${archivo.fecha}</td>
                            <td>${archivo.descripcion}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/download?file=${archivo.guardado}">Descargar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </section>

    <section>
        <h2>3. Anatomía de FileUploadServlet</h2>
        <pre>
@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,     // 1 MB
    maxFileSize = 1024 * 1024 * 10,     // 10 MB
    maxRequestSize = 1024 * 1024 * 20   // 20 MB
)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) {
        
        Part filePart = request.getPart("archivo");
        String fileName = getSubmittedFileName(filePart);
        
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, targetPath);
        }
    }
}
        </pre>
    </section>

    <section>
        <h2>4. Descargar Archivo</h2>
        <pre>
@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response) {
        
        String fileName = request.getParameter("file");
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        
        response.setContentType(
            getServletContext().getMimeType(fileName));
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"" + fileName + "\"");
        
        // Escribir archivo a response.getOutputStream()
    }
}
        </pre>
    </section>

    <section>
        <h2>5. Seguridad en Upload/Download</h2>
        <table>
            <tr>
                <th>Práctica</th>
                <th>Descripción</th>
            </tr>
            <tr>
                <td>Validar extensiones</td>
                <td>Verificar que solo extensiones permitidas</td>
            </tr>
            <tr>
                <td>Path traversal prevention</td>
                <td>Usar <code>Paths.get()</code> para sanitizar rutas</td>
            </tr>
            <tr>
                <td>Límites de tamaño</td>
                <td>Configurar <code>maxFileSize</code></td>
            </tr>
            <tr>
                <td>Content-Type sniffing</td>
                <td>Validar tipo MIME real del archivo</td>
            </tr>
            <tr>
                <td>Almacenamiento aislado</td>
                <td>Guardar en directorio fuera del webroot</td>
            </tr>
        </table>
    </section>

    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio3.jsp">Ejercicio 3 - CDI</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio4.jsp">Ejercicio 4 - Bean Validation</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio5.jsp">Ejercicio 5 - Filters</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio6.jsp">Ejercicio 6 - Listeners</a>
    </p>
</body>
</html>