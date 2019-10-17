
package DAO;

import DTO.dtoProceso;
import Ventana.Estadisticas;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Ventana.Ventana_No_Expropiativo;
import javax.swing.JFrame;

/**
 *
 * @author Juan
 */
public class daoPCB_NoExp {
    public static void CargarPCB(Ventana_No_Expropiativo ventana, int tipo, dtoProceso proceso, int ini, int fin){
        if(tipo==1){
            ventana.tblPCB_FCFS.setBackground(Color.WHITE);
            DefaultTableModel modelo = (DefaultTableModel) ventana.tblPCB_FCFS.getModel();
            Object[] miTabla = new Object[11];
            miTabla[0]=proceso.getIdentificador();
            miTabla[1]=proceso.getEstadoCPU();
            miTabla[2]=proceso.getProcesador();
            miTabla[3]=proceso.getMemoria().getKey().toString()+'-'+proceso.getMemoria().getValue().toString();
            miTabla[4]=proceso.getEstadoProceso();
            miTabla[5]=proceso.getRecursos().size();
            miTabla[6]=proceso.getPlanificador();
            miTabla[7]=proceso.getPrioridad();
            miTabla[8]=proceso.getContabilizacion();
            miTabla[9]=proceso.getAncestro();
            String aux="";
            for(int i=0; i<proceso.getDescendientes().size(); i++){
                if(i==0){
                    aux+=proceso.getDescendientes().get(i).toString();
                }else{
                    aux+='-'+proceso.getDescendientes().get(i).toString();
                }
            }
            miTabla[10]=aux;
            modelo.addRow(miTabla);
            ventana.tblPCB_FCFS.setModel(modelo);
        }else if(tipo==2){
            ventana.tblPCB_SJF.setBackground(Color.WHITE);
            DefaultTableModel modelo = (DefaultTableModel) ventana.tblPCB_SJF.getModel();
            Object[] miTabla = new Object[11];
            miTabla[0]=proceso.getIdentificador();
            miTabla[1]=proceso.getEstadoCPU();
            miTabla[2]=proceso.getProcesador();
            miTabla[3]=proceso.getMemoria().getKey().toString()+'-'+proceso.getMemoria().getValue().toString();
            miTabla[4]=proceso.getEstadoProceso();
            miTabla[5]=proceso.getRecursos().size();
            miTabla[6]=proceso.getPlanificador();
            miTabla[7]=proceso.getPrioridad();
            miTabla[8]=proceso.getContabilizacion();
            miTabla[9]=proceso.getAncestro();
            String aux="";
            for(int i=0; i<proceso.getDescendientes().size(); i++){
                if(i==0){
                    aux+=proceso.getDescendientes().get(i).toString();
                }else{
                    aux+='-'+proceso.getDescendientes().get(i).toString();
                }
            }
            miTabla[10]=aux;
            modelo.addRow(miTabla);
            ventana.tblPCB_SJF.setModel(modelo);
        }else{
            ventana.tblPCB_RR.setBackground(Color.WHITE);
            DefaultTableModel modelo = (DefaultTableModel) ventana.tblPCB_RR.getModel();
            Object[] miTabla = new Object[11];
            miTabla[0]=proceso.getIdentificador();
            miTabla[1]=proceso.getEstadoCPU();
            miTabla[2]=proceso.getProcesador();
            miTabla[3]=proceso.getMemoria().getKey().toString()+'-'+proceso.getMemoria().getValue().toString();
            miTabla[4]=proceso.getEstadoProceso();
            miTabla[5]=proceso.getRecursos().size();
            miTabla[6]=proceso.getPlanificador();
            miTabla[7]=proceso.getPrioridad();
            miTabla[8]=proceso.getContabilizacion();
            miTabla[9]=proceso.getAncestro();
            String aux="";
            for(int i=0; i<proceso.getDescendientes().size(); i++){
                if(i==0){
                    aux+=proceso.getDescendientes().get(i).toString();
                }else{
                    aux+='-'+proceso.getDescendientes().get(i).toString();
                }
            }
            miTabla[10]=aux;
            modelo.addRow(miTabla);
            ventana.tblPCB_RR.setModel(modelo);
        }
    }
    
    public static void ActualizaEstadoPCB(Ventana_No_Expropiativo ventana, int pos, int tipo, String estado){
        if(tipo==1){
            ventana.tblPCB_FCFS.setValueAt(estado,pos-1,4);
        }else if(tipo==2){
            ventana.tblPCB_SJF.setValueAt(estado,pos-1,4);
        }else{
            ventana.tblPCB_RR.setValueAt(estado,pos-1,4);
        }
    }
    
    public static void ActualizaDescendientesPCB(Ventana_No_Expropiativo ventana, int pos, int tipo, int descendiente){
        String aux="";
        if(tipo==1){
            aux=ventana.tblPCB_FCFS.getValueAt(pos-1,10).toString();
            aux+='-'+String.valueOf(descendiente);
            ventana.tblPCB_FCFS.setValueAt(aux, pos-1, 10);
        }else if(tipo==2){
            aux=ventana.tblPCB_SJF.getValueAt(pos-1,10).toString();
            aux+='-'+String.valueOf(descendiente);
            ventana.tblPCB_SJF.setValueAt(aux, pos-1, 10);
        }else{
            aux=ventana.tblPCB_RR.getValueAt(pos-1,10).toString();
            aux+='-'+String.valueOf(descendiente);
            ventana.tblPCB_RR.setValueAt(aux, pos-1, 10);
        }
    }
    
    public static void ActualizaEstadisticaINI(Estadisticas estadistica, int pos, int tipo, int ini){
        if(tipo==1){
            estadistica.tblEstadisticaFCFS.setValueAt(String.valueOf(ini),pos-1,2);
        }else if(tipo==2){
            estadistica.tblEstadisticaSJF.setValueAt(String.valueOf(ini),pos-1,2);
        }else{
            estadistica.tblEstadisticaRR.setValueAt(String.valueOf(ini),pos-1,2);
        }
    }
    
    public static void ActualizaEstadisticaFIN(Estadisticas estadistica, int pos, int tipo, int fin){
        if(tipo==1){
            estadistica.tblEstadisticaFCFS.setValueAt(String.valueOf(fin),pos-1,3);
        }else if(tipo==2){
            estadistica.tblEstadisticaSJF.setValueAt(String.valueOf(fin),pos-1,3);
        }else{
            estadistica.tblEstadisticaRR.setValueAt(String.valueOf(fin),pos-1,3);
        }
    }
}
