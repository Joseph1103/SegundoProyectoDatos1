package servidor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package Nodos;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Rafael Angel Montero Fernpandez.
 * Correo: rafaelangelmfx@gmail.com
 * Celular de Costa Rica: +506 83942235
 * Muy ultil para crear un analizador sintactico.
 * Las expresiones salen bien si se respeta la cantidad de terminos.
 * Al final tiene que ir la misma cantidad de operadores que terminos.
 * Menos cantidad omite numeros y terminos de la operasion.
 * Más cantidad coloca un operador al principio sin numeros a su izquierda.
 */
public class ArbolExpresion {
    public static void main(String []m){

        final String EXPRESION_DE_PRUEBA ="ab+cde+**", Ejmplo2="22/24+467-5****";
        NodoExpresivo raiz=new NodoExpresivo("");
        raiz=raiz.CrearArbolDeExpreciones(Ejmplo2);
        System.out.println("Expresion postfix mostrada en PostOrder");
        raiz.postOrder();
        System.out.println("\n\nExpresion Infix mostrada en InOrder");

        String operacion = raiz.inOrder();
        System.out.println(operacion);

        Context rhino = Context.enter();

        Scriptable scope = rhino.initStandardObjects();

        try {
            Object result = rhino.evaluateString(scope,operacion,"Evaluacion",1,null);

            System.out.println(result);

        }finally {
            Context.exit();
        }

        /*ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("rhino");

        try {
            Object result = engine.eval(operacion);

            System.out.println("Result: " + result);
        }catch (ScriptException e){
            e.printStackTrace();
        }*/


    }
}


class NodoExpresivo{
    public NodoExpresivo Iz, Dr;
    public String texto="";

    public NodoExpresivo(String nuevo_texto){
        texto=nuevo_texto;
    }

    public NodoExpresivo(String nuevo_texto, NodoExpresivo izquierda, NodoExpresivo derecha){
        texto=nuevo_texto;
        Iz=izquierda;
        Dr=derecha;
    }

    public boolean esOperador(String nuevo_texto){
        String []mOperadores={"+","-","*","/","=","^"};
        for(int i=0; i<mOperadores.length; ++i){
            if(nuevo_texto.equalsIgnoreCase(mOperadores[i])==true ){
                return true;
            }
        }

        return false;
    }

    private void msj(String datos){
        System.out.println(datos);
    }

    public void postOrder(){
        vPostOrder="";
        System.out.println(postOrder(this));
    }

    private String vPostOrder="";
    private String postOrder(NodoExpresivo nueva_raiz){
        if(nueva_raiz==null){
            return "";
        }
        postOrder(nueva_raiz.Iz);
        postOrder(nueva_raiz.Dr);
        vPostOrder+=nueva_raiz.texto;
        return vPostOrder;
    }


    public String inOrder(){
        vInOrder="";
        //System.out.println(inOrder(this));
        return inOrder(this);

    }
    private String vInOrder="";
    private String inOrder(NodoExpresivo nueva_raiz){
        if(nueva_raiz==null){
            return "";
        }

        if(this.esOperador(nueva_raiz.texto)==true){
            vInOrder+="(";
        }

        inOrder(nueva_raiz.Iz);
        vInOrder+=nueva_raiz.texto;
        inOrder(nueva_raiz.Dr);

        if(this.esOperador(nueva_raiz.texto)==true){
            vInOrder+=")";
        }
        return vInOrder;
    }


    /**
     * Permite crear un arbol de expreciones a partir de una expresion tipo postfix.
     * En la cual los operadores aparecen en medio o al final de la expresion (cadena de texto).
     * @param expresion Un postfix o exprecion matemativa a analizar para crear el arbol.
     * @return Retorna el arbol creado.
     */
    public NodoExpresivo CrearArbolDeExpreciones(String expresion){
        if(expresion.isEmpty()==true){
            return null;
        }
        Stack<NodoExpresivo> s=new Stack();//Primer uso de Stack en Java, jueves 1 de setiembre del 2022.
        for(char c: expresion.toCharArray()){
            if(this.esOperador(c + "")==true){
                //La entrada actual es un operador.
                NodoExpresivo x=null;
                try{
                    x=s.pop();
                }catch(EmptyStackException e){}

                NodoExpresivo y=null;
                try{
                    y=s.pop();
                }catch(EmptyStackException e){}

                //Un nuevo arbol binario de expreciones.
                NodoExpresivo nodo = new NodoExpresivo(c +"", y, x);
                s.push(nodo); //Se inserta en la pila.
            }
            else{
                //La entrada actual es un operando.
                s.push(new NodoExpresivo(c+""));
            }
        }
        return s.peek(); //Retorna el arbol sin borrarlo de la pila.
    }
}