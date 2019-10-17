
package DAO;

import DTO.dtoProceso;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Juan
 */
public class daoProceso {
    public void MostrarProceso(dtoProceso proceso){
        System.out.println("-----------------------------------------------------");
        System.out.println("IdProceso: " + proceso.getIdentificador());
        System.out.println("EstadoCPU " + proceso.getEstadoCPU());
        System.out.println("Procesador " + proceso.getProcesador());
        System.out.println("Memoria " + proceso.getMemoria().getKey()+ " - " + proceso.getMemoria().getValue());
        System.out.println("Estado del Proceso " + proceso.getEstadoProceso());
        System.out.println("Recursos " + proceso.getRecursos().size());
        System.out.println("Prioridad " + proceso.getPrioridad());
        System.out.println("Contabilizacion " + proceso.getContabilizacion());
        System.out.println("Ancestro " + proceso.getAncestro());
        System.out.println("Descendientes " + proceso.getDescendientes().size());
    }
    
    public static void AgregarProceso(DefaultTableModel Tabla, String id, Integer BurstTime){
        Object[] miTabla = new Object[4];
        miTabla[0]= id;
        miTabla[1]= BurstTime.toString();
        miTabla[2]= BurstTime.toString();
        miTabla[3] = "Listo";
        Tabla.addRow(miTabla);
    }
    
    public static void IngresarInterrupcion(DefaultTableModel modelo, int a, int ContadorInterrupciones){
        ContadorInterrupciones ++;
        Object[] miTabla = new Object[4];
        miTabla[0]= ContadorInterrupciones;//Coloco el numero de interrupcion
        miTabla[1]= String.valueOf(a);
        miTabla[2]= String.valueOf(a);
        miTabla[3]= "Atendiendo";
        modelo.addRow(miTabla);   
    } 
}
