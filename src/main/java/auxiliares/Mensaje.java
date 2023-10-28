package auxiliares;

import java.io.Serializable;

/**
 * Esta clase representa un objeto Mensaje que puede ser serializado para
 * ser transmitido entre sistemas. Un objeto Mensaje contiene una acción,
 * una expresión matemática, una respuesta y un número de puerto.
 */
public class Mensaje implements Serializable {

    private String accion, expresionMatematica;
    private Object respuesta;
    private int puerto;
    String fecha;
    String operacion, resultado;

    /**
     * Obtiene la acción asociada al mensaje.
     *
     * @return La acción del mensaje.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * Establece la acción asociada al mensaje.
     *
     * @param accion La acción del mensaje.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Obtiene la expresión matemática asociada al mensaje.
     *
     * @return La expresión matemática del mensaje.
     */
    public String getExpresionMatematica() {
        return expresionMatematica;
    }

    /**
     * Establece la expresión matemática asociada al mensaje.
     *
     * @param expresionMatematica La expresión matemática del mensaje.
     */
    public void setExpresionMatematica(String expresionMatematica) {
        this.expresionMatematica = expresionMatematica;
    }

    /**
     * Obtiene la respuesta asociada al mensaje.
     *
     * @return La respuesta del mensaje.
     */
    public Object getRespuesta() {
        return respuesta;
    }
    /**
     * Establece la respuesta asociada al mensaje.
     *
     * @param respuesta La respuesta del mensaje.
     */
    public void setRespuesta(Object respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * Obtiene el número de puerto asociado al mensaje.
     *
     * @return El número de puerto del mensaje.
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Establece el número de puerto asociado al mensaje.
     *
     * @param puerto El número de puerto del mensaje.
     */
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getOperacion() {
        return operacion;
    }

    public String getResultado() {
        return resultado;
    }
}
