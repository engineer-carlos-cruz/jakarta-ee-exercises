<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 9 - Scheduling</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #e83e8c; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #e83e8c; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        a { color: #e83e8c; }
        .info { background: #d1ecf1; border: 1px solid #bee5eb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .stats { background: #e2e3e5; padding: 20px; border-radius: 5px; text-align: center; }
        .stats-number { font-size: 36px; color: #e83e8c; font-weight: bold; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #e83e8c; color: white; }
        .btn { background: #e83e8c; color: white; padding: 8px 15px; text-decoration: none; border-radius: 3px; border: none; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Ejercicio 9: Scheduling (Tareas Programadas)</h1>
    <p>Este ejercicio demuestra el uso de <code>@Schedule</code> para tareas automáticas.</p>

    <section>
        <h2>1. Métricas de la Aplicación</h2>
        <div class="info">
            <p><strong>Nota:</strong> Las métricas reales solo están disponibles cuando se usa un contenedor EJB full (como WildFly, GlassFish, TomEE).</p>
            <p>En Tomcat básico estas tareas no se ejecutan automáticamente.</p>
        </div>
        
        <div class="stats">
            <p>Este ejercicio demuestra la estructura de scheduling en Jakarta EE.</p>
        </div>
    </section>

    <section>
        <h2>2. SchedulerSimulator</h2>
        <pre>
@WebListener
public class SchedulerSimulator implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(() -> {
            // Tarea que se ejecuta cada 5 segundos
            System.out.println("[Scheduler] Latido #" + counter);
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdown();
    }
}
        </pre>
    </section>

    <section>
        <h2>3. Expresiones @Schedule</h2>
        <table>
            <tr>
                <th>Expresión</th>
                <th>Descripción</th>
                <th>Ejemplo</th>
            </tr>
            <tr>
                <td><code>second="*/5"</code></td>
                <td>Cada 5 segundos</td>
                <td>0, 5, 10, 15...</td>
            </tr>
            <tr>
                <td><code>minute="*/10"</code></td>
                <td>Cada 10 minutos</td>
                <td>00, 10, 20...</td>
            </tr>
            <tr>
                <td><code>hour="8"</code></td>
                <td>A las 8 AM</td>
                <td>Solo a las 8:00</td>
            </tr>
            <tr>
                <td><code>dayOfWeek="Mon-Fri"</code></td>
                <td>Días laborales</td>
                <td>Lunes a Viernes</td>
            </tr>
            <tr>
                <td><code>dayOfMonth="1"</code></td>
                <td>Primer día del mes</td>
                <td>Cada día 1</td>
            </tr>
            <tr>
                <td><code>month="*/3"</code></td>
                <td>Cada trimestre</td>
                <td>Enero, Abril, Julio, Octubre</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>4. TimerService Programático</h2>
        <pre>
@Resource
TimerService timerService;

@PostConstruct
public void init() {
    // Crear timer programático
    timerService.createTimer(
        60000,          // 60 segundos
        "Mi Timer"      // Info
    );
}

@Timeout
public void handleTimeout(Timer timer) {
    System.out.println("Timer: " + timer.getInfo());
}
        </pre>
    </section>

    <section>
        <h2>5. Timer con Datos</h2>
        <pre>
public void programarTarea(long intervaloMs, String info) {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(info);
    timerConfig.setPersistent(false);
    
    timerService.createIntervalTimer(
        0,              // Sin delay inicial
        intervaloMs,     // Intervalo
        timerConfig
    );
}

// Manejar timer
@Timeout
public void executeScheduledTask(Timer timer) {
    String taskInfo = (String) timer.getInfo();
    System.out.println("Ejecutando: " + taskInfo);
}
        </pre>
    </section>

    <section>
        <h2>6. Casos de Uso</h2>
        <table>
            <tr>
                <th>Caso</th>
                <th>Frecuencia</th>
            </tr>
            <tr>
                <td>Limpieza de cache</td>
                <td>Cada 5 minutos</td>
            </tr>
            <tr>
                <td>Reportes diarios</td>
                <td>Cada día a medianoche</td>
            </tr>
            <tr>
                <td>Backup semanal</td>
                <td>Domingo a las 2 AM</td>
            </tr>
            <tr>
                <td>Notificaciones</td>
                <td>Cada hora</td>
            </tr>
            <tr>
                <td>Health checks</td>
                <td>Cada 30 segundos</td>
            </tr>
            <tr>
                <td>Expirar sesiones</td>
                <td>Cada 15 minutos</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>7. Anotaciones Importantes</h2>
        <table>
            <tr>
                <th>Anotación</th>
                <th>Propósito</th>
            </tr>
            <tr>
                <td><code>@Startup</code></td>
                <td>Inicializar al arrancar la aplicación</td>
            </tr>
            <tr>
                <td><code>@Singleton</code></td>
                <td>Una sola instancia del bean</td>
            </tr>
            <tr>
                <td><code>@Schedule</code></td>
                <td>Tarea programada automática</td>
            </tr>
            <tr>
                <td><code>@Timeout</code></td>
                <td>Manejador de timer</td>
            </tr>
            <tr>
                <td><code>@Resource TimerService</code></td>
                <td>Inyección del servicio de timers</td>
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
        <a href="${pageContext.servletContext.contextPath}/ejercicio6.jsp">Ejercicio 6 - Listeners</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio7.jsp">Ejercicio 7 - File Upload</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio8.jsp">Ejercicio 8 - Async Servlet</a>
    </p>
</body>
</html>