
package Ventana;

import DAO.daoPCB_NoExp;
import DAO.daoProceso;
import DTO.PairFCFS;
import DTO.PairRR;
import DTO.PairSJF;
import DTO.PairTiempo;
import DTO.dtoProceso;
import org.jfree.chart.JFreeChart;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 *
 * @author Juan
 */
public class Ventana_No_Expropiativo extends javax.swing.JFrame implements KeyListener, MouseListener, MouseMotionListener {

    /**
     * Creates new form Ventana_Principal
     */
    public static double contadorBloqueados_FCFS = 0;
    public static JFreeChart barChart_FCFS;
    public static JFreeChart barChart_SJF;
    public static JFreeChart barChart_RR;
    public static int Contador = 1; //numero total de procesos ingresados
    public static int Tamano;
    public static int BurstTime;
    public static int Quantum = -1;//Carga el quantum en ejecución
    //interrupciones
    public static int ContadorInterrupciones_FCFS = 0;
    public static int ContadorInterrupciones_SJF = 0;
    public static int ContadorInterrupciones_RR = 0;
    public static boolean primera = true;//para interrupciones y procesos
    private static double contadorTerminados_FCFS;
    private static double contadorListos_FCFS;
    private static double contadorEjecutando_FCFS;
    private static double contadorTerminados_SJF;
    private static int contadorListos_SJF;
    private static int contadorEjecutando_SJF;
    private static double contadorBloqueados_SJF;
    private static double contadorTerminados_RR;
    private static int contadorEjecutando_RR;
    private static double contadorBloqueados_RR;
    private static int contadorListos_RR;

    public Thread principal = new Thread(new Hilo());

