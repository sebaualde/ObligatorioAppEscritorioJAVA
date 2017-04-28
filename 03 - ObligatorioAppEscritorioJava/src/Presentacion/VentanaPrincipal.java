/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Logica.FabricaLogica;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author S.U.R
 */
public class VentanaPrincipal extends JFrame{
    protected ImageIcon iconFondo;
    protected Image imgFondo; 
    protected JDesktopPane escritorio;
    protected JMenuBar barrasMenus;
    protected JMenu Mantenimiento;
    protected JMenu listados;
    protected JMenu recibos;
    protected JMenu Ayuda;
    
    protected JMenuItem ABMClientes;
    protected JMenuItem GenerarRecibos;
    protected JMenuItem ACSolicitudDeServicios;
    protected JMenuItem ABOperario;
    protected JMenuItem ABGrua;
    protected JMenuItem AAuxilioMecanico;
    protected JMenuItem ARemolque;
    protected JMenuItem ABMVehiculos;
    protected JMenuItem ListadoVehiculosCli;
    protected JMenuItem submListadosSolicitudes;

    protected JMenuItem submRecibosDelMes;
    protected JMenuItem ManualAyuda;
    protected JMenuItem About;

    protected VentanaClientes ventanaClientes;
    protected VentanaVehiculos ventanaVehiculos;
    protected VentanaSolicitudes ventanaSolicitudes;
    protected VentanaOperarios ventanaOperarios;
    protected VentanaGruas ventanaGruas;
    protected VentanaAuxiliosMecanicos ventanaAuxiliosMecanicos;
    protected VentanaRemolques ventanaRemolques;
    protected VentanaListadoSolicitudes ventanaListadoSolicitudes;
    protected VentanaVehiculosDeCliente ventanaVehiculosDeCliente;
    protected VentanaAbout ventanaAbout;
    protected RecibosDelMes ventanaRecibosDelMes;
    
    public ArrayList<Double> Tarifas = new ArrayList<>();

    public VentanaPrincipal()
    {   
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {  
        try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception ex) {
            System.out.println("¡Error! No se pudo cambiar el look and feel.");
        }
        SwingUtilities.updateComponentTreeUI(this);
        
        setTitle("Ventana Principal");
        
        //creacion de imagen de fondo para la ventana principal
        iconFondo = new ImageIcon(getClass().getResource("/Recursos/fondo.png"));
        imgFondo = iconFondo.getImage();
        escritorio = new JDesktopPane(){
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                g2d.drawImage(imgFondo, 0, 0, getSize().width, getSize().height, this);
            }
        };
        setContentPane(escritorio);
        
        barrasMenus = new JMenuBar();
        setJMenuBar(barrasMenus);
        
        //------------------MANTENIMIENTO--------------------------------------------------------
        
        Mantenimiento = new JMenu("Mantenimiento");
        Mantenimiento.setMnemonic('m');
        barrasMenus.add(Mantenimiento);

