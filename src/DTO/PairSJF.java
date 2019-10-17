
package DTO;

/**
 *
 * @author Juan
 */
public class PairSJF implements Comparable<PairSJF>{
    
    private Integer BurstTime;
    private Integer Nivel;
    private Integer Identificador;
    
    @Override
    public int compareTo(PairSJF other) {
        if(this.BurstTime.intValue()>other.BurstTime.intValue()){
            return 1;
        }else if(this.BurstTime.intValue()==other.BurstTime.intValue()){
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

    public Integer getNivel() {
        return Nivel;
    }

    public void setNivel(Integer Nivel) {
        this.Nivel = Nivel;
    }

    public Integer getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(Integer Identificador) {
        this.Identificador = Identificador;
    }

    public PairSJF() {
        this.BurstTime = null;
        this.Nivel = null;
        this.Identificador = null;
    }
    
    public PairSJF(Integer BurstTime, Integer Nivel, Integer Identificador) {
        this.BurstTime = BurstTime;
        this.Nivel = Nivel;
        this.Identificador = Identificador;
    }

}