    public Ventana_No_Expropiativo() {
        initComponents();

        //color de fondo y letra de los procesos
        tblFCFS.setBackground(Color.white);
        tblFCFS.setForeground(Color.blue);
        tblSJF.setBackground(Color.white);
        tblSJF.setForeground(Color.blue);
        tblRR.setBackground(Color.white);
        tblRR.setForeground(Color.blue);

        System.out.println("run");
        double[][] data = {{0}, {0}, {0}, {0}};
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Estado", "", data);

        barChart_FCFS = ChartFactory.createBarChart("", "Proceso", "Progreso", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        barChart_SJF = ChartFactory.createBarChart("", "Proceso", "Progreso", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        barChart_RR = ChartFactory.createBarChart("", "Proceso", "Progreso", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        refrescarTablaContadores(1);
        refrescarTablaContadores(2);
        refrescarTablaContadores(3);
        //Interrupciones de I/O
        /*txtTamano.addKeyListener(this);
        txtBurstTime.addKeyListener(this);
        btnAnadirProcesoUsuario.addMouseListener(this);
        txtTamano.addMouseListener(this);
        txtBurstTime.addMouseListener(this);
        btnAnadirProcesoUsuario.addMouseListener(this);
        addMouseMotionListener(this);
        addMouseMotionListener(this);*/
    }

    public Queue<PairFCFS> FCFS = new LinkedList();
    public PriorityQueue<PairSJF> SJF = new PriorityQueue<>();
    public ArrayList<PairRR> RR = new ArrayList<PairRR>();
    public ArrayList<dtoProceso> ProcesosFCFS = new ArrayList<dtoProceso>();
    public ArrayList<dtoProceso> ProcesosSJF = new ArrayList<dtoProceso>();
    public ArrayList<dtoProceso> ProcesosRR = new ArrayList<dtoProceso>();
    public ArrayList<PairTiempo> DuracionFCFS = new ArrayList<PairTiempo>();
    public ArrayList<PairTiempo> DuracionSJF = new ArrayList<PairTiempo>();
    public ArrayList<PairTiempo> DuracionRR = new ArrayList<PairTiempo>();
    public int cntFCFS = 1;
    public int cntSJF = 1;
    public int cntRR = 1;
    public int IdProcesoFCFS = 1;
    public int IdProcesoSJF = 1;
    public int IdProcesoRR = 1;
    public int TimeTot = 0;
    public PairRR UltimoRR;
    public Estadisticas VentanaEstadisticas = new Estadisticas();
    public int T_INI_FCFS = 0;
    public int cnt_INI_FCFS = 0;
    public int T_FIN_FCFS = 0;
    public int cnt_FIN_FCFS = 0;
    public int T_INI_SJF = 0;
    public int cnt_INI_SJF = 0;
    public int T_FIN_SJF = 0;
    public int cnt_FIN_SJF = 0;
    public int T_INI_RR = 0;
    public int cnt_INI_RR = 0;
    public int T_FIN_RR = 0;
    public int cnt_FIN_RR = 0;
    public int BurstFCFS = 0;
    public int BurstSJF = 0;
    public int BurstRR = 0;
    public double RetornoFCFS = 0.0;
    public double RetornoSJF = 0.0;
    public double RetornoRR = 0.0;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBurstTime = new javax.swing.JTextField();
        btnAnadirProcesoUsuario = new javax.swing.JButton();
        txtTamano = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtQuantum = new javax.swing.JTextField();
        btnDefinirQuantum = new javax.swing.JButton();
        btnDefinirQuantum1 = new javax.swing.JButton();
        btnGenerarAleatorio = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCntFCFS = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFCFS = new javax.swing.JTable();
        jLabelNProceso = new javax.swing.JLabel();
        txtIdProcesoFCFS = new javax.swing.JTextField();
        jLabelPorcentaje = new javax.swing.JLabel();
        txtPorcentajeFCFS = new javax.swing.JTextField();
        pgrFCFS = new javax.swing.JProgressBar();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblInterrupciones_FCFS = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPCB_FCFS = new javax.swing.JTable();
        JPanel_FCF = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCntSJF = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSJF = new javax.swing.JTable();
        txtIdProcesoSJF = new javax.swing.JTextField();
        jLabelNProceso1 = new javax.swing.JLabel();
        jLabelPorcentaje2 = new javax.swing.JLabel();
        txtPorcentajeSJF = new javax.swing.JTextField();
        pgrSJF = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblInterrupciones_SJF = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPCB_SJF = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        JPanel_SJF = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCntRR = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRR = new javax.swing.JTable();
        jLabelNProceso2 = new javax.swing.JLabel();
        txtIdProcesoRR = new javax.swing.JTextField();
        pgrRR = new javax.swing.JProgressBar();
        jLabelPorcentaje1 = new javax.swing.JLabel();
        txtPorcentajeRR = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblInterrupciones_RR = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblPCB_RR = new javax.swing.JTable();
        JPanel_RR = new javax.swing.JPanel();
        JPanel_FCFSS = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("PROCESOS");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Nuevo Proceso");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Tamaño (Mb)");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Rafaga");

        btnAnadirProcesoUsuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnadirProcesoUsuario.setText("Añadir Proceso Usuario");
        btnAnadirProcesoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirProcesoUsuarioActionPerformed(evt);
            }
        });

        txtTamano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTamanoActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("Definir Quantum");

        txtQuantum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantumActionPerformed(evt);
            }
        });

        btnDefinirQuantum.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDefinirQuantum.setText("Definir Quantum");
        btnDefinirQuantum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefinirQuantumActionPerformed(evt);
            }
        });

        btnDefinirQuantum1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnDefinirQuantum1.setText("Volver");
        btnDefinirQuantum1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefinirQuantum1ActionPerformed(evt);
            }
        });

        btnGenerarAleatorio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerarAleatorio.setText("Generar ");
        btnGenerarAleatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarAleatorioActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Generar Estadísticas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 204));
        jLabel5.setText("First Come First Served (FCFS)");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Procesos Principales Creados");

        tblFCFS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "Rafaga", "Residuo", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tblFCFS);

        jLabelNProceso.setText("Proceso Actual");

        txtIdProcesoFCFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProcesoFCFSActionPerformed(evt);
            }
        });

        jLabelPorcentaje.setText("Porcentaje Procesado");

        txtPorcentajeFCFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeFCFSActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setText("INTERRUPCIONES");

        tblInterrupciones_FCFS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Interrupcion", "Proceso", "Tipo", "Duración", "Restante", "Estado"
            }
        ));
        jScrollPane8.setViewportView(tblInterrupciones_FCFS);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 204));
        jLabel15.setText("PCB's");

        tblPCB_FCFS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "EstadoCPU", "Procesador", "Memoria", "Estado", "Rescursos", "Planificador", "Prioridad", "Contabilizacion", "Ancestro", "Descendientes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblPCB_FCFS);

        javax.swing.GroupLayout JPanel_FCFLayout = new javax.swing.GroupLayout(JPanel_FCF);
        JPanel_FCF.setLayout(JPanel_FCFLayout);
        JPanel_FCFLayout.setHorizontalGroup(
            JPanel_FCFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
        );
        JPanel_FCFLayout.setVerticalGroup(
            JPanel_FCFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCntFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel15)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(pgrFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabelNProceso)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtIdProcesoFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabelPorcentaje)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPorcentajeFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JPanel_FCF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCntFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPorcentajeFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelPorcentaje))
                                .addGap(5, 5, 5))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelNProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdProcesoFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(pgrFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JPanel_FCF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        jTabbedPane1.addTab("FCFS", jPanel1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 204));
        jLabel6.setText("Shortest Job First (SJF)");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Procesos Principales Creados");

        txtCntSJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCntSJFActionPerformed(evt);
            }
        });

        tblSJF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "Rafaga", "Residuo", "Estado"
            }
        ));
        jScrollPane2.setViewportView(tblSJF);

        txtIdProcesoSJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProcesoSJFActionPerformed(evt);
            }
        });

        jLabelNProceso1.setText("Proceso Actual");

        jLabelPorcentaje2.setText("Porcentaje Procesado");

        txtPorcentajeSJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeSJFActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setText("INTERRUPCIONES");

        tblInterrupciones_SJF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Interrupcion", "Proceso", "Tipo", "Duración", "Restante", "Estado"
            }
        ));
        jScrollPane7.setViewportView(tblInterrupciones_SJF);

        tblPCB_SJF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "EstadoCPU", "Procesador", "Memoria", "Estado", "Rescursos", "Planificador", "Prioridad", "Contabilizacion", "Ancestro", "Descendientes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblPCB_SJF);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 204));
        jLabel16.setText("PCB's");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 50, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 78, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanel_SJFLayout = new javax.swing.GroupLayout(JPanel_SJF);
        JPanel_SJF.setLayout(JPanel_SJFLayout);
        JPanel_SJFLayout.setHorizontalGroup(
            JPanel_SJFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 854, Short.MAX_VALUE)
        );
        JPanel_SJFLayout.setVerticalGroup(
            JPanel_SJFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(236, 236, 236))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pgrSJF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelNProceso1)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdProcesoSJF, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPorcentaje2)
                                .addGap(18, 18, 18)
                                .addComponent(txtPorcentajeSJF, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(197, 197, 197)
                            .addComponent(txtCntSJF, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JPanel_SJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtCntSJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNProceso1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdProcesoSJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPorcentaje2)
                            .addComponent(txtPorcentajeSJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(pgrSJF, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(163, 163, 163))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JPanel_SJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SJF", jPanel2);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 204));
        jLabel7.setText("Round Robin (RR)");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Procesos Principales Creados");

        tblRR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "Rafaga", "Residuo", "Estado"
            }
        ));
        jScrollPane3.setViewportView(tblRR);

        jLabelNProceso2.setText("Proceso Actual");

        txtIdProcesoRR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProcesoRRActionPerformed(evt);
            }
        });

        jLabelPorcentaje1.setText("Porcentaje Procesado");

        txtPorcentajeRR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeRRActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("INTERRUPCIONES");

        tblInterrupciones_RR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Interrupcion", "Proceso", "Tipo", "Duración", "Restante", "Estado"
            }
        ));
        jScrollPane4.setViewportView(tblInterrupciones_RR);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 204));
        jLabel17.setText("PCB's");

        tblPCB_RR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Proceso", "EstadoCPU", "Procesador", "Memoria", "Estado", "Rescursos", "Planificador", "Prioridad", "Contabilizacion", "Ancestro", "Descendientes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tblPCB_RR);

        javax.swing.GroupLayout JPanel_RRLayout = new javax.swing.GroupLayout(JPanel_RR);
        JPanel_RR.setLayout(JPanel_RRLayout);
        JPanel_RRLayout.setHorizontalGroup(
            JPanel_RRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        JPanel_RRLayout.setVerticalGroup(
            JPanel_RRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 232, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pgrRR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabelNProceso2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtIdProcesoRR, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(22, 22, 22)
                                    .addComponent(jLabelPorcentaje1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPorcentajeRR, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9)
                            .addComponent(JPanel_RR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 381, Short.MAX_VALUE)
                        .addComponent(txtCntRR, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel17)
                        .addGap(843, 843, 843))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel11)))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(904, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCntRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17)))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelNProceso2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdProcesoRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPorcentajeRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabelPorcentaje1))
                        .addGap(5, 5, 5)
                        .addComponent(pgrRR, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(JPanel_RR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("RR", jPanel3);

        javax.swing.GroupLayout JPanel_FCFSSLayout = new javax.swing.GroupLayout(JPanel_FCFSS);
        JPanel_FCFSS.setLayout(JPanel_FCFSSLayout);
        JPanel_FCFSSLayout.setHorizontalGroup(
            JPanel_FCFSSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        JPanel_FCFSSLayout.setVerticalGroup(
            JPanel_FCFSSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(548, 548, 548)
                        .addComponent(btnDefinirQuantum1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(34, 34, 34)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTamano, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(48, 48, 48)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtBurstTime, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(btnAnadirProcesoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(btnDefinirQuantum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnGenerarAleatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(JPanel_FCFSS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDefinirQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBurstTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnadirProcesoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerarAleatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDefinirQuantum1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanel_FCFSS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdProcesoFCFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProcesoFCFSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcesoFCFSActionPerformed

    private void txtPorcentajeFCFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeFCFSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeFCFSActionPerformed

    private void txtIdProcesoSJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProcesoSJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcesoSJFActionPerformed

    private void txtPorcentajeSJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeSJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeSJFActionPerformed

    private void txtIdProcesoRRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProcesoRRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcesoRRActionPerformed

    private void txtPorcentajeRRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeRRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeRRActionPerformed

    private void btnAnadirProcesoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirProcesoUsuarioActionPerformed
        // TODO add your handling code here:
        Tamano = Integer.parseInt(txtTamano.getText()); // recoge tamaño
        BurstTime = Integer.parseInt(txtBurstTime.getText()); // recoge burst time
        DefaultTableModel NuevaTabla1 = (DefaultTableModel) tblFCFS.getModel();
        DefaultTableModel NuevaTabla2 = (DefaultTableModel) tblSJF.getModel();
        DefaultTableModel NuevaTabla3 = (DefaultTableModel) tblRR.getModel();

        if (Quantum > 0) {
            // Crea el objeto Proceso
            dtoProceso proceso = new dtoProceso();
            daoPCB_NoExp daopcb = new daoPCB_NoExp();
            if (Contador % 3 == 1) {
                proceso.setIdentificador(IdProcesoFCFS);
                txtCntFCFS.setText(String.valueOf(cntFCFS));
                IngresaEstadistica(IdProcesoFCFS, BurstTime, 1);
                IdProcesoFCFS++;
                cntFCFS++;
            } else if (Contador % 3 == 2) {
                proceso.setIdentificador(IdProcesoSJF);
                txtCntSJF.setText(String.valueOf(cntSJF));
                IngresaEstadistica(IdProcesoSJF, BurstTime, 2);
                IdProcesoSJF++;
                cntSJF++;
            } else {
                proceso.setIdentificador(IdProcesoRR);
                txtCntRR.setText(String.valueOf(cntRR));
                IngresaEstadistica(IdProcesoRR, BurstTime, 3);
                IdProcesoRR++;
                cntRR++;
            }
            proceso.setEstadoCPU(1);
            proceso.setProcesador(1);
            proceso.setMemoria(new Pair<>(0, Tamano));
            proceso.setEstadoProceso("Nuevo");
            proceso.setRecursos(new ArrayList<String>());
            proceso.setPlanificador("Largo");
            proceso.setPrioridad(0);
            proceso.setContabilizacion(0);
            proceso.setAncestro(0);
            proceso.setDescendientes(new ArrayList<Integer>());

            String idNueva = proceso.getAncestro().toString() + '-' + proceso.getIdentificador().toString();
            if (Contador % 3 == 1) {
                BurstFCFS += BurstTime;
                ProcesosFCFS.add(proceso);
                daopcb.CargarPCB(this, 1, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 1);
                
                contarDatos(1);
                        refrescarTablaContadores(1);
                FCFS.add(new PairFCFS(BurstTime, proceso.getIdentificador(), 0));
                DuracionFCFS.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla1, idNueva, BurstTime);
                tblFCFS.setModel(NuevaTabla1);
            } else if (Contador % 3 == 2) {
                BurstSJF += BurstTime;
                ProcesosSJF.add(proceso);
                daopcb.CargarPCB(this, 2, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 2);
                
                contarDatos(2);
                        refrescarTablaContadores(2);
                SJF.add(new PairSJF(BurstTime, 0, proceso.getIdentificador()));
                DuracionSJF.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla2, idNueva, BurstTime);
                tblSJF.setModel(NuevaTabla2);
            } else {
                BurstRR += BurstTime;
                if (RR.size() == 0) {
                    UltimoRR = new PairRR(BurstTime, proceso.getIdentificador(), BurstTime);
                }
                ProcesosRR.add(proceso);
                daopcb.CargarPCB(this, 3, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 3);
                
                contarDatos(3);
                        refrescarTablaContadores(3);
                RR.add(new PairRR(BurstTime, proceso.getIdentificador(), BurstTime));
                DuracionRR.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla3, idNueva, BurstTime);
                tblRR.setModel(NuevaTabla3);
            }
            if (primera) {
                principal.start();
                primera = !primera;
            }
            txtTamano.setText(null);
            txtBurstTime.setText(null);
            Contador++;
        }
    }//GEN-LAST:event_btnAnadirProcesoUsuarioActionPerformed

    private void txtTamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTamanoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTamanoActionPerformed

    private void txtCntSJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCntSJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCntSJFActionPerformed

    private void btnDefinirQuantumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefinirQuantumActionPerformed
        // TODO add your handling code here:
        Quantum = Integer.parseInt(txtQuantum.getText()); // recoge quamtum
    }//GEN-LAST:event_btnDefinirQuantumActionPerformed

    private void btnDefinirQuantum1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefinirQuantum1ActionPerformed
        // TODO add your handling code here:
    
    }//GEN-LAST:event_btnDefinirQuantum1ActionPerformed

    private void txtQuantumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantumActionPerformed

    private void btnGenerarAleatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarAleatorioActionPerformed
        // TODO add your handling code here:
        DefaultTableModel NuevaTabla1 = (DefaultTableModel) tblFCFS.getModel();
        DefaultTableModel NuevaTabla2 = (DefaultTableModel) tblSJF.getModel();
        DefaultTableModel NuevaTabla3 = (DefaultTableModel) tblRR.getModel();

        if (Quantum > 0) {
            for (int i = 0; i < 10; i++) {
                dtoProceso proceso = new dtoProceso();
                daoPCB_NoExp daopcb = new daoPCB_NoExp();
                Random r = new Random();

                BurstTime = 10 + r.nextInt(190);
                Tamano = 500 + r.nextInt(50);

                proceso.setIdentificador(IdProcesoFCFS);
                txtCntFCFS.setText(String.valueOf(cntFCFS));
                IngresaEstadistica(IdProcesoFCFS, BurstTime, 1);
                IdProcesoFCFS++;
                cntFCFS++;

                proceso.setIdentificador(IdProcesoSJF);
                txtCntSJF.setText(String.valueOf(cntSJF));
                IngresaEstadistica(IdProcesoSJF, BurstTime, 2);
                IdProcesoSJF++;
                cntSJF++;

                proceso.setIdentificador(IdProcesoRR);
                txtCntRR.setText(String.valueOf(cntRR));
                IngresaEstadistica(IdProcesoRR, BurstTime, 3);
                IdProcesoRR++;
                cntRR++;

                proceso.setEstadoCPU(1);
                proceso.setProcesador(1);
                proceso.setMemoria(new Pair<>(0, Tamano));
                proceso.setEstadoProceso("Nuevo");
                proceso.setRecursos(new ArrayList<String>());
                proceso.setPlanificador("Largo");
                proceso.setPrioridad(0);
                proceso.setContabilizacion(0);
                proceso.setAncestro(0);
                proceso.setDescendientes(new ArrayList<Integer>());

                String idNueva = proceso.getAncestro().toString() + '-' + proceso.getIdentificador().toString();

                BurstFCFS += BurstTime;
                ProcesosFCFS.add(proceso);
                daopcb.CargarPCB(this, 1, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 1);
                
                contarDatos(1);
                        refrescarTablaContadores(1);
                FCFS.add(new PairFCFS(BurstTime, proceso.getIdentificador(), 0));
                DuracionFCFS.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla1, idNueva, BurstTime);
                tblFCFS.setModel(NuevaTabla1);

                BurstSJF += BurstTime;
                ProcesosSJF.add(proceso);
                daopcb.CargarPCB(this, 2, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 2);
                
                contarDatos(2);
                        refrescarTablaContadores(2);
                SJF.add(new PairSJF(BurstTime, 0, proceso.getIdentificador()));
                DuracionSJF.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla2, idNueva, BurstTime);
                tblSJF.setModel(NuevaTabla2);

                BurstRR += BurstTime;
                if (RR.size() == 0) {
                    UltimoRR = new PairRR(BurstTime, proceso.getIdentificador(), BurstTime);
                }
                ProcesosRR.add(proceso);
                daopcb.CargarPCB(this, 3, proceso, -1, -1);
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 3);
                
                contarDatos(3);
                        refrescarTablaContadores(3);
                RR.add(new PairRR(BurstTime, proceso.getIdentificador(), BurstTime));
                DuracionRR.add(new PairTiempo(null, null));
                daoProceso.AgregarProceso(NuevaTabla3, idNueva, BurstTime);
                tblRR.setModel(NuevaTabla3);

                txtTamano.setText(null);
                txtBurstTime.setText(null);
                Contador += 3;
                if (primera) {
                    principal.start();
                    primera = !primera;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Escriba el valor del Quantum");
        }
    }//GEN-LAST:event_btnGenerarAleatorioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        VentanaEstadisticas.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana_No_Expropiativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana_No_Expropiativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana_No_Expropiativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana_No_Expropiativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                               
                new Ventana_No_Expropiativo().setVisible(true);
            }
        });
    }

    private class Hilo implements Runnable { //Objeto de tipo Hilo con extension ejecutable

        @Override
        public void run() {
            while (true) {
                MuestraUsoCPU();
                System.out.println("-> " + FCFS.size() + " " + SJF.size() + " " + RR.size());
                if (!FCFS.isEmpty()) {
                    PairFCFS ac = FCFS.poll();
                    dtoProceso procesoActual = new dtoProceso();
                    procesoActual = ProcesosFCFS.get(ac.getIdentificador().intValue() - 1);
                    String idNuevo = procesoActual.getAncestro().toString() + '-' + procesoActual.getIdentificador().toString();
                    int PosicionTabla = BuscaPosicionTabla(idNuevo, 1);
                    int tiempo = ac.getBurstTime().intValue();
                    AsignaInicio(ac.getIdentificador().intValue(), 1);
                    for (int i = 1; i <= tiempo; i++) {
                        TimeTot++;
                        CambiaEstado(procesoActual.getIdentificador().intValue(), "Ejecutando", 1);
                        
                        contarDatos(1);
                        refrescarTablaContadores(1);
                        MostrarProgreso(i, tiempo, ac.getIdentificador().intValue(), 1);
                        tblFCFS.setValueAt(procesoActual.getEstadoProceso(), PosicionTabla, 3);
                        tblFCFS.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                        Dormir();
                        GeneraInterrupcion(PosicionTabla, ac.getIdentificador().intValue(), 1);
                        GeneraHijo(PosicionTabla, ac.getIdentificador().intValue(), 1);
                    }
                    CambiaEstado(procesoActual.getIdentificador().intValue(), "Terminado", 1);
                    
                    contarDatos(1);
                        refrescarTablaContadores(1);
                    Borrar(PosicionTabla, 1);
                    AsignaFin(ac.getIdentificador().intValue(), 1);

                    /*PairTiempo aux = new PairTiempo();
                    aux=DuracionFCFS.get(ac.getIdentificador().intValue()-1);
                    System.out.println("Tipo 1 para "+ac.getIdentificador()+" -> "+aux.getInicio()+" "+aux.getFin());*/
                }
                if (!SJF.isEmpty()) {
                    PairSJF ac = SJF.poll();
                    dtoProceso procesoActual = new dtoProceso();
                    procesoActual = ProcesosSJF.get(ac.getIdentificador().intValue() - 1);
                    /*System.out.println("La posicion que debe buscar "+ac.getIdentificador()+" "+ac.getBurstTime());
                    System.out.println("La posicion q me genera "+procesoActual.getIdentificador());*/
                    String idNuevo = procesoActual.getAncestro().toString() + '-' + procesoActual.getIdentificador().toString();
                    int PosicionTabla = BuscaPosicionTabla(idNuevo, 2);
                    int tiempo = ac.getBurstTime().intValue();
                    AsignaInicio(ac.getIdentificador().intValue(), 2);
                    for (int i = 1; i <= tiempo; i++) {
                        TimeTot++;
                        CambiaEstado(procesoActual.getIdentificador().intValue(), "Ejecutando", 2);
                        
                        contarDatos(2);
                        refrescarTablaContadores(2);
                        MostrarProgreso(i, tiempo, ac.getIdentificador().intValue(), 2);
                        tblSJF.setValueAt(procesoActual.getEstadoProceso(), PosicionTabla, 3);
                        tblSJF.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                        Dormir();
                        GeneraInterrupcion(PosicionTabla, ac.getIdentificador().intValue(), 2);
                        GeneraHijo(PosicionTabla, ac.getIdentificador().intValue(), 2);
                    }
                    CambiaEstado(procesoActual.getIdentificador().intValue(), "Terminado", 2);
                    
                    contarDatos(2);
                        refrescarTablaContadores(2);
                    Borrar(PosicionTabla, 2);
                    AsignaFin(ac.getIdentificador().intValue(), 2);

                    /*PairTiempo aux = new PairTiempo();
                    aux=DuracionSJF.get(ac.getIdentificador().intValue()-1);
                    System.out.println("Tipo 2 para "+ac.getIdentificador()+" -> "+aux.getInicio()+" "+aux.getFin());*/
                }
                int sz = RR.size();
                if (sz != 0) {
                    dtoProceso procesoActual = new dtoProceso();
                    procesoActual = ProcesosRR.get(UltimoRR.getIdentificador().intValue() - 1);
                    String idNuevo = procesoActual.getAncestro().toString() + '-' + procesoActual.getIdentificador().toString();
                    int PosicionTabla = BuscaPosicionTabla(idNuevo, 3);
                    int tiempo = UltimoRR.getResiduo().intValue();
                    int tiempoReducir;
                    boolean termina = false;
                    if (UltimoRR.getBurstTime().equals(UltimoRR.getResiduo())) {
                        AsignaInicio(UltimoRR.getIdentificador().intValue(), 3);
                    }
                    if (tiempo > Quantum) {
                        tiempoReducir = Quantum;
                    } else {
                        termina = true;
                        tiempoReducir = tiempo;
                    }
                    for (int i = 1; i <= tiempoReducir; i++) {
                        TimeTot++;
                        CambiaEstado(procesoActual.getIdentificador().intValue(), "Ejecutando", 3);
                        
                        contarDatos(3);
                        refrescarTablaContadores(3);
                        MostrarProgreso(UltimoRR.getBurstTime().intValue() - (UltimoRR.getResiduo().intValue() - i), UltimoRR.getBurstTime().intValue(), UltimoRR.getIdentificador().intValue(), 3);
                        tblRR.setValueAt(procesoActual.getEstadoProceso(), PosicionTabla, 3);
                        tblRR.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                        Dormir();
                        GeneraInterrupcion(PosicionTabla, UltimoRR.getIdentificador().intValue(), 3);
                        GeneraHijo(PosicionTabla, UltimoRR.getIdentificador().intValue(), 3);
                    }
                    if (termina) {
                        CambiaEstado(procesoActual.getIdentificador().intValue(), "Terminado", 3);
                        
                        contarDatos(3);
                        refrescarTablaContadores(3);
                        AsignaFin(UltimoRR.getIdentificador().intValue(), 3);

                        /*PairTiempo aux = new PairTiempo();
                        aux=DuracionRR.get(UltimoRR.getIdentificador().intValue()-1);
                        System.out.println("Tipo 3 para "+UltimoRR.getIdentificador()+" -> "+aux.getInicio()+" "+aux.getFin());*/
                        RR.remove(PosicionTabla);
                        sz = RR.size();
                        if (sz != 0) {
                            UltimoRR = RR.get(PosicionTabla % sz);
                        }
                        Borrar(PosicionTabla, 3);
                    } else {
                        CambiaEstado(procesoActual.getIdentificador().intValue(), "Listo", 3);
                        
                        contarDatos(3);
                        refrescarTablaContadores(3);
                        RR.set(PosicionTabla, new PairRR(UltimoRR.getBurstTime(), UltimoRR.getIdentificador(), tiempo - tiempoReducir));
                        tblRR.setValueAt("Listo", PosicionTabla, 3);
                        UltimoRR = RR.get((PosicionTabla + 1) % sz);
                    }
                }
                /*if(FCFS.size()==0 && SJF.size()==0 && RR.size()==0){
                    System.out.println("Termina");
                    primera=true;
                }*/
            }
        }
    }

    public void Borrar(int pos, int tipo) {
        if (tipo == 1) {
            DefaultTableModel dtm = (DefaultTableModel) tblFCFS.getModel();
            dtm.removeRow(pos);
            tblFCFS.setModel(dtm);
        } else if (tipo == 2) {
            DefaultTableModel dtm = (DefaultTableModel) tblSJF.getModel();
            dtm.removeRow(pos);
            tblSJF.setModel(dtm);
        } else {
            DefaultTableModel dtm = (DefaultTableModel) tblRR.getModel();
            dtm.removeRow(pos);
            tblRR.setModel(dtm);
        }
    }

    public static void contarDatos(int tipo) {
        JTable table;
        if(tipo == 1){
            table = tblPCB_FCFS;
        }else if(tipo == 2){
            table = tblPCB_SJF;
        }else{
            table = tblPCB_RR;
        }
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();

        double terminados = 0;
        double listos = 0;
        double bloqueados = 0;
        double ejecutados = 0;

        for (int i = 0; i < dtm.getRowCount(); i++) {
            try {
                if (dtm.getValueAt(i, 4) == "Listo") {
                    listos++;
                } else if (dtm.getValueAt(i, 4) == "Terminado") {
                    terminados++;
                } else if (dtm.getValueAt(i, 4) == "Ejecutando") {
                    ejecutados++;
                } else {
                    bloqueados++;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        switch (tipo) {
            case 1:
                contadorTerminados_FCFS = terminados;
                contadorListos_FCFS = listos;
                contadorEjecutando_FCFS = ejecutados;
                contadorBloqueados_FCFS = bloqueados;
                break;
            case 2:
                contadorTerminados_SJF = terminados;
                contadorListos_SJF = (int) listos;
                contadorEjecutando_SJF = (int) ejecutados;
                contadorBloqueados_SJF = bloqueados;
                break;
            default:
                contadorTerminados_RR = terminados;
                contadorListos_RR = (int) listos;
                contadorEjecutando_RR = (int) ejecutados;
                contadorBloqueados_RR = bloqueados;
                break;
        }

    }

    public static void refrescarTablaContadores(int tipo) {
        if (tipo == 1) {
            double[][] data = {{contadorEjecutando_FCFS}, {contadorListos_FCFS}, {contadorTerminados_FCFS}, {contadorBloqueados_FCFS}};
          
            
            Comparable comparable1 = (Comparable)("Ejecutando");
            Comparable comparable2 = (Comparable)("Listo");
            Comparable comparable3 = (Comparable)("Terminado");
            Comparable  comparable4 = (Comparable)("Bloqueado");
            Comparable[] estados= {comparable1,comparable2,comparable3,comparable4};
            Comparable[] prog = {(Comparable) ("Progreso")};
            
            
            CategoryDataset dataset = DatasetUtilities.createCategoryDataset(estados, prog, data);

            CategoryPlot barChartPlot = barChart_FCFS.getCategoryPlot();
            barChartPlot.setDataset(dataset);

            BarRenderer br = (BarRenderer) barChartPlot.getRenderer();
            br.setMaximumBarWidth(.15);
            barChartPlot.getRenderer().setSeriesPaint(0, new Color(0, 255, 0));
            barChartPlot.getRenderer().setSeriesPaint(1, new Color(0, 0, 255));
            barChartPlot.getRenderer().setSeriesPaint(2, new Color(0, 230, 255));

            ChartPanel bar_FCFS = new ChartPanel(barChart_FCFS);
            bar_FCFS.setSize(700, 200);

            JPanel_FCF.add(bar_FCFS);
            JPanel_FCF.setSize(700, 200);
        } else if (tipo == 2) {
            double[][] data = {{contadorEjecutando_SJF}, {contadorListos_SJF}, {contadorTerminados_SJF}, {contadorBloqueados_SJF}};
            CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Estado", "", data);

            CategoryPlot barChartPlot = barChart_SJF.getCategoryPlot();
            barChartPlot.setDataset(dataset);

            BarRenderer br = (BarRenderer) barChartPlot.getRenderer();
            br.setMaximumBarWidth(.15);
            barChartPlot.getRenderer().setSeriesPaint(0, new Color(0, 255, 0));
            barChartPlot.getRenderer().setSeriesPaint(1, new Color(0, 0, 255));
            barChartPlot.getRenderer().setSeriesPaint(2, new Color(0, 230, 255));

            ChartPanel bar_SJF = new ChartPanel(barChart_SJF);
            bar_SJF.setSize(700, 200);

            JPanel_SJF.add(bar_SJF);
            JPanel_SJF.setSize(700, 200);

        } else {
            double[][] data = {{contadorEjecutando_RR}, {contadorListos_RR}, {contadorTerminados_RR}, {contadorBloqueados_RR}};
            CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Estado", "", data);

            CategoryPlot barChartPlot = barChart_RR.getCategoryPlot();
            barChartPlot.setDataset(dataset);

            BarRenderer br = (BarRenderer) barChartPlot.getRenderer();
            br.setMaximumBarWidth(.15);
            barChartPlot.getRenderer().setSeriesPaint(0, new Color(0, 255, 0));
            barChartPlot.getRenderer().setSeriesPaint(1, new Color(0, 0, 255));
            barChartPlot.getRenderer().setSeriesPaint(2, new Color(0, 230, 255));

            ChartPanel bar_RR = new ChartPanel(barChart_RR);
            bar_RR.setSize(700, 200);

            JPanel_RR.add(bar_RR);
            JPanel_RR.setSize(700, 200);

        }
    }

    public void GeneraHijo(int PosicionTabla, int IdProceso, int tipo) {
        Random r = new Random();
        daoPCB_NoExp daopcb = new daoPCB_NoExp();
        if (r.nextInt(100) < 5) {
            ActualizaDescendientes(IdProceso, tipo);
            CambiaEstado(IdProceso, "Bloqueado", tipo);
            
            contarDatos(tipo);
                        refrescarTablaContadores(tipo);
            dtoProceso proceso = new dtoProceso();
            if (tipo == 1) {
                tblFCFS.setValueAt("Bloqueado por Hijo", PosicionTabla, 3);
                proceso.setIdentificador(IdProcesoFCFS);
                IdProcesoFCFS++;
            } else if (tipo == 2) {
                tblSJF.setValueAt("Bloqueado por Hijo", PosicionTabla, 3);
                proceso.setIdentificador(IdProcesoSJF);
                IdProcesoSJF++;
            } else {
                tblRR.setValueAt("Bloqueado por Hijo", PosicionTabla, 3);
                proceso.setIdentificador(IdProcesoRR);
                IdProcesoRR++;
            }
            Tamano = r.nextInt(100) + 1;
            proceso.setEstadoCPU(1);
            proceso.setProcesador(1);
            proceso.setMemoria(new Pair<>(0, Tamano));
            proceso.setEstadoProceso("Nuevo");
            proceso.setRecursos(new ArrayList<String>());
            proceso.setPlanificador("Largo");
            proceso.setPrioridad(0);
            proceso.setContabilizacion(0);
            proceso.setAncestro(IdProceso);
            proceso.setDescendientes(new ArrayList<Integer>());
            if (Quantum > 20) {
                BurstTime = r.nextInt(20) + 1;
            } else {
                BurstTime = r.nextInt(Quantum) + 1;
            }
            if (tipo == 1) {
                IngresaEstadistica(IdProcesoFCFS - 1, BurstTime, 1);
            } else if (tipo == 2) {
                IngresaEstadistica(IdProcesoSJF - 1, BurstTime, 2);
            } else {
                IngresaEstadistica(IdProcesoRR - 1, BurstTime, 3);
            }
            String idNueva = proceso.getAncestro().toString() + '-' + proceso.getIdentificador().toString();
            if (tipo == 1) {
                DefaultTableModel NuevaTabla1 = (DefaultTableModel) tblFCFS.getModel();
                ProcesosFCFS.add(proceso);
                daopcb.CargarPCB(this, tipo, proceso, -1, -1);
                DuracionFCFS.add(new PairTiempo(null, null));
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 1);
                
                contarDatos(1);
                        refrescarTablaContadores(1);
                daoProceso.AgregarProceso(NuevaTabla1, idNueva, BurstTime);
                tblFCFS.setModel(NuevaTabla1);
            } else if (tipo == 2) {
                DefaultTableModel NuevaTabla2 = (DefaultTableModel) tblSJF.getModel();
                ProcesosSJF.add(proceso);
                daopcb.CargarPCB(this, tipo, proceso, -1, -1);
                DuracionSJF.add(new PairTiempo(null, null));
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 2);
                
                contarDatos(2);
                        refrescarTablaContadores(2);
                daoProceso.AgregarProceso(NuevaTabla2, idNueva, BurstTime);
                tblSJF.setModel(NuevaTabla2);
            } else {
                DefaultTableModel NuevaTabla3 = (DefaultTableModel) tblRR.getModel();
                ProcesosRR.add(proceso);
                daopcb.CargarPCB(this, tipo, proceso, -1, -1);
                DuracionRR.add(new PairTiempo(null, null));
                CambiaEstado(proceso.getIdentificador().intValue(), "Listo", 3);
                contarDatos(3);
                        refrescarTablaContadores(3);
                daoProceso.AgregarProceso(NuevaTabla3, idNueva, BurstTime);
                tblRR.setModel(NuevaTabla3);
            }
            AtiendeHijo(proceso, tipo, BurstTime);
        }
    }

    public void AtiendeHijo(dtoProceso procesoHijo, int tipo, int tiempo) {
        int PosicionTabla;
        AsignaInicio(procesoHijo.getIdentificador().intValue(), tipo);
        if (tipo == 1) {
            BurstFCFS += BurstTime;
            String idNuevo = procesoHijo.getAncestro().toString() + '-' + procesoHijo.getIdentificador().toString();
            PosicionTabla = BuscaPosicionTabla(idNuevo, 1);
            for (int i = 1; i <= tiempo; i++) {
                TimeTot++;
                CambiaEstado(procesoHijo.getIdentificador().intValue(), "Ejecutando", tipo);
                contarDatos(tipo);
                        refrescarTablaContadores(tipo);
                MostrarProgreso(i, tiempo, procesoHijo.getIdentificador().intValue(), 1);
                tblFCFS.setValueAt(procesoHijo.getEstadoProceso(), PosicionTabla, 3);
                tblFCFS.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                GeneraInterrupcion(PosicionTabla, procesoHijo.getIdentificador().intValue(), 1);
                Dormir();
            }
        } else if (tipo == 2) {
            BurstSJF += BurstTime;
            String idNuevo = procesoHijo.getAncestro().toString() + '-' + procesoHijo.getIdentificador().toString();
            PosicionTabla = BuscaPosicionTabla(idNuevo, 2);
            for (int i = 1; i <= tiempo; i++) {
                TimeTot++;
                CambiaEstado(procesoHijo.getIdentificador().intValue(), "Ejecutando", tipo);
                
                contarDatos(tipo);
                        refrescarTablaContadores(tipo);
                MostrarProgreso(i, tiempo, procesoHijo.getIdentificador().intValue(), 2);
                tblSJF.setValueAt(procesoHijo.getEstadoProceso(), PosicionTabla, 3);
                tblSJF.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                GeneraInterrupcion(PosicionTabla, procesoHijo.getIdentificador().intValue(), 2);
                Dormir();
            }
        } else {
            BurstRR += BurstTime;
            String idNuevo = procesoHijo.getAncestro().toString() + '-' + procesoHijo.getIdentificador().toString();
            PosicionTabla = BuscaPosicionTabla(idNuevo, 3);
            for (int i = 1; i <= tiempo; i++) {
                TimeTot++;
                CambiaEstado(procesoHijo.getIdentificador().intValue(), "Ejecutando", tipo);
                
                contarDatos(tipo);
                        refrescarTablaContadores(tipo);
                MostrarProgreso(i, tiempo, procesoHijo.getIdentificador().intValue(), 3);
                tblRR.setValueAt(procesoHijo.getEstadoProceso(), PosicionTabla, 3);
                tblRR.setValueAt(String.valueOf(tiempo - i), PosicionTabla, 2);
                GeneraInterrupcion(PosicionTabla, procesoHijo.getIdentificador().intValue(), 3);
                Dormir();
            }
        }
        CambiaEstado(procesoHijo.getIdentificador().intValue(), "Terminado", tipo);
        
        contarDatos(tipo);
                        refrescarTablaContadores(tipo);
        Borrar(PosicionTabla, tipo);
        AsignaFin(procesoHijo.getIdentificador().intValue(), tipo);
    }

    public static int BuscaPosicionTabla(String idNueva, int tipo) {
        if (tipo == 1) {
            for (int i = 0; i < tblFCFS.getRowCount(); i++) {
                if (String.valueOf(tblFCFS.getValueAt(i, 0)).equals(idNueva)) {
                    return i;
                }
            }
            return -1;
        } else if (tipo == 2) {
            for (int i = 0; i < tblSJF.getRowCount(); i++) {
                if (String.valueOf(tblSJF.getValueAt(i, 0)).equals(idNueva)) {
                    return i;
                }
            }
            return -1;
        } else {
            for (int i = 0; i < tblRR.getRowCount(); i++) {
                if (String.valueOf(tblRR.getValueAt(i, 0)).equals(idNueva)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public void Dormir() {
        try {
            Thread.sleep(200); //Dormir sistema
        } catch (InterruptedException ex) {
            Logger.getLogger(Ventana_No_Expropiativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void GeneraInterrupcion(int PosicionTabla, int IdProceso, int tipo) {
        Random r = new Random();
        if (r.nextInt(100) < 5) {
            //existe interrupcion
            CambiaEstado(IdProceso, "Bloqueado", tipo);
            
            contarDatos(tipo);
                        refrescarTablaContadores(tipo);
            if (tipo == 1) {
                tblFCFS.setValueAt("Bloqueado por Interrupción", PosicionTabla, 3);
                int TiempoInterrupcion = r.nextInt(10) + 1;
                IngresarInterrupcion(TiempoInterrupcion, IdProceso, tipo);
                for (int j = 1; j <= TiempoInterrupcion; j++) {
                    tblInterrupciones_FCFS.setValueAt(String.valueOf(TiempoInterrupcion - j), ContadorInterrupciones_FCFS - 1, 4);
                    Dormir();
                }
                tblInterrupciones_FCFS.setValueAt("Terminado", ContadorInterrupciones_FCFS - 1, 5);
            } else if (tipo == 2) {
                tblSJF.setValueAt("Bloqueado por Interrupción", PosicionTabla, 3);
                int TiempoInterrupcion = r.nextInt(10) + 1;
                IngresarInterrupcion(TiempoInterrupcion, IdProceso, tipo);
                for (int j = 1; j <= TiempoInterrupcion; j++) {
                    tblInterrupciones_SJF.setValueAt(String.valueOf(TiempoInterrupcion - j), ContadorInterrupciones_SJF - 1, 4);
                    Dormir();
                }
                tblInterrupciones_SJF.setValueAt("Terminado", ContadorInterrupciones_SJF - 1, 5);
            } else {
                tblRR.setValueAt("Bloqueado por Interrupción", PosicionTabla, 3);
                int TiempoInterrupcion = r.nextInt(10) + 1;
                IngresarInterrupcion(TiempoInterrupcion, IdProceso, tipo);
                for (int j = 1; j <= TiempoInterrupcion; j++) {
                    tblInterrupciones_RR.setValueAt(String.valueOf(TiempoInterrupcion - j), ContadorInterrupciones_RR - 1, 4);
                    Dormir();
                }
                tblInterrupciones_RR.setValueAt("Terminado", ContadorInterrupciones_RR - 1, 5);
            }

        }
    }

    public void IngresarInterrupcion(int tiempoInterrupcion, int IdProceso, int tipo) {
        Object[] miTabla = new Object[6];
        miTabla[1] = String.valueOf(IdProceso);
        if (tipo == 1) {
            ContadorInterrupciones_FCFS++;
            miTabla[0] = ContadorInterrupciones_FCFS;
            miTabla[2] = String.valueOf("FCFS");
        } else if (tipo == 2) {
            ContadorInterrupciones_SJF++;
            miTabla[0] = ContadorInterrupciones_SJF;
            miTabla[2] = String.valueOf("SJF");
        } else {
            ContadorInterrupciones_RR++;
            miTabla[0] = ContadorInterrupciones_RR;
            miTabla[2] = String.valueOf("RR");
        }
        miTabla[3] = String.valueOf(tiempoInterrupcion);
        miTabla[4] = String.valueOf(tiempoInterrupcion);
        miTabla[5] = "Atendiendo";

        if (tipo == 1) {
            DefaultTableModel modelo = (DefaultTableModel) tblInterrupciones_FCFS.getModel();
            modelo.addRow(miTabla);
            tblInterrupciones_FCFS.setModel(modelo);
        } else if (tipo == 2) {
            DefaultTableModel modelo = (DefaultTableModel) tblInterrupciones_SJF.getModel();
            modelo.addRow(miTabla);
            tblInterrupciones_SJF.setModel(modelo);

        } else {
            DefaultTableModel modelo = (DefaultTableModel) tblInterrupciones_RR.getModel();
            modelo.addRow(miTabla);
            tblInterrupciones_RR.setModel(modelo);
        }

    }

    public void ActualizaDescendientes(int IdProceso, int tipo) {
        dtoProceso actualiza = new dtoProceso();
        daoPCB_NoExp daopcb = new daoPCB_NoExp();
        ArrayList<Integer> nuevaLista = new ArrayList<Integer>();
        if (tipo == 1) {
            actualiza = ProcesosFCFS.get(IdProceso - 1);
            nuevaLista = actualiza.getDescendientes();
            nuevaLista.add(IdProcesoFCFS);
            actualiza.setDescendientes(nuevaLista);
            ProcesosFCFS.set(IdProceso - 1, actualiza);
            daopcb.ActualizaDescendientesPCB(this, IdProceso, tipo, IdProcesoFCFS);
        } else if (tipo == 2) {
            actualiza = ProcesosSJF.get(IdProceso - 1);
            nuevaLista = actualiza.getDescendientes();
            nuevaLista.add(IdProcesoSJF);
            actualiza.setDescendientes(nuevaLista);
            ProcesosSJF.set(IdProceso - 1, actualiza);
            daopcb.ActualizaDescendientesPCB(this, IdProceso, tipo, IdProcesoSJF);
        } else {
            actualiza = ProcesosRR.get(IdProceso - 1);
            nuevaLista = actualiza.getDescendientes();
            nuevaLista.add(IdProcesoRR);
            actualiza.setDescendientes(nuevaLista);
            ProcesosRR.set(IdProceso - 1, actualiza);
            daopcb.ActualizaDescendientesPCB(this, IdProceso, tipo, IdProcesoRR);
        }
    }

    public void CambiaEstado(int IdProceso, String estado, int tipo) {
        dtoProceso actualiza = new dtoProceso();
        daoPCB_NoExp daopcb = new daoPCB_NoExp();
        daopcb.ActualizaEstadoPCB(this, IdProceso, tipo, estado);
        if (tipo == 1) {
            actualiza = ProcesosFCFS.get(IdProceso - 1);
            actualiza.setEstadoProceso(estado);
            ProcesosFCFS.set(IdProceso - 1, actualiza);
        } else if (tipo == 2) {
            actualiza = ProcesosSJF.get(IdProceso - 1);
            actualiza.setEstadoProceso(estado);
            ProcesosSJF.set(IdProceso - 1, actualiza);
        } else {
            actualiza = ProcesosRR.get(IdProceso - 1);
            actualiza.setEstadoProceso(estado);
            ProcesosRR.set(IdProceso - 1, actualiza);
        }
    }

    public void MostrarProgreso(int avance, int total, Integer Id, int tipo) {
        int porcentaje = (avance * 100) / total;
        if (tipo == 1) {
            txtIdProcesoFCFS.setText(Id.toString());
            txtPorcentajeFCFS.setText(String.valueOf(porcentaje + "%"));
            pgrFCFS.setValue(porcentaje);
            pgrFCFS.repaint();
        } else if (tipo == 2) {
            txtIdProcesoSJF.setText(Id.toString());
            txtPorcentajeSJF.setText(String.valueOf(porcentaje + "%"));
            pgrSJF.setValue(porcentaje);
            pgrSJF.repaint();
        } else {
            txtIdProcesoRR.setText(Id.toString());
            txtPorcentajeRR.setText(String.valueOf(porcentaje + "%"));
            pgrRR.setValue(porcentaje);
            pgrRR.repaint();
        }
    }

    public void AsignaInicio(int Id, int tipo) {
        PairTiempo par = new PairTiempo();
        daoPCB_NoExp daopcb = new daoPCB_NoExp();
        daopcb.ActualizaEstadisticaINI(VentanaEstadisticas, Id, tipo, TimeTot);
        if (tipo == 1) {
            par = DuracionFCFS.get(Id - 1);
            par.setInicio(TimeTot);
            DuracionFCFS.set(Id - 1, par);
            T_INI_FCFS += TimeTot;
            cnt_INI_FCFS++;
            VentanaEstadisticas.txtEsperaFCFS.setText(String.valueOf((T_INI_FCFS * 1.0) / (cnt_INI_FCFS * 1.0)));
        } else if (tipo == 2) {
            par = DuracionSJF.get(Id - 1);
            par.setInicio(TimeTot);
            DuracionSJF.set(Id - 1, par);
            T_INI_SJF += TimeTot;
            cnt_INI_SJF++;
            VentanaEstadisticas.txtEsperaSJF.setText(String.valueOf((T_INI_SJF * 1.0) / (cnt_INI_SJF * 1.0)));
        } else {
            par = DuracionRR.get(Id - 1);
            par.setInicio(TimeTot);
            DuracionRR.set(Id - 1, par);
            T_INI_RR += TimeTot;
            cnt_INI_RR++;
            VentanaEstadisticas.txtEsperaRR.setText(String.valueOf((T_INI_RR * 1.0) / (cnt_INI_RR * 1.0)));
        }

    }

    public void AsignaFin(int Id, int tipo) {
        PairTiempo par = new PairTiempo();
        daoPCB_NoExp daopcb = new daoPCB_NoExp();
        daopcb.ActualizaEstadisticaFIN(VentanaEstadisticas, Id, tipo, TimeTot);
        if (tipo == 1) {
            int inicio = DuracionFCFS.get(Id - 1).getInicio().intValue();
            par = DuracionFCFS.get(Id - 1);
            par.setFin(TimeTot);
            DuracionFCFS.set(Id - 1, par);
            T_FIN_FCFS += TimeTot;
            cnt_FIN_FCFS++;
            VentanaEstadisticas.txtRetornoFCFS.setText(String.valueOf((T_FIN_FCFS * 1.0) / (cnt_FIN_FCFS * 1.0)));
        } else if (tipo == 2) {
            int inicio = DuracionSJF.get(Id - 1).getInicio().intValue();
            par = DuracionSJF.get(Id - 1);
            par.setFin(TimeTot);
            DuracionSJF.set(Id - 1, par);
            T_FIN_SJF += TimeTot;
            cnt_FIN_SJF++;
            VentanaEstadisticas.txtRetornoSJF.setText(String.valueOf((T_FIN_SJF * 1.0) / (cnt_FIN_SJF * 1.0)));
        } else {
            int inicio = DuracionRR.get(Id - 1).getInicio().intValue();
            par = DuracionRR.get(Id - 1);
            par.setFin(TimeTot);
            DuracionRR.set(Id - 1, par);
            T_FIN_RR += TimeTot;
            cnt_FIN_RR++;
            VentanaEstadisticas.txtRetornoRR.setText(String.valueOf((T_FIN_RR * 1.0) / (cnt_FIN_RR * 1.0)));
        }
    }

    public void IngresaEstadistica(int id, int burst, int tipo) {
        DefaultTableModel modelo1 = (DefaultTableModel) VentanaEstadisticas.tblEstadisticaFCFS.getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) VentanaEstadisticas.tblEstadisticaSJF.getModel();
        DefaultTableModel modelo3 = (DefaultTableModel) VentanaEstadisticas.tblEstadisticaRR.getModel();
        Object[] miTabla = new Object[4];
        miTabla[0] = String.valueOf(id);
        miTabla[1] = String.valueOf(burst);
        miTabla[2] = String.valueOf("-1");
        miTabla[3] = String.valueOf("-1");
        if (tipo == 1) {
            modelo1.addRow(miTabla);
            VentanaEstadisticas.tblEstadisticaFCFS.setModel(modelo1);
        } else if (tipo == 2) {
            modelo2.addRow(miTabla);
            VentanaEstadisticas.tblEstadisticaSJF.setModel(modelo2);
        } else {
            modelo3.addRow(miTabla);
            VentanaEstadisticas.tblEstadisticaRR.setModel(modelo3);
        }
    }

    public void MuestraUsoCPU() {
        VentanaEstadisticas.txtCPUFCFS.setText(String.valueOf((BurstFCFS * 100.0) / ((BurstFCFS + BurstSJF + BurstRR) * 1.0)) + '%');
        VentanaEstadisticas.txtCPUSJF.setText(String.valueOf((BurstSJF * 100.0) / ((BurstFCFS + BurstSJF + BurstRR) * 1.0)) + '%');
        VentanaEstadisticas.txtCPURR.setText(String.valueOf((BurstRR * 100.0) / ((BurstFCFS + BurstSJF + BurstRR) * 1.0)) + '%');
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JPanel JPanel_FCF;
    private javax.swing.JPanel JPanel_FCFSS;
    private static javax.swing.JPanel JPanel_RR;
    private static javax.swing.JPanel JPanel_SJF;
    private javax.swing.JButton btnAnadirProcesoUsuario;
    private javax.swing.JButton btnDefinirQuantum;
    private javax.swing.JButton btnDefinirQuantum1;
    private javax.swing.JButton btnGenerarAleatorio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelNProceso;
    private javax.swing.JLabel jLabelNProceso1;
    private javax.swing.JLabel jLabelNProceso2;
    private javax.swing.JLabel jLabelPorcentaje;
    private javax.swing.JLabel jLabelPorcentaje1;
    private javax.swing.JLabel jLabelPorcentaje2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JProgressBar pgrFCFS;
    private javax.swing.JProgressBar pgrRR;
    private javax.swing.JProgressBar pgrSJF;
    public static javax.swing.JTable tblFCFS;
    public static javax.swing.JTable tblInterrupciones_FCFS;
    public static javax.swing.JTable tblInterrupciones_RR;
    public static javax.swing.JTable tblInterrupciones_SJF;
    public static javax.swing.JTable tblPCB_FCFS;
    public static javax.swing.JTable tblPCB_RR;
    public static javax.swing.JTable tblPCB_SJF;
    public static javax.swing.JTable tblRR;
    public static javax.swing.JTable tblSJF;
    private javax.swing.JTextField txtBurstTime;
    private javax.swing.JTextField txtCntFCFS;
    private javax.swing.JTextField txtCntRR;
    private javax.swing.JTextField txtCntSJF;
    private javax.swing.JTextField txtIdProcesoFCFS;
    private javax.swing.JTextField txtIdProcesoRR;
    private javax.swing.JTextField txtIdProcesoSJF;
    private javax.swing.JTextField txtPorcentajeFCFS;
    private javax.swing.JTextField txtPorcentajeRR;
    private javax.swing.JTextField txtPorcentajeSJF;
    private javax.swing.JTextField txtQuantum;
    private javax.swing.JTextField txtTamano;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
