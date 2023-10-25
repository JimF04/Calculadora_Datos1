public class AlgebraicTree {
    static class TreeNode{
        String data;
        TreeNode left;
        TreeNode right;

        public TreeNode(String data){
            this(data, null, null);
        }
        public TreeNode(String data, TreeNode left, TreeNode right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
        public String getElement(){
            return data;
        }
        public TreeNode getLeft(){
            return left;
        }
        public TreeNode getRight(){
            return right;
        }
    }

    public static boolean isOperator(String token){
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("**") || token.equals("%");
    }

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

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stacks.Stack_LinkedList stack = new Stacks().new Stack_LinkedList();
        String preprocessed = preprocess(infix);

        for (String token : preprocessed.split("\\s+")) {
            if (Character.isDigit(token.charAt(0))) {
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

    public static float evaluate(TreeNode tree){
        if (tree == null){
            return 0;
        } else if (tree.getLeft() == null && tree.getRight() == null){
            return Integer.parseInt(tree.getElement());
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
                    return left / right;
                case "**":
                    return (float) Math.pow(left, right);
                case "%":
                    return left/100 * right;
                default:
                    return 0;
            }
        }
    }

    public static String preprocess(String infix) {
        // Eliminar todos los espacios
        String noSpaces = infix.replaceAll("\\s+", "");

        // Añadir espacios alrededor de todos los operadores
        String spaced = noSpaces.replaceAll("(\\*\\*|[+\\-*/()%^])", " $1 ");

        // Eliminar espacios dobles
        String cleaned = spaced.replaceAll("\\s+", " ").trim();
        return cleaned;
    }
    public void result(String exp){
        String infix = exp;
        System.out.println("Infix: " + infix);
        String postfix = infixToPostfix(infix);
        System.out.println("Postfix: " + postfix);
        TreeNode tree = postfixToTree(postfix);
        System.out.println("Result: " + evaluate(tree));

    }


    public static void main(String[] args) {
        System.out.println("arbol usado");

    }



}
