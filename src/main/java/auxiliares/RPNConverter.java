package auxiliares;

import java.util.Stack;

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

    private static boolean isOperand(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean isOperator(String token) {
        return token.matches("[+\\-*/%^]");
    }

    /*public static void main(String[] args) {
        String rpnExpression = "10 3 %";
        Node expressionTree = buildExpressionTree(rpnExpression);
        String infixExpression = infixExpression(expressionTree);
        System.out.println("RPN: " + rpnExpression);
        System.out.println("Infix: " + infixExpression);
    }*/

    public String convertirInfixToPostFix(String expresion){

        Node expressionTree = buildExpressionTree(expresion);

        System.out.println("resultadou " + infixExpression(expressionTree));

        return infixExpression(expressionTree);

    }
}