        //-----------DARÍO------------
        ABMClientes = new JMenuItem("ABM-Clientes");
        ABMClientes.setMnemonic('c');
        ABMClientes.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
        ABMClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                manejarClicABMCli();
        }
        });
        Mantenimiento.add(ABMClientes);
   
        ABMVehiculos = new JMenuItem("ABM-Vehículos");
        ABMVehiculos.setMnemonic('v');
        ABMVehiculos.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        ABMVehiculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                manejarClicABMVeh();
        }
        });
        Mantenimiento.add(ABMVehiculos);
   
        //------------SEBA-----------
        ACSolicitudDeServicios = new JMenuItem("AC-Solicitud Servicios");
        ACSolicitudDeServicios.setMnemonic('s');
        ACSolicitudDeServicios.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
        ACSolicitudDeServicios.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicABSolicitudes(e);
            }
        });
        Mantenimiento.add(ACSolicitudDeServicios);
        
        ABOperario = new JMenuItem("AB-Operarios");
        ABOperario.setMnemonic('o');
        ABOperario.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
        ABOperario.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicABOperario(e);
            }
        });
        Mantenimiento.add(ABOperario);

        ABGrua = new JMenuItem("AB-Grúas");
        ABGrua.setMnemonic('g');
        ABGrua.setAccelerator(KeyStroke.getKeyStroke('G', InputEvent.CTRL_MASK));
        ABGrua.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicABGruas(e);
            }
        });
        Mantenimiento.add(ABGrua);
        
        //------------MARCE-----------
        AAuxilioMecanico = new JMenuItem("A-Auxilios Mecanicos");
        AAuxilioMecanico.setMnemonic('m');
        AAuxilioMecanico.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK));
        AAuxilioMecanico.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicAuxilioMecanico(e);
            }
        });
        Mantenimiento.add(AAuxilioMecanico);

        ARemolque = new JMenuItem("A-Remolques");
        ARemolque.setMnemonic('r');
        ARemolque.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
        ARemolque.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicARemolque(e);
            }
        });
        Mantenimiento.add(ARemolque);
        
        //------------------LISTADOS--------------------------------------------------------
        listados = new JMenu("Listados");
        listados.setMnemonic('l');
        barrasMenus.add(listados);
        
        submListadosSolicitudes = new JMenuItem("Listados de Solicitudes");
        submListadosSolicitudes.setMnemonic('s');
        submListadosSolicitudes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                ManejadorClicListadoSolicitudes(ae);
            }
        });
        listados.add(submListadosSolicitudes);
        
        ListadoVehiculosCli = new JMenuItem("Vehículos del cliente");
        ListadoVehiculosCli .setMnemonic('v');
        ListadoVehiculosCli.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                ClicListadoVehiculosCli();
            }
        });
        listados.add(ListadoVehiculosCli);
        
        //------------------RECIBOS--------------------------------------------------------
        
        recibos  = new JMenu("Recibos");
        recibos.setMnemonic('r');
        barrasMenus.add(recibos);
        
        GenerarRecibos = new JMenuItem("Generar Recibos");
        GenerarRecibos.setMnemonic('g');
        GenerarRecibos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clicGenerarRecibos(ae);
            }
        });
        recibos.add(GenerarRecibos);
        
        submRecibosDelMes = new JMenuItem("Recibos por mes");
        submRecibosDelMes.setMnemonic('g');
        submRecibosDelMes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clicRecibosDelMes();
            }
        });
        recibos.add(submRecibosDelMes);
        
        //---------------------------------------------------------------------------------
        
        //---------------------AYUDA------------------------------------------------------------
        Ayuda = new JMenu("Ayuda");
        Ayuda.setMnemonic('A');
        barrasMenus.add(Ayuda);
        
        ManualAyuda = new JMenuItem("Manual Ayuda");
        ManualAyuda.setMnemonic('M');
        ManualAyuda.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                ManejadorManualAyuda(ae);
            }
        }); 
        Ayuda.add(ManualAyuda);
        
        Ayuda.add(new JSeparator());
        
        About = new JMenuItem("Nosotros");
        About.setMnemonic('n');
        About.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                ManejadorClicAbout(ae);
            }
        });
        Ayuda.add(About);

        CargarTarifas();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
     private void ManejadorClicABGruas(ActionEvent e)
    {
        try
        {
            if (ventanaGruas == null)
            {
                ventanaGruas = VentanaGruas.getInstancia(this);
                escritorio.add(ventanaGruas);
                ventanaGruas.setVisible(true);
            }
            ventanaGruas.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ManejadorClicABSolicitudes(ActionEvent e)
    {
        try
        {
            if (ventanaSolicitudes == null)
            {
                ventanaSolicitudes = VentanaSolicitudes.getInstancia(this);
                escritorio.add(ventanaSolicitudes);
                ventanaSolicitudes.setVisible(true);
            }
            ventanaSolicitudes.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ManejadorClicABOperario(ActionEvent e)
    {
        try
        {
            if (ventanaOperarios == null)
            {
                ventanaOperarios = VentanaOperarios.getInstancia(this);
                escritorio.add(ventanaOperarios);
                ventanaOperarios.setVisible(true);
            }
            ventanaOperarios.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void manejarClicABMCli()
    {
        try
        {
            if(ventanaClientes == null) {
                ventanaClientes= VentanaClientes.getInstancia(this);
                escritorio.add(ventanaClientes);
                ventanaClientes.setVisible(true);
            }
            ventanaClientes.setSelected(true);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void manejarClicABMVeh()
    {
        try
        {
            if(ventanaVehiculos == null) {
                ventanaVehiculos= VentanaVehiculos.getInstancia(this, null);
                escritorio.add(ventanaVehiculos);
                ventanaVehiculos.setVisible(true);
            }
            ventanaVehiculos.setSelected(true);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ClicListadoVehiculosCli()
    {
        try
        {
            if(ventanaVehiculosDeCliente == null) {
                ventanaVehiculosDeCliente= VentanaVehiculosDeCliente.getInstancia(this, null);
                escritorio.add(ventanaVehiculosDeCliente);
                ventanaVehiculosDeCliente.setVisible(true);
            }
            ventanaVehiculosDeCliente.setSelected(true);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clicRecibosDelMes()    
    {
        try
        {
            if(ventanaRecibosDelMes == null) {
                ventanaRecibosDelMes = RecibosDelMes.getInstancia(this);
                escritorio.add(ventanaRecibosDelMes);
                ventanaRecibosDelMes.setVisible(true);
            }
            ventanaRecibosDelMes.setSelected(true);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ManejadorClicListadoSolicitudes(ActionEvent e)
    {
        try
        {
            if ( ventanaListadoSolicitudes == null)
            {
                ventanaListadoSolicitudes = VentanaListadoSolicitudes.getInstancia(this);
                escritorio.add(ventanaListadoSolicitudes);
                ventanaListadoSolicitudes.setVisible(true);
            }
            ventanaListadoSolicitudes.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ManejadorClicAuxilioMecanico(ActionEvent e)
    {
        try
        {
            if (ventanaAuxiliosMecanicos == null)
            {
                ventanaAuxiliosMecanicos = VentanaAuxiliosMecanicos.getInstancia(this);
                escritorio.add(ventanaAuxiliosMecanicos);
                ventanaAuxiliosMecanicos.setVisible(true);
            }
            ventanaAuxiliosMecanicos.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ManejadorClicARemolque(ActionEvent e)
    {
        try
        {
            if (ventanaRemolques == null)
            {
                ventanaRemolques = VentanaRemolques.getInstancia(this);
                escritorio.add(ventanaRemolques);
                ventanaRemolques.setVisible(true);
            }
            ventanaRemolques.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Double> CargarTarifas()
    {
      File archivo = null;
      FileReader fr = null;
      BufferedReader br = null;
      
        try 
        {
            fr = new FileReader (new File ("src/Recursos/tarifas.txt"));
            br = new BufferedReader(fr);
            
            double mensualidad = 0;
            double auxilioMecanico = 0;
            double remolque50 = 0;
            double remolque200 = 0;
            double remolque500 = 0;
            double remolque1000 = 0;
            
            String linea;
            int i = 0;
            
            while ((linea = br.readLine()) != null)
                    {
                        String[]partes = linea.split(": ");
                        String l = partes[partes.length-1];
                        switch(i)
                        {
                            case 0:
                                mensualidad = Double.parseDouble(l);
                                Tarifas.add(mensualidad);
                                break;
                            case 1:
                                auxilioMecanico = Double.parseDouble(l);
                                Tarifas.add(auxilioMecanico);
                                break;
                            case 2:
                                remolque50 = Double.parseDouble(l);
                                Tarifas.add(remolque50);
                                break;
                            case 3:
                                remolque200 = Double.parseDouble(l);
                                Tarifas.add(remolque200);
                                break;
                            case 4:
                                remolque500 = Double.parseDouble(l);
                                Tarifas.add(remolque500);
                                break;
                            case 5:
                                remolque1000 = Double.parseDouble(l);
                                Tarifas.add(remolque1000);
                                break;
                            
                            default:
                                return Tarifas;                           
                        }
                        i++;         
                    }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudieron obtener los valores de tarifas", JOptionPane.ERROR_MESSAGE);
        }
        return Tarifas;
    }
    
    private void ManejadorClicAbout(ActionEvent e)
    {
        try
        {
            if (ventanaAbout == null)
            {
                ventanaAbout = VentanaAbout.getInstancia(this);
                escritorio.add(ventanaAbout);
                ventanaAbout.setVisible(true);
            }
            ventanaAbout.setSelected(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void ManejadorManualAyuda(ActionEvent e)
    {
        try
        {
            Runtime.getRuntime().exec("cmd /c start src/Recursos/Help.pdf");   
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clicGenerarRecibos(ActionEvent e){
        try{
            FabricaLogica.GetLogicaRecibo().GenerearRecibos(Tarifas.get(0));
            JOptionPane.showMessageDialog(this, "Se generaron los recibos por el mes pasado", "Listo", JOptionPane.INFORMATION_MESSAGE);
        
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "¡Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}

