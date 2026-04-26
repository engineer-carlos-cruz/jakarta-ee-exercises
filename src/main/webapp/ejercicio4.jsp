<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ejercicio 4 - Bean Validation</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 30px auto; padding: 20px; }
        section { background: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 5px; }
        h2 { color: #333; border-bottom: 2px solid #dc3545; padding-bottom: 10px; }
        .ejemplo { background: #e9ecef; padding: 10px; border-left: 4px solid #dc3545; margin: 10px 0; }
        code { background: #272822; color: #f8f8f2; padding: 2px 6px; border-radius: 3px; }
        pre { background: #272822; color: #f8f8f2; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .btn { background: #dc3545; color: white; padding: 10px 20px; border: none; border-radius: 3px; cursor: pointer; }
        a { color: #dc3545; }
        .resultado { background: #d4edda; border: 1px solid #c3e6cb; padding: 15px; margin: 10px 0; border-radius: 3px; }
        .error { background: #f8d7da; border: 1px solid #f5c6cb; padding: 15px; margin: 10px 0; border-radius: 3px; color: #721c24; }
        .error ul { margin: 5px 0; padding-left: 20px; }
        form { background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="password"], input[type="number"] {
            width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 3px; box-sizing: border-box; }
        .nota { color: #666; font-size: 12px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #dc3545; color: white; }
    </style>
</head>
<body>
    <h1>Ejercicio 4: Bean Validation</h1>
    <p>Este ejercicio demuestra la validación de beans usando Jakarta Bean Validation.</p>

    <c:set var="errores" value="${requestScope.errores}" />
    <c:set var="exito" value="${requestScope.exito}" />

    <c:if test="${not empty errores}">
        <div class="error">
            <strong>Errores de validación:</strong>
            <ul>
                <c:forEach var="err" items="${errores}">
                    <li>${err}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <c:if test="${exito == 'true'}">
        <div class="resultado">
            <strong>¡Registro exitoso!</strong>
            <p>Bienvenido, ${registro.nombre}!</p>
            <p>Tu email: ${registro.email}</p>
            <p>Tu edad: ${registro.edad} años</p>
            <p>Código postal: ${registro.codigoPostal}</p>
        </div>
    </c:if>

    <section>
        <h2>1. Formulario de Registro</h2>
        <div class="ejemplo">
            <p>Usa Bean Validation para validar los datos del formulario.</p>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/ejercicio4" autocomplete="off">
            <label>Nombre: <span class="nota">(3-50 caracteres)</span>
            <input type="text" name="nombre" value="${param.nombre}" placeholder="Tu nombre completo">

            <label>Email:
            <input type="email" name="email" value="${param.email}" placeholder="correo@ejemplo.com">

            <label>Contraseña: <span class="nota">(6-20 caracteres)</span>
            <input type="password" name="password" placeholder="Contraseña">

            <label>Confirmar Contraseña:
            <input type="password" name="confirmarPassword" placeholder="Repite la contraseña">

            <label>Edad: <span class="nota">(18-120 años)</span>
            <input type="number" name="edad" value="${param.edad}" placeholder="Tu edad">

            <label>Código Postal: <span class="nota">(5 dígitos)</span>
            <input type="text" name="codigoPostal" value="${param.codigoPostal}" placeholder="12345" maxlength="5">

            <label>
            <input type="checkbox" name="terminos" value="aceptado" ${param.terminos == 'aceptado' ? 'checked' : ''}>
            Acepto los términos y condiciones
            </label>

            <button type="submit" class="btn">Registrar</button>
        </form>
    </section>

    <section>
        <h2>2. Anotaciones de Bean Validation</h2>
        <table>
            <tr>
                <th>Anotación</th>
                <th>Descripción</th>
                <th>Ejemplo</th>
            </tr>
            <tr>
                <td><code>@NotNull</code></td>
                <td>No puede ser null</td>
                <td>Integer edad</td>
            </tr>
            <tr>
                <td><code>@NotBlank</code></td>
                <td>No puede estar vacío</td>
                <td>String nombre</td>
            </tr>
            <tr>
                <td><code>@Email</code></td>
                <td>Formato email válido</td>
                <td>String email</td>
            </tr>
            <tr>
                <td><code>@Size</code></td>
                <td>Tamaño min-max</td>
                <td>@Size(min=3,max=50)</td>
            </tr>
            <tr>
                <td><code>@Min/@Max</code></td>
                <td>Valor numérico</td>
                <td>@Min(18) @Max(120)</td>
            </tr>
            <tr>
                <td><code>@Pattern</code></td>
                <td>Expresión regular</td>
                <td>@Pattern(regexp="\\d{5}")</td>
            </tr>
            <tr>
                <td><code>@DecimalMin/Max</code></td>
                <td>Decimal min/max</td>
                <td>@DecimalMin("0.0")</td>
            </tr>
            <tr>
                <td><code>@Digits</code></td>
                <td>Dígitos permitidos</td>
                <td>@Digits(integer=2,fraction=3)</td>
            </tr>
            <tr>
                <td><code>@Past/@Future</code></td>
                <td>Fecha pasada/futura</td>
                <td>@Past Date fecha</td>
            </tr>
            <tr>
                <td><code>@NotEmpty</code></td>
                <td>No vacío (colecciones)</td>
                <td>List&lt;String&gt; items</td>
            </tr>
        </table>
    </section>

    <section>
        <h2>3. Validación Programática</h2>
        <pre>
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

// Obtener validator
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();

// Validar objeto
Set&lt;ConstraintViolation&lt;RegistroDTO&gt;&gt; violations = validator.validate(registroDTO);
for (ConstraintViolation&lt;RegistroDTO&gt; v : violations) {
    System.out.println(v.getMessage());
}
        </pre>
    </section>

    <section>
        <h2>4. Validación con CDI</h2>
        <pre>
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@Inject
private Validator validator;

public List&lt;String&gt; validar(RegistroDTO dto) {
    Set&lt;ConstraintViolation&lt;RegistroDTO&gt;&gt; violations = validator.validate(dto);
    return violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
}
        </pre>
    </section>

    <hr>
    <p>
        <a href="${pageContext.servletContext.contextPath}/index.jsp">Ejercicio 1 - JSP</a> | 
        <a href="${pageContext.servletContext.contextPath}/ejercicio2.jsp">Ejercicio 2 - JSTL</a> |
        <a href="${pageContext.servletContext.contextPath}/ejercicio3.jsp">Ejercicio 3 - CDI</a>
    </p>
</body>
</html>