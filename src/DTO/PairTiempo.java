
package DTO;

/**
 *
 * @author Juan
 */
public class PairTiempo {
    private Integer Inicio;
    private Integer Fin;

    public Integer getInicio() {
        return Inicio;
    }

    public void setInicio(Integer Inicio) {
        this.Inicio = Inicio;
    }

    public Integer getFin() {
        return Fin;
    }

    public void setFin(Integer Fin) {
        this.Fin = Fin;
    }

    public PairTiempo() {
        this.Inicio = null;
        this.Fin = null;
    }
    
    public PairTiempo(Integer Inicio, Integer Fin) {
        this.Inicio = Inicio;
        this.Fin = Fin;
    }
    
}
