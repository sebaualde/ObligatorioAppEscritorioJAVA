/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import EntidadesCompartidas.Cliente;
import Logica.FabricaLogica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author S.U.R
 */
public class RecibosDelMes extends JInternalFrame{
    protected JPanel pnlCentro;
    protected JPanel pnlTitulo;
    protected JLabel lblTitulo;
        
    protected JScrollPane scroll;
    
    protected JPanel pnlCampos;
    protected JTextField txtMes;
    protected JTextField txtAnio;
    protected JButton btnListar;
    
    protected JPanel pnlGrilla;
    
    protected JPanel pnlMensaje;
    protected JLabel lblMensaje;
    protected JTable grilla;
    
    protected ModeloJTableRecibos modeloRecibos;
    
    private static RecibosDelMes instancia = null;
    
    public static RecibosDelMes getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new RecibosDelMes(ventanaPrincipal);
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public RecibosDelMes(VentanaPrincipal pVentanaPrincipal){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
    }   
    
    public RecibosDelMes(VentanaPrincipal pVentanaPrincipal, Cliente pCliente){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
    }   
     
    protected void inicializarComponentes(){
    
        //TÍTULO
        setTitle("Recibos del Mes");
                     
        pnlCentro =  new JPanel();
       
        JPanel pnlCampos = new JPanel(new FlowLayout());
        
        txtMes = new JTextField();
        txtMes.setPreferredSize(new Dimension(50,25));

        txtAnio = new JTextField();
        txtAnio.setPreferredSize(new Dimension(50,25));

        btnListar = new JButton("Listar");
        btnListar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
             int mes, anio;
                try{
                    try{   
                        mes = Integer.parseInt(txtMes.getText().trim());
                    }catch(Exception ex){
                        throw new Exception("Mes incorrecto");
                    }
                    try{   
                        anio = Integer.parseInt(txtAnio.getText().trim());
                    }catch(Exception ex){
                        throw new Exception("Año incorrecto");
                    }
                    if(mes >12 || mes <1){
                        throw new Exception("Mes incorrecto");
                    }
                    
                    if(anio <0){
                        throw new Exception("Año incorrecto");
                    }
                    
                    listarRecibos(mes, anio);
                }
                catch (Exception ex){
                    lblMensaje.setText(ex.getMessage());
                }    
            }
        });
        
        pnlCampos.add(new JLabel("Mes (mm):"));
        pnlCampos.add(txtMes);
        pnlCampos.add(new JLabel(" Año (aaaa):"));
        pnlCampos.add(txtAnio);
        pnlCampos.add(btnListar);
        
        pnlGrilla= new JPanel();
        
        grilla= new JTable();
        grilla.setRowHeight(35);
        grilla.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount()==2){
                    try{
                   Long cedula= Long.parseLong(grilla.getValueAt(grilla.getSelectedRow(),3).toString());
                   Cliente cli = FabricaLogica.GetLogicaCliente().Buscar(cedula);
                   String detalle = "<html>Nro. Serie: " + grilla.getValueAt(grilla.getSelectedRow(),0)+ "    Fecha: " + grilla.getValueAt(grilla.getSelectedRow(),1) + "<br> Cliente: " + cli.getNombre() + " CI: " + cli.getCedula() + "<br><b>Importe total: </b> " + grilla.getValueAt(grilla.getSelectedRow(),2);
                    
                    JOptionPane.showMessageDialog(ventanaPrincipal, detalle, "Detalle del recibo", JOptionPane.INFORMATION_MESSAGE);
                    //grilla.getValueAt(grilla.getSelectedRow(),1);
                    }
                    catch(Exception ex){
                        lblMensaje.setText(ex.getMessage());
                    }
                }
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                   }

            @Override
            public void mouseReleased(MouseEvent me) {
                    }

            @Override
            public void mouseEntered(MouseEvent me) {
                      }

            @Override
            public void mouseExited(MouseEvent me) {
                 }
        });
        
        grilla.setRowHeight(100);
        scroll = new JScrollPane(grilla);
        
        pnlCentro.add(scroll);
        lblMensaje = new JLabel();        
        
        getContentPane().add(pnlCampos, BorderLayout.NORTH);
        getContentPane().add(pnlCentro, BorderLayout.CENTER);
        getContentPane().add(lblMensaje, BorderLayout.SOUTH);
        
        setClosable(true);
        setMaximizable(false);
        setIconifiable(true);
        setResizable(false);
        
        
        pack();
        setLocation(50,50);
        setVisible(true);
    }
    
  
    
    protected void listarRecibos(int mes, int anio){
        try{
            ArrayList<EntidadesCompartidas.Recibo> recibos = FabricaLogica.GetLogicaRecibo().ListarRecibosDelMes(mes, anio);
            modeloRecibos = new ModeloJTableRecibos(recibos);
            grilla.setModel(modeloRecibos);
            if(recibos.size()!=0){
            lblMensaje.setText("Listando " + recibos.size() + " recibos");
            }else{
                lblMensaje.setText("No hay recibos en el mes y año ingresados");
            }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    } 
    
    @Override
    public void dispose(){
        instancia=null;
        ventanaPrincipal.ventanaRecibosDelMes =null;
    }
}


