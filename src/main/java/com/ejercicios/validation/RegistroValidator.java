package com.ejercicios.validation;

import com.ejercicios.dto.RegistroDTO;
import jakarta.annotation.Resource;
import jakarta.validation.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.*;

@Named
@RequestScoped
public class RegistroValidator {

    @Resource
    private Validator validator;

    public List<String> validar(RegistroDTO dto) {
        List<String> errores = new ArrayList<>();

        if (dto.getPassword() != null && dto.getConfirmarPassword() != null 
            && !dto.getPassword().equals(dto.getConfirmarPassword())) {
            errores.add("Las contraseñas no coinciden");
        }

        Set<ConstraintViolation<RegistroDTO>> violations = validator.validate(dto);
        for (ConstraintViolation<RegistroDTO> v : violations) {
            errores.add(v.getMessage());
        }

        return errores;
    }

    public boolean esValido(RegistroDTO dto) {
        return validar(dto).isEmpty();
    }
}