package com.ejercicios.cdi;

public interface ICalculadoraService {
    int suma(int a, int b);
    int resta(int a, int b);
    int multiplicacion(int a, int b);
    double division(int a, int b);
    double potencia(double base, double exponente);
    int factorial(int n);
}