/**
 * Esta clase representa un árbol algebraico que puede convertir expresiones infix en postfix,
 * construir un árbol de expresiones postfix y evaluar el resultado de la expresión.
 */
public class AlgebraicTree {
    /**
     * Clase interna que representa un nodo en el árbol de expresiones algebraicas.
     */
    static class TreeNode{
        String data;
        TreeNode left;
        TreeNode right;
        /**
         * Constructor de un nodo con un valor de datos.
         *
         * @param data El valor de datos para este nodo.
         */
        public TreeNode(String data){
            this(data, null, null);
        }
        /**
         * Constructor de un nodo con un valor de datos, nodo izquierdo y nodo derecho.
         *
         * @param data  El valor de datos para este nodo.
         * @param left  El nodo izquierdo.
         * @param right El nodo derecho.
         */
        public TreeNode(String data, TreeNode left, TreeNode right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
        /**
         * Obtiene el valor de datos del nodo.
         *
         * @return El valor de datos del nodo.
         */
        public String getElement(){
            return data;
        }
        /**
         * Obtiene el nodo izquierdo del nodo actual.
         *
         * @return El nodo izquierdo del nodo actual.
         */
        public TreeNode getLeft(){
            return left;
        }
        /**
         * Obtiene el nodo derecho del nodo actual.
         *
         * @return El nodo derecho del nodo actual.
         */
        public TreeNode getRight(){
            return right;
        }
    }
    /**
     * Verifica si una cadena es un operador válido.
     *
     * @param token La cadena a verificar.
     * @return true si es un operador válido, false en caso contrario.
     */
    public static boolean isOperator(String token){
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("**") || token.equals("%");
    }
    /**
     * Obtiene la precedencia de un operador.
     *
     * @param operator El operador cuya precedencia se desea obtener.
     * @return El nivel de precedencia del operador.
     */
    public static int precedence(String  operator){
        switch (operator){
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
            case "%":
                return 2;
            case "**":
                return 3;
            default:
                return 0;
        }
    }
    /**
     * Convierte una expresión infix en una expresión postfix.
     *
     * @param infix La expresión infix a convertir.
     * @return La expresión postfix resultante.
     */
    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stacks.Stack_LinkedList stack = new Stacks().new Stack_LinkedList();
        String preprocessed = preprocess(infix);

        for (String token : preprocessed.split("\\s+")) {
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && token.startsWith("-") && Character.isDigit(token.charAt(1)))){
                postfix.append(token);
                postfix.append(' ');
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator((String) stack.peek())) {
                    String topOperator = (String) stack.peek();
                    if ((token.equals("**") && precedence(topOperator) > precedence(token))
                            || (!token.equals("**") && precedence(topOperator) >= precedence(token))) {
                        postfix.append(stack.pop());
                        postfix.append(' ');
                    } else {
                        break;
                    }
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop());
                    postfix.append(' ');
                }
                stack.pop();
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
            postfix.append(' ');
        }
        return postfix.toString().trim();
    }

    /**
     * Construye un árbol de expresiones a partir de una expresión postfix.
     *
     * @param postfix La expresión postfix.
     * @return El nodo raíz del árbol de expresiones.
     */
    public static TreeNode postfixToTree(String postfix){
        Stacks.Stack_LinkedList stack = new Stacks().new Stack_LinkedList();
        for (String token : postfix.split("\\s+")){
            if (token.matches("\\d+")){
                stack.push(new TreeNode(token));
            } else if (isOperator(token)){
                TreeNode right = (TreeNode) stack.pop();
                TreeNode left = (TreeNode) stack.pop();
                stack.push(new TreeNode(token, left, right));
            }
        }
        return (TreeNode) stack.pop();
    }
    /**
     * Evalúa el resultado de un árbol de expresiones algebraicas.
     *
     * @param tree El árbol de expresiones a evaluar.
     * @return El resultado de la evaluación de la expresión.
     */
    public static float evaluate(TreeNode tree){
        if (tree == null){
            return 0;
        } else if (tree.getLeft() == null && tree.getRight() == null){
            return Float.parseFloat(tree.getElement());
        } else {
            float left = evaluate(tree.getLeft());
            float right = evaluate(tree.getRight());
            String operator = tree.getElement();
            switch (operator){
                case "+":
                    return left + right;
                case "-":
                    return left - right;
                case "*":
                    return left * right;
                case "/":
                    if (right == 0){
                        return -1;
                    } else {
                        return left / right;
                    }
                case "**":
                    return (float) Math.pow(left, right);
                case "%":
                    return left/100 * right;
                default:
                    return 0;
            }
        }
    }
    /**
     * Realiza un preprocesamiento de una expresión infix para facilitar su manipulación.
     *
     * @param infix La expresión infix a preprocesar.
     * @return La expresión infix preprocesada.
     */
    public static String preprocess(String infix) {
        // Eliminar todos los espacios
        String noSpaces = infix.replaceAll("\\s+", "");

        // Reemplazar doble negativo con positivo
        noSpaces = noSpaces.replaceAll("--", "");

        // Detectar y cambiar el patrón a**(-b) a a**(0-b)
        noSpaces = noSpaces.replaceAll("(\\d+)\\*\\*\\((-\\d+)\\)", "$1**(0$2)");

        // Detectar y cambiar el patrón a*(-b) a - (a*b)
        noSpaces = noSpaces.replaceAll("(\\([^)]+\\)|\\d+)\\*\\((-\\d+)\\)", "$1*(0$2)");

        // Añadir espacios alrededor de todos los operadores
        String spaced = noSpaces.replaceAll("(\\*\\*|[+\\-*/()%^])", " $1 ");

        // Eliminar espacios dobles
        String cleaned = spaced.replaceAll("\\s+", " ").trim();
        return cleaned;
    }

    /**
     * Calcula y devuelve el resultado de una expresión algebraica dada.
     *
     * @param exp La expresión algebraica a evaluar.
     * @return El resultado de la expresión.
     */
    public float result(String exp){
        String infix = exp;
        System.out.println("Infix: " + infix);
        String postfix = infixToPostfix(infix);
        System.out.println("Postfix: " + postfix);
        TreeNode tree = postfixToTree(postfix);
        System.out.println("Result: " + evaluate(tree));
        return evaluate(tree);

    }

    /**
     * Método principal de la clase.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        System.out.println("arbol usado");
        AlgebraicTree at = new AlgebraicTree();

        String[] testExpressions = {
                "5*(-2)",
                "10*(-5) + 3",
                "(6**6)*(-10)",
                "(7 + 1)*(-2)",
                "(-5)*(-5)",
                "(6**(-6))",
                "6/0",
                "--3",
                "--3 + (5 * (-2)) + (6**(-6))"

        };

        for (String expr : testExpressions) {
            System.out.println("Expression: " + expr);
            System.out.println("Result: " + at.result(expr));
            System.out.println("========================");
        }


    }



}
