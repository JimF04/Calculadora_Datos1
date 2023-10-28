import com.fathzer.soft.javaluator.DoubleEvaluator;

import java.util.function.Function;


class Expresion {
    public double evaluar() {
        return 0.0;
    }
}

class Numero extends Expresion {
    private double valor;

    public Numero(double valor) {
        this.valor = valor;
    }

    public double evaluar() {
        return valor;
    }
}

class Variable extends Expresion {
    private String nombre;
    private Function<Double, Double> funcion;

    public Variable(String nombre, Function<Double, Double> funcion) {
        this.nombre = nombre;
        this.funcion = funcion;
    }

    public double evaluar() {
        // Evaluar la variable en un valor específico usando la función proporcionada
        return funcion.apply(0.0); // Puedes reemplazar 0.0 con el valor deseado
    }
}

class OperadorBinario extends Expresion {
    private char operador;
    private Expresion izquierda;
    private Expresion derecha;

    public OperadorBinario(char operador, Expresion izquierda, Expresion derecha) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

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

class IntegralDefinida extends Expresion {
    private Expresion funcion;
    private Variable variable;
    private double limiteInferior;
    private double limiteSuperior;

    public IntegralDefinida(Expresion funcion, Variable variable, double limiteInferior, double limiteSuperior) {
        this.funcion = funcion;
        this.variable = variable;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
    }

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

public class Protree {
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

    // Función para evaluar la expresión en un punto dado
    private static double evaluarFuncion(String expresion, double x) {
        DoubleEvaluator evaluator = new DoubleEvaluator();
        return evaluator.evaluate(expresion.replace("x", String.valueOf(x)));
    }
}