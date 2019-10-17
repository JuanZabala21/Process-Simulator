
package DTO;

/**
 *
 * @author Juan
 */
public class PairFCFS {
    private Integer BurstTime;
    private Integer Identificador;
    private Integer Nivel;
    
    
    public int compareTo(PairFCFS other) {
        if(this.Nivel.intValue()<other.Nivel.intValue()){
            return 1;
        }else if(this.Nivel.intValue()==other.Nivel.intValue()){
            if(this.Identificador.intValue()>other.Identificador.intValue()){
                return 1;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getBurstTime() {
        return BurstTime;
    }

    public void setBurstTime(Integer BurstTime) {
        this.BurstTime = BurstTime;
    }

    public Integer getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(Integer Identificador) {
        this.Identificador = Identificador;
    }

    public Integer getNivel() {
        return Nivel;
    }

    public void setNivel(Integer Nivel) {
        this.Nivel = Nivel;
    }
    
    public PairFCFS() {
        this.BurstTime = null;
        this.Identificador = null;
        this.Nivel = null;
    }

    public PairFCFS(Integer BurstTime, Integer Identificador, Integer Nivel) {
        this.BurstTime = BurstTime;
        this.Identificador = Identificador;
        this.Nivel = Nivel;
    }
    
    
}
