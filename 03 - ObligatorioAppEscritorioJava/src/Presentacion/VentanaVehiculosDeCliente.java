/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import Logica.FabricaLogica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
public class VentanaVehiculosDeCliente extends JInternalFrame{
    protected JPanel pnlCentro;
    protected JPanel pnlTitulo;
    protected JLabel lblTitulo;
        
    protected JScrollPane scroll;
    
    protected JPanel pnlCampos;
    protected JTextField txtCedula;
    protected JButton btnListar;
    
    protected JPanel pnlGrilla;
    
    protected JPanel pnlMensaje;
    protected JLabel lblMensaje;
    protected JTable grilla;
    
    protected ModeloJTableVehiculos modeloVehiculos;
    
    private static VentanaVehiculosDeCliente instancia = null;
    
    public static VentanaVehiculosDeCliente getInstancia(VentanaPrincipal ventanaPrincipal, Cliente pCliente)
    {
        if (instancia == null) 
        {
            instancia = new VentanaVehiculosDeCliente(ventanaPrincipal, pCliente);
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaVehiculosDeCliente(VentanaPrincipal pVentanaPrincipal){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
    }   
    
    public VentanaVehiculosDeCliente(VentanaPrincipal pVentanaPrincipal, Cliente pCliente){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
           
           if(pCliente!=null){
               listarVehiculos(pCliente);
           }
    }   
     
    protected void inicializarComponentes(){
    
        //TÍTULO
        setTitle("Vehículos del Cliente");
                     
        pnlCentro =  new JPanel();
       
        JPanel pnlCampos = new JPanel(new FlowLayout());
        
        txtCedula = new JTextField();
        txtCedula.setPreferredSize(new Dimension(150,25));

        btnListar = new JButton("Listar");
        btnListar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
             long cedula=0;
                try{
                    if(txtCedula.getText().length()!=8){
                        throw new Exception("La cédula debe tener 8 caracteres");
                    }
                    cedula = Long.parseLong(txtCedula.getText());
                    
                    Cliente cliente = FabricaLogica.GetLogicaCliente().Buscar(cedula);
                    if(cliente==null){
                        throw new Exception("El cliente ingresado no existe");
                    }
                    
                    listarVehiculos(cliente);
                }
                catch (Exception ex){
                    lblMensaje.setText(ex.getMessage());
                    txtCedula.requestFocus();
                }    
            }
        });
        
        pnlCampos.add(new JLabel("Cédula: "));
        pnlCampos.add(txtCedula);
        pnlCampos.add(btnListar);
        
        pnlGrilla= new JPanel();
        
        grilla= new JTable();
        grilla.setRowHeight(100);
        scroll = new JScrollPane(grilla);
        
        pnlCentro.add(scroll);
        lblMensaje = new JLabel();;        
        
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
    
  
    
    protected void listarVehiculos(Cliente cliente){
        try{
          ArrayList<Vehiculo> vehiculos = Logica.FabricaLogica.GetLogicaVehiculo().vehiculosPorCliente(cliente);
          modeloVehiculos = new ModeloJTableVehiculos(vehiculos);
          grilla.setModel(modeloVehiculos);
          
          if(vehiculos.size()!=0){
              lblMensaje.setText("El cliente tiene " + vehiculos.size() + " vehículo(s)");
          }else{
              lblMensaje.setText("El cliente no tiene vehículos registrados");
          }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    } 
    
    @Override
    public void dispose(){
        instancia=null;
        ventanaPrincipal.ventanaVehiculosDeCliente =null;
    }
}


