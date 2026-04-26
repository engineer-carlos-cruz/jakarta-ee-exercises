package com.ejercicios.servlet;

import com.ejercicios.dto.RegistroDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "Ejercicio4Servlet", urlPatterns = {"/ejercicio4"})
public class Ejercicio4Servlet extends HttpServlet {

    private Validator validator;

    @Override
    public void init() throws ServletException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RegistroDTO registro = new RegistroDTO();
        registro.setNombre(request.getParameter("nombre"));
        registro.setEmail(request.getParameter("email"));
        registro.setPassword(request.getParameter("password"));
        registro.setConfirmarPassword(request.getParameter("confirmarPassword"));
        
        String edadStr = request.getParameter("edad");
        if (edadStr != null && !edadStr.isEmpty()) {
            try {
                registro.setEdad(Integer.parseInt(edadStr));
            } catch (NumberFormatException e) {
                // ignora
            }
        }
        
        registro.setCodigoPostal(request.getParameter("codigoPostal"));
        registro.setTerminos(request.getParameter("terminos"));

        List<String> errores = new ArrayList<>();

        if (registro.getPassword() != null && registro.getConfirmarPassword() != null
                && !registro.getPassword().equals(registro.getConfirmarPassword())) {
            errores.add("Las contraseñas no coinciden");
        }

        Set<ConstraintViolation<RegistroDTO>> violations = validator.validate(registro);
        for (ConstraintViolation<RegistroDTO> v : violations) {
            errores.add(v.getMessage());
        }

        if (errores.isEmpty()) {
            request.setAttribute("registro", registro);
            request.setAttribute("exito", "true");
        } else {
            request.setAttribute("errores", errores);
        }

        request.getRequestDispatcher("/ejercicio4.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/ejercicio4.jsp");
    }

    @Override
    public void destroy() {
        if (validator != null) {
            validator = null;
        }
    }
}