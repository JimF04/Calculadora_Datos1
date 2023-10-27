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
        int numIntervalos = 1000; // Puedes ajustar la cantidad de intervalos
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
        // Define la función f(x) = x^2
        Function<Double, Double> funcionCuadratica = x -> x * x;

        // Crea una variable para x
        Variable x = new Variable("x", funcionCuadratica);

        // Construye la expresión f(x) = x^2
        Expresion f_x = new OperadorBinario('*', x, x);

        // Calcula la integral definida de f(x) en [0, 1]
        IntegralDefinida integral = new IntegralDefinida(f_x, x, 0.0, 1.0);

        double resultado = integral.evaluar();
        System.out.println("El resultado de la integral definida es: " + resultado);
    }
}