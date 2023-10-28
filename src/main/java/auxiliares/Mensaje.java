package auxiliares;

import java.io.Serializable;

public class Mensaje implements Serializable {

    private String accion, expresionMatematica;
    private Object respuesta;
    private int puerto;

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

    public Object getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Object respuesta) {
        this.respuesta = respuesta;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
}
