package servidor;

import java.util.Stack;

class NodoExpresion {
    String valor;
    NodoExpresion izquierda;
    NodoExpresion derecha;

    public NodoExpresion(String valor) {
        this.valor = valor;
        this.izquierda = null;
        this.derecha = null;
    }

    public boolean esOperador() {
        return valor.matches("[+\\-*/^]");
    }
}

public class NPItoInfix {
    public static NodoExpresion convertirNPIaArbol(String npi) {
        Stack<NodoExpresion> stack = new Stack<>();
        String[] tokens = npi.split(" ");

        for (String token : tokens) {
            NodoExpresion nodo = new NodoExpresion(token);
            if (nodo.esOperador()) {
                nodo.derecha = stack.pop();
                nodo.izquierda = stack.pop();
            }
            stack.push(nodo);
        }

        if (stack.size() != 1) {
            // La pila debería contener un único árbol de expresión al final.
            throw new IllegalArgumentException("Expresión NPI no válida");
        }

        return stack.pop();
    }

    public static void imprimirInorden(NodoExpresion nodo) {
        if (nodo != null) {
            boolean necesitaParentesis = false;

            if (nodo.esOperador()) {
                if (nodo.izquierda != null && nodo.izquierda.esOperador()) {
                    necesitaParentesis = true;
                }
                if (nodo.derecha != null && nodo.derecha.esOperador()) {
                    necesitaParentesis = true;
                }
            }

            if (necesitaParentesis) {
                System.out.print("(");
            }

            imprimirInorden(nodo.izquierda);
            System.out.print(nodo.valor + " ");
            imprimirInorden(nodo.derecha);

            if (necesitaParentesis) {
                System.out.print(")");
            }
        }
    }

    /*public static void main(String[] args) {
        String expresionNPI = "2 3 + 45 5 * /";
        NodoExpresion arbolExpresion = convertirNPIaArbol(expresionNPI);

        System.out.println("Expresión en notación polaca inversa (NPI): " + expresionNPI);
        System.out.println("Expresión en notación estándar (infix): ");
        imprimirInorden(arbolExpresion);
    }*/

    public void realizarOperacion(String expresion){

        NodoExpresion arbolExpresion = convertirNPIaArbol(expresion);
        imprimirInorden(arbolExpresion);

    }
}
