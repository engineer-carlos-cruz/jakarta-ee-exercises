<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 8 - Async Servlet</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #20c997; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #20c997; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #20c997; color: white; padding: 10px 20px; text-decoration: none; border-radius: 3px; border: none; cursor: pointer; }
        a { color: #20c997; }
        .info { background: #d1ecf1; border: 1px solid #bee5eb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .progress-container { background: #e0e0e0; width: 100%; height: 30px; border-radius: 5px; }
        .progress-bar { background: #20c997; height: 100%; border-radius: 5px; transition: width 0.3s; }
        form { background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="number"] { padding: 8px; margin-top: 5px; width: 100px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #20c997; color: white; }
    </style>
</head>
<body>
    <h1>Ejercicio 8: Async Servlet</h1>
    <p>Este ejercicio demuestra el procesamiento asíncrono de requests usando <code>AsyncContext</code>.</p>

    <section>
        <h2>1. Iniciar Tarea Asíncrona</h2>
        <div class="ejemplo">
            <p>Usa <code>asyncSupported = true</code> y <code>request.startAsync()</code> para procesar en segundo plano.</p>
        </div>
        
        <form method="get" action="${pageContext.request.contextPath}/async">
            <input type="hidden" name="action" value="start">
            <label>Segundos de procesamiento:
            <input type="number" name="segundos" value="5" min="1" max="30"></label>
            <button type="submit" class="btn">Iniciar Tarea</button>
        </form>
    </section>

    <section>
        <h2>2. Anatomía de Async Servlet</h2>
        <pre>
@WebServlet(urlPatterns = {"/async"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    private ExecutorService executor = 
        Executors.newFixedThreadPool(10);

    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response) {
        
        AsyncContext asyncContext = 
            request.startAsync();
        
        asyncContext.setTimeout(60000);
        
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent e) { }
            
            @Override
            public void onTimeout(AsyncEvent e) { }
            
            @Override
            public void onError(AsyncEvent e) { }
            
            @Override
            public void onStartAsync(AsyncEvent e) { }
        });
        
        executor.submit(() -> {
            // Procesamiento largo...
            asyncContext.complete();
        });
    }
}
        </pre>
    </section>

    <section>
        <h2>3. Conceptos de Async</h2>
        <table>
            <tr>
                <th>Concepto</th>
                <th>Descripción</th>
            </tr>
            <tr>
                <td><code>startAsync()</code></td>
                <td>Inicia el contexto asíncrono para el request</td>
            </tr>
            <tr>
                <td><code>AsyncContext</code></td>
                <td>Contexto que permite respuestas delayed</td>
            </tr>
            <tr>
                <td><code>setTimeout()</code></td>
                <td>Timeout en ms antes de onTimeout</td>
            </tr>
            <tr>
                <td><code>addListener()</code></td>
                <td>Registrar listeners para eventos async</td>
            </tr>
            <tr>
                <td><code>complete()</code></td>
                <td>Finaliza el procesamiento async</td>
            </tr>
            <tr>
                <td><code>ExecutorService</code></td>
                <td>Pool de hilos para tareas background</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>4. Eventos AsyncListener</h2>
        <table>
            <tr>
                <th>Evento</th>
                <th>Cuándo ocurre</th>
            </tr>
            <tr>
                <td><code>onStartAsync</code></td>
                <td>Cuando inicia el procesamiento asíncrono</td>
            </tr>
            <tr>
                <td><code>onComplete</code></td>
                <td>Cuando el procesamiento termina</td>
            </tr>
            <tr>
                <td><code>onTimeout</code></td>
                <td>Cuando expira el timeout</td>
            </tr>
            <tr>
                <td><code>onError</code></td>
                <td>Cuando ocurre un error</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>5. Beneficios de Async</h2>
        <div class="info">
            <ul>
                <li><strong>Mejora throughput:</strong> Libera el thread servlet mientras espera I/O</li>
                <li><strong>Escalabilidad:</strong> Más requests concurrentes con menos threads</li>
                <li><strong>Long Polling:</strong> Ideal para Server-Sent Events</li>
                <li><strong>Chat/WebSocket fallback:</strong> Para comet-style apps</li>
            </ul>
        </div>
    </section>

    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio3.jsp">Ejercicio 3 - CDI</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio4.jsp">Ejercicio 4 - Bean Validation</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio5.jsp">Ejercicio 5 - Filters</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio6.jsp">Ejercicio 6 - Listeners</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio7.jsp">Ejercicio 7 - File Upload</a>
    </p>
</body>
</html>