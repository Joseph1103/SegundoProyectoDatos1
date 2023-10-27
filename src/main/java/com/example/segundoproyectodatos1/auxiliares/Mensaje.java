package auxiliares;

import java.io.Serializable;

public class Mensaje implements Serializable {

    private String accion, expresionMatematica;


    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getExpresionMatematica() {
        return expresionMatematica;
    }

    public void setExpresionMatematica(String expresionMatematica) {
        this.expresionMatematica = expresionMatematica;
    }
}
