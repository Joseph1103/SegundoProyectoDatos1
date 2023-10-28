package auxiliares;

import java.util.Stack;
/**
 * Esta clase proporciona métodos para convertir expresiones en notación infija
 * a notación posfija utilizando una pila.
 */
public class InfixToPostfix {
    /**
     * Convierte una expresión en notación infija a notación posfija.
     *
     * @param expresion La expresión en notación infija que se va a convertir.
     * @return La expresión en notación posfija.
     */
    public static String conversionPostfijo(String expresion) {
        String epostfija = "";
        Stack<Character> stack = new Stack<>();
        int i = 0;

        while (i < expresion.length()) {

            StringBuilder token = new StringBuilder();

            // Leer un número o un operador
            while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)))) {//|| esOperador(expresion.charAt(i))))) {
                token.append(expresion.charAt(i));
                i++;
            }

            if (token.length() > 0) {
                epostfija += token.toString() + " ";
            }

            if (i < expresion.length()) {
                char car = expresion.charAt(i);
                if (jerarquia(car) > 0) {
                    while (!stack.isEmpty() && jerarquia(stack.peek()) >= jerarquia(car)) {
                        epostfija += stack.pop() + " ";
                    }
                    stack.push(car);
                    i++;
                } else if (car == ')') {
                    char aux = stack.pop();
                    while (aux != '(') {
                        epostfija += aux + " ";
                        aux = stack.pop();
                    }
                    i++;
                } else if (car == '(') {
                    stack.push(car);
                    i++;
                } else {
                    i++;
                }
            }

        }

        while (!stack.isEmpty()) {
            epostfija += stack.pop() + " ";
        }

        return epostfija;
    }
    /**
     * Determina la jerarquía de un operador.
     *
     * @param car El operador cuya jerarquía se va a determinar.
     * @return El nivel de jerarquía del operador.
     */

    private static int jerarquia(char car) {
        switch (car) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }
    /**
     * Verifica si un carácter es un operador.
     *
     * @param car El carácter que se va a verificar.
     * @return `true` si el carácter es un operador, `false` en caso contrario.
     */

    private static boolean esOperador(char car) {
        return car == '+' || car == '-' || car == '*' || car == '/' || car == '^' || car == '%';
    }
    /**
     * Método principal para realizar una conversión de notación posfija.
     * Ejemplo de uso:
     * <pre>
     * String result = conversionPostfijo("((22/2)*((2+4)*(4*((6-7)*5)))");
     * System.out.println(result);
     * </pre>
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */

    public static void main(String[] args) {
        String result = conversionPostfijo("((22/2)*((2+4)*(4*((6-7)*5)))");
        System.out.println(result);
    }
    /**
     * Convierte una expresión en notación infija a notación posfija.
     *
     * @param expresion La expresión en notación infija que se va a convertir.
     * @return La expresión en notación posfija.
     */

    public String conversionPosfijo(String expresion) {
        return conversionPostfijo(expresion);
    }
}