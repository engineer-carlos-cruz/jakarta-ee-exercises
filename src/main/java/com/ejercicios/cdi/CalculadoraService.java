package com.ejercicios.cdi;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CalculadoraService implements ICalculadoraService {

    @Override
    public int suma(int a, int b) {
        return a + b;
    }

    @Override
    public int resta(int a, int b) {
        return a - b;
    }

    @Override
    public int multiplicacion(int a, int b) {
        return a * b;
    }

    @Override
    public double division(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("No se puede dividir por cero");
        }
        return (double) a / b;
    }

    @Override
    public double potencia(double base, double exponente) {
        return Math.pow(base, exponente);
    }

    @Override
    public int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El factorial no existe para números negativos");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}