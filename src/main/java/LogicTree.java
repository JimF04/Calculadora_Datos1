/**
 * Esta clase representa un árbol lógico que puede convertir expresiones infix en postfix,
 * construir un árbol de expresiones postfix y evaluar el resultado de la expresión lógica.
 */
public class LogicTree {
    /**
     * Clase interna que representa un nodo en el árbol de expresiones lógicas.
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
     * Verifica si una cadena es un operador lógico válido.
     *
     * @param token La cadena a verificar.
     * @return true si es un operador lógico válido, false en caso contrario.
     */
    public static boolean isOperator(String token){
        return token.equals("&") || token.equals("|") || token.equals("^") || token.equals("~");
    }
    /**
     * Obtiene la precedencia de un operador lógico.
     *
     * @param operator El operador cuya precedencia se desea obtener.
     * @return El nivel de precedencia del operador.
     */
    public static int precedence(String  operator){
        switch (operator){
            case "|":
            case "&":
            case "^":
                return 1;
            case "~":
                return 2;
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

        for (String token : infix.split("\\s+")) {
//            System.out.println(token.charAt(0));
            if (Character.isLetter(token.charAt(0))) {
                postfix.append(token);
                postfix.append(' ');
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator((String) stack.peek())) {
                    String topOperator = (String) stack.peek();
                    if (precedence(topOperator) >= precedence(token)) {
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
     * Construye un árbol de expresiones lógicas a partir de una expresión postfix.
     *
     * @param postfix La expresión postfix.
     * @return El nodo raíz del árbol de expresiones lógicas.
     */
    public static TreeNode postfixToTree(String postfix){
        Stacks.Stack_LinkedList stack = new Stacks().new Stack_LinkedList();
        for (String token : postfix.split("\\s+")){
            if (Character.isLetter(token.charAt(0))){
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
     * Evalúa el resultado de un árbol de expresiones lógicas.
     *
     * @param tree El árbol de expresiones a evaluar.
     * @return El resultado de la evaluación de la expresión lógica.
     */
    public static boolean evaluate(TreeNode tree){
//        System.out.println(tree.data);
        if (tree == null){
            return false;
        } else if (tree.getLeft() == null && tree.getRight() == null){
            return Boolean.parseBoolean(tree.getElement());
        } else {
            boolean left = evaluate(tree.getLeft());
            boolean right = evaluate(tree.getRight());

            String operator = tree.getElement();
            switch (operator){
                case "|":
                    return left | right;
                case "&":
                    return left & right;
                case "^":
                    return left ^ right;
                case "~":
                    try {
                        return !right;

                    } catch (Exception e) {
                        System.out.println(e);
                    }


                default:
                    return true;
            }
        }
    }
    /**
     * Calcula y devuelve el resultado de una expresión lógica dada.
     *
     * @param exp La expresión lógica a evaluar.
     * @return El resultado de la expresión lógica.
     */
    public boolean result(String exp){
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

    }
}
