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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author S.U.R
 */
public class VentanaVehiculos extends JInternalFrame{
    
    protected JPanel pnlTitulo;
    protected JLabel lblTitulo;
        
    protected JPanel pnlCampos;
    protected JTextField txtMatricula;
    protected JTextField txtMarca;
    protected JTextField txtModelo;
    protected JTextField txtPeso;
    protected JTextField txtCliente;
  
    protected JPanel pnlBotones;
    protected JButton btnAgregar;
    protected JButton btnEliminar;
    protected JButton btnModificar;
    protected JButton btnLimpiar;
    
    protected JPanel pnlMensaje;
    protected JLabel lblMensaje;
    protected JLabel lblNombreCliente;
    
    protected Cliente cliente = null;
    protected Vehiculo vehiculo = null;
    
    private static VentanaVehiculos instancia = null;
    
    public static VentanaVehiculos getInstancia(VentanaPrincipal ventanaPrincipal, Cliente pCliente)
    {
        if (instancia == null) 
        {
            instancia = new VentanaVehiculos(ventanaPrincipal, pCliente);
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaVehiculos(VentanaPrincipal pVentanaPrincipal, Cliente pCliente){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
           
           if(pCliente!=null){
               cliente = pCliente;
               txtCliente.setText(String.valueOf(pCliente.getCedula()));
               lblNombreCliente.setText(pCliente.getNombre());
               txtCliente.setEnabled(false);
           }
          
    }   
     
    protected void inicializarComponentes(){
    
        //TÍTULO
        setTitle("Base de datos de Vehículos");
        getContentPane().setLayout(new BorderLayout());
        pnlTitulo = new JPanel(new FlowLayout());
        lblTitulo = new JLabel("<html><h1>Mantenimiento de Vehículos</h1></html>");
        pnlTitulo.add(lblTitulo);
        
        //CONTROLES
        pnlCampos = new JPanel();
        pnlCampos.setBorder(new TitledBorder("Datos del vehículo"));
        pnlCampos.setLayout(new BoxLayout(pnlCampos, BoxLayout.Y_AXIS));
               
        JPanel pnlMatricula = new JPanel(new FlowLayout());
        txtMatricula = new JTextField();
        txtMatricula.requestFocus();
        txtMatricula.setPreferredSize(new Dimension(150,25));
        txtMatricula.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                try{
                    if(txtMatricula.getText().trim().length()==0 || txtMatricula.getText().trim().length()>7){
                        throw new Exception();
                    }
                        
                    buscarVehiculo(txtMatricula.getText().trim());
                }
                catch (Exception ex){
                    lblMensaje.setText("Formato de matrícula incorrecto");
                }
            }
        });
        pnlMatricula.add(new JLabel("Matrícula:"));
        pnlMatricula.add(txtMatricula);
        pnlCampos.add(pnlMatricula);
        
        JPanel pnlmarca = new JPanel(new FlowLayout());
        txtMarca = new JTextField();
        txtMarca.setPreferredSize(new Dimension(150,25)); 
        pnlmarca.add(new JLabel("Marca:"));
        pnlmarca.add(txtMarca);
        pnlCampos.add(pnlmarca);

        JPanel pnlModelo = new JPanel(new FlowLayout());
        txtModelo = new JTextField();
        txtModelo.setPreferredSize(new Dimension(150,25));   
        pnlModelo.add(new JLabel("Modelo:"));
        pnlModelo.add(txtModelo);
        pnlCampos.add(pnlModelo);

        JPanel pnlPeso = new JPanel(new FlowLayout());
        txtPeso = new JTextField();
        txtPeso.setPreferredSize(new Dimension(150,25));       
        pnlPeso.add(new JLabel("Peso:"));
        pnlPeso.add(txtPeso);
        pnlPeso.add(new JLabel(" Kgs"));                
        pnlCampos.add(pnlPeso);

