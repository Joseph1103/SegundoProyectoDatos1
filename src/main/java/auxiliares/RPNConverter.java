package auxiliares;

import java.util.Stack;

/**
 * Esta clase proporciona métodos para construir un árbol de expresión a partir de una
 * expresión en notación polaca inversa (RPN) y para convertir dicho árbol en una
 * expresión infija.
 */
class Node {
    String value;
    Node left;
    Node right;

    public Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

public class RPNConverter {

    /**
     * Construye un árbol de expresión a partir de una expresión en notación polaca inversa (RPN).
     *
     * @param rpn La expresión en notación polaca inversa.
     * @return El nodo raíz del árbol de expresión.
     */
    public static Node buildExpressionTree(String rpn) {
        String[] tokens = rpn.split(" ");
        Stack<Node> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                stack.push(new Node(token));
            } else if (isOperator(token)) {
                Node operand2 = stack.pop();
                Node operand1 = stack.pop();
                Node operatorNode = new Node(token);
                operatorNode.left = operand1;
                operatorNode.right = operand2;
                stack.push(operatorNode);
            }
        }

        if (stack.size() == 1) {
            return stack.pop();
        } else {
            // Error handling: The RPN expression is not well-formed
            return null;
        }
    }

    /**
     * Convierte un árbol de expresión en una expresión infija.
     *
     * @param root El nodo raíz del árbol de expresión.
     * @return La expresión infija resultante.
     */
    public static String infixExpression(Node root) {
        if (root == null) {
            return "";
        }

        if (isOperator(root.value)) {
            return "(" + infixExpression(root.left) + " " + root.value + " " + infixExpression(root.right) + ")";
        } else {
            return root.value;
        }
    }

    /**
     * Verifica si un token es un operando (número).
     *
     * @param token El token que se va a verificar.
     * @return `true` si el token es un operando, `false` en caso contrario.
     */
    private static boolean isOperand(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Verifica si un token es un operador.
     *
     * @param token El token que se va a verificar.
     * @return `true` si el token es un operador, `false` en caso contrario.
     */
    private static boolean isOperator(String token) {
        return token.matches("[+\\-*/%^]");
    }


    /**
     * Convierte una expresión en notación infija en una expresión en notación
     * posfija (RPN).
     *
     * @param expresion La expresión en notación infija que se va a convertir.
     * @return La expresión en notación RPN.
     */
    public String convertirInfixToPostFix(String expresion){

        Node expressionTree = buildExpressionTree(expresion);

        System.out.println("resultadou " + infixExpression(expressionTree));

        return infixExpression(expressionTree);

    }
}
