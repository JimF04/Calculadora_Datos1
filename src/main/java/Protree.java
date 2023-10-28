import com.fathzer.soft.javaluator.DoubleEvaluator;

import java.util.function.Function;

/**
 * Representa una expresión matemática genérica.
 */
class Expresion {
    /**
     * Evalúa la expresión.
     *
     * @return Resultado de la evaluación.
     */
    public double evaluar() {
        return 0.0;
    }
}

/**
 * Representa un número en la expresión.
 */
class Numero extends Expresion {
    private double valor;
    /**
     * Constructor para un número.
     *
     * @param valor Valor numérico.
     */
    public Numero(double valor) {
        this.valor = valor;
    }
    /**
     * {@inheritDoc}
     */
    public double evaluar() {
        return valor;
    }
}
/**
 * Representa una variable en la expresión.
 */
class Variable extends Expresion {
    private String nombre;
    private Function<Double, Double> funcion;
    /**
     * Constructor para una variable.
     *
     * @param nombre Nombre de la variable.
     * @param funcion Función asociada a la variable.
     */
    public Variable(String nombre, Function<Double, Double> funcion) {
        this.nombre = nombre;
        this.funcion = funcion;
    }
    /**
     * {@inheritDoc}
     */
    public double evaluar() {
        // Evaluar la variable en un valor específico usando la función proporcionada
        return funcion.apply(0.0); // Puedes reemplazar 0.0 con el valor deseado
    }
}
/**
 * Representa una operación binaria en la expresión.
 */
class OperadorBinario extends Expresion {
    private char operador;
    private Expresion izquierda;
    private Expresion derecha;
    /**
     * Constructor para una operación binaria.
     *
     * @param operador Operador binario.
     * @param izquierda Expresión del lado izquierdo.
     * @param derecha Expresión del lado derecho.
     */
    public OperadorBinario(char operador, Expresion izquierda, Expresion derecha) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }
    /**
     * {@inheritDoc}
     */
    public double evaluar() {
        double izq = izquierda.evaluar();
        double der = derecha.evaluar();
        switch (operador) {
            case '+':
                return izq + der;
            case '-':
                return izq - der;
            case '*':
                return izq * der;
            case '/':
                return izq / der;
            default:
                return 0.0;
        }
    }
}
/**
 * Representa una integral definida en la expresión.
 */
class IntegralDefinida extends Expresion {
    private Expresion funcion;
    private Variable variable;
    private double limiteInferior;
    private double limiteSuperior;
    /**
     * Constructor para una integral definida.
     *
     * @param funcion Función a integrar.
     * @param variable Variable de integración.
     * @param limiteInferior Límite inferior de integración.
     * @param limiteSuperior Límite superior de integración.
     */
    public IntegralDefinida(Expresion funcion, Variable variable, double limiteInferior, double limiteSuperior) {
        this.funcion = funcion;
        this.variable = variable;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
    }
    /**
     * {@inheritDoc}
     */
    public double evaluar() {
        // Método de aproximación numérica para calcular la integral definida
        int numIntervalos = 1000000; // Puedes ajustar la cantidad de intervalos
        double anchoIntervalo = (limiteSuperior - limiteInferior) / numIntervalos;
        double suma = 0.0;

        for (int i = 0; i < numIntervalos; i++) {
            double x1 = limiteInferior + i * anchoIntervalo;
            double x2 = x1 + anchoIntervalo;
            double area = (funcion.evaluar() + funcion.evaluar()) / 2 * anchoIntervalo;
            suma += area;
        }

        return suma;
    }
}
/**
 * Clase principal que contiene métodos para evaluar y calcular integrales.
 */
public class Protree {
    /**
     * Punto de entrada principal.
     *
     * @param args Argumentos de línea de comando.
     */
    public static void main(String[] args) {
        DoubleEvaluator evaluator = new DoubleEvaluator();

        // Permite al usuario ingresar la expresión matemática
        String expresion = "x^3 + 1"; // Ejemplo de expresión
        // Define los límites de integración
        double limiteInferior = 4.0;
        double limiteSuperior = 10.0;
        // Añadir código para permitir al usuario ingresar los límites de integración

        int numIntervalos = 1000000; // Ajusta el número de intervalos
        double anchoIntervalo = (limiteSuperior - limiteInferior) / numIntervalos;
        double suma = 0.0;

        for (int i = 0; i < numIntervalos; i++) {
            double x1 = limiteInferior + i * anchoIntervalo;
            double x2 = x1 + anchoIntervalo;
            // Evalúa la función en los puntos del intervalo
            double area = (evaluarFuncion(expresion, x1) + evaluarFuncion(expresion, x2)) / 2 * anchoIntervalo;
            suma += area;
        }

        System.out.println("El resultado de la integral definida es: " + suma);
    }
    /**
     * Calcula la integral definida de una expresión entre dos límites.
     *
     * @param expresion Expresión a integrar.
     * @param limiteInferior Límite inferior de integración.
     * @param limiteSuperior Límite superior de integración.
     * @return Resultado de la integral.
     */
    public static double calculateIntegral(String expresion, double limiteInferior, double limiteSuperior) {
        int numIntervalos = 1000000; // Ajusta el número de intervalos
        double anchoIntervalo = (limiteSuperior - limiteInferior) / numIntervalos;
        double suma = 0.0;

        for (int i = 0; i < numIntervalos; i++) {
            double x1 = limiteInferior + i * anchoIntervalo;
            double x2 = x1 + anchoIntervalo;
            // Evalúa la función en los puntos del intervalo
            double area = (evaluarFuncion(expresion, x1) + evaluarFuncion(expresion, x2)) / 2 * anchoIntervalo;
            suma += area;
        }

        return suma;  // Devuelve el resultado de la integral
    }

    /**
     * Evalúa una expresión en un valor específico.
     *
     * @param expresion Expresión a evaluar.
     * @param x Valor en el que se evaluará la expresión.
     * @return Resultado de la evaluación.
     */
    private static double evaluarFuncion(String expresion, double x) {
        DoubleEvaluator evaluator = new DoubleEvaluator();
        return evaluator.evaluate(expresion.replace("x", String.valueOf(x)));
    }
}