        JPanel pnlPropietario = new JPanel(new FlowLayout());
        txtCliente = new JTextField();
        txtCliente.setPreferredSize(new Dimension(150,25));
        txtCliente.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                long cedula;
                try{
                    
                    cedula = Long.parseLong(txtCliente.getText().trim());
                    cliente = FabricaLogica.GetLogicaCliente().Buscar(cedula);
                    
                    if(cliente == null){
                        throw new Exception();
                    }
                    else{
                        lblNombreCliente.setText(cliente.getNombre());                        
                    }
                }
                catch (Exception ex){
                    lblNombreCliente.setText("<no encontrado>");
                }                
                
            }
        });
        pnlPropietario.add(new JLabel("Propietario:"));
        pnlPropietario.add(txtCliente);
        lblNombreCliente= new JLabel("");
        pnlPropietario.add(lblNombreCliente);                
        pnlCampos.add(pnlPropietario);
       
        
        //BOTONES
        pnlBotones = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnAgregar.setEnabled(false);
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnAgregar();
            }
        });
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnEliminar();
            }
        });
        
        btnModificar = new JButton("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnModificar();
            }
        });
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnLimpiar();
            }
        });
        
        pnlBotones.setBorder(new TitledBorder("Opciones"));
        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnModificar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);
        
        pnlCampos.add(pnlBotones);
        
        pnlMensaje= new JPanel();
        pnlMensaje.setBorder(new TitledBorder("Mensajes"));
        lblMensaje= new JLabel();
        pnlMensaje.add(lblMensaje);
        
        
        
        getContentPane().add(pnlTitulo, BorderLayout.NORTH);
        getContentPane().add(pnlCampos, BorderLayout.CENTER);
        getContentPane().add(pnlMensaje, BorderLayout.SOUTH);
        
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        
        
        pack();
        setLocation(50,50);
        
    }
    
    protected void clicBtnAgregar() {
        try{
            String marca, modelo;
            int peso;
                       
            if(cliente == null)
            {
                throw new Exception("El cliente no existe");
            }
            
            marca = txtMarca.getText().trim();
            modelo = txtModelo.getText().trim();
            
            try{
            peso = Integer.parseInt(txtPeso.getText().trim());
            }
            catch(Exception ex){
                throw new Exception("Formato de peso incorrecto");
            }
            
            Vehiculo unVehiculo = new Vehiculo(txtMatricula.getText().trim(), marca, modelo, peso, cliente);
            
            Logica.FabricaLogica.GetLogicaVehiculo().Agregar(unVehiculo);
            clicBtnLimpiar();
            lblMensaje.setText("Vehículo agregado correctamente");
        }    
       catch(Exception ex){
           lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnEliminar() {
        try{
            if(JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar al vehículo con matrícula " + vehiculo.getMatricula()+"?", "Confirmación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.OK_OPTION){
                Logica.FabricaLogica.GetLogicaVehiculo().Eliminar(vehiculo);
                clicBtnLimpiar();
                lblMensaje.setText("Vehículo eliminado correctamente");
            }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnModificar() {
        try{
            String marca, modelo;
            int peso;
                       
            if(cliente == null)
            {
                throw new Exception("El cliente no existe");
            }
            
            marca = txtMarca.getText().trim();
            modelo = txtModelo.getText().trim();
            
            try{
            peso = Integer.parseInt(txtPeso.getText().trim());
            }
            catch(Exception ex){
                throw new Exception("Formato de peso incorrecto");
            }
            
            Vehiculo unVehiculo = new Vehiculo(txtMatricula.getText().trim(), marca, modelo, peso, cliente);
            
            Logica.FabricaLogica.GetLogicaVehiculo().Modificar(unVehiculo);
            clicBtnLimpiar();
            lblMensaje.setText("Vehículo modificado correctamente");
        }    
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnLimpiar() {
        try{
            txtMatricula.setText("");
            txtMarca.setText("");
            txtModelo.setText("");
            txtPeso.setText("");
            txtCliente.setText("");
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnModificar.setEnabled(false);
            txtMatricula.setEnabled(true);
            cliente = null;
            lblMensaje.setText("");
            lblNombreCliente.setText("");
            vehiculo=null;
            txtCliente.setEnabled(true);
        }    
        catch(Exception ex){
            lblMensaje.setText("No se pudo limpiar la ventana");
        }
        }
    
    protected Cliente buscarVehiculo(String matricula){
        try{
           Vehiculo unVehiculo = Logica.FabricaLogica.GetLogicaVehiculo().Buscar(matricula);
           
           if(unVehiculo != null){
               vehiculo = unVehiculo;
               habilitarCampos(true);
               lblMensaje.setText("Vehículo encontrado!");
               cliente = Logica.FabricaLogica.GetLogicaCliente().Buscar(Long.parseLong(txtCliente.getText()));
               
           }
           else{
               habilitarCampos(false);
               lblMensaje.setText("El vehículo no existe. Puede agregarlo");
           }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
        
        return cliente;
    } 
    
    protected void habilitarCampos(boolean encontrado){
        txtMatricula.setEnabled(false);    
        if(encontrado){
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(true);
            btnModificar.setEnabled(true);
            
            txtMarca.setText(vehiculo.getMarca());
            txtModelo.setText(vehiculo.getModelo());
            txtPeso.setText(String.valueOf(vehiculo.getPeso()));
            txtCliente.setText(String.valueOf(vehiculo.getPropietario().getCedula()));
            lblNombreCliente.setText(vehiculo.getPropietario().getNombre());
        }else{
            btnAgregar.setEnabled(true);
            btnEliminar.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }
    
    @Override
    public void dispose(){
        instancia=null;
        ventanaPrincipal.ventanaVehiculos=null;
    }
}


