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
public class VentanaClientes extends JInternalFrame{
    
    protected JPanel pnlTitulo;
    protected JLabel lblTitulo;
        
    protected JPanel pnlCampos;
    protected JTextField txtCedula;
    protected JTextField txtNombre;
    protected JTextField txtDireccion;
    protected JTextField txtTelefono;
  
    protected JPanel pnlBotones;
    protected JPanel pnlBotones2;
    protected JButton btnAgregar;
    protected JButton btnEliminar;
    protected JButton btnModificar;
    protected JButton btnLimpiar;
    protected JButton btnVerVehiculos;
    protected JButton btnCargarVehiculo;
    
    protected JPanel pnlMensaje;
    protected JLabel lblMensaje;
    
    protected Cliente cliente = null;
    
    private static VentanaClientes instancia = null;
    
    public static VentanaClientes getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaClientes(ventanaPrincipal);
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaClientes(VentanaPrincipal pVentanaPrincipal){

        ventanaPrincipal = pVentanaPrincipal;
           inicializarComponentes();
           if(cliente != null){
               habilitarCampos(true);
           }
    }   
     
    protected void inicializarComponentes(){
    
        //TÍTULO
        setTitle("Mantenimiento de Clientes");
        getContentPane().setLayout(new BorderLayout());
        pnlTitulo = new JPanel(new FlowLayout());
        lblTitulo = new JLabel("<html><h1>Mantenimiento de Clientes</h1></html>");
        pnlTitulo.add(lblTitulo);
        
        //CONTROLES
        pnlCampos = new JPanel();
        pnlCampos.setBorder(new TitledBorder("Datos del cliente"));
        pnlCampos.setLayout(new BoxLayout(pnlCampos, BoxLayout.Y_AXIS));
               
        JPanel pnlCedula = new JPanel(new FlowLayout());
        txtCedula = new JTextField();
        txtCedula.setPreferredSize(new Dimension(150,25));
        txtCedula.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                long cedula=0;
                try{
                    if(txtCedula.getText().length()!=8){
                        throw new Exception();
                    }
                    cedula = Long.parseLong(txtCedula.getText());
                    
                    buscarCliente(cedula);
                }
                catch (Exception ex){
                    lblMensaje.setText("Formato de cédula incorrecto");
                }
            }
        });
        pnlCedula.add(new JLabel("Cédula:"));
        pnlCedula.add(txtCedula);
        pnlCampos.add(pnlCedula);
        
        JPanel pnlNombre = new JPanel(new FlowLayout());
        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(150,25)); 
        pnlNombre.add(new JLabel("Nombre:"));
        pnlNombre.add(txtNombre);
        pnlCampos.add(pnlNombre);

        JPanel pnlDireccion = new JPanel(new FlowLayout());
        txtDireccion = new JTextField();
        txtDireccion.setPreferredSize(new Dimension(150,25));   
        pnlDireccion.add(new JLabel("Dirección:"));
        pnlDireccion.add(txtDireccion);
        pnlCampos.add(pnlDireccion);

        JPanel pnlTelefono = new JPanel(new FlowLayout());
        txtTelefono = new JTextField();
        txtTelefono.setPreferredSize(new Dimension(150,25));       
        pnlTelefono.add(new JLabel("Teléfono:"));
        pnlTelefono.add(txtTelefono);
        pnlCampos.add(pnlTelefono);
       
        
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
        
        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnModificar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);
        
        pnlBotones2 = new JPanel(new FlowLayout());
        
        btnVerVehiculos = new JButton("Ver Vehículos");
        btnVerVehiculos.setEnabled(false);
        btnVerVehiculos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnVerVehiculos();
            }
        });
        pnlBotones2.add(btnVerVehiculos);
        
        btnCargarVehiculo = new JButton("Cargar Vehículos");
        btnCargarVehiculo.setEnabled(false);
        btnCargarVehiculo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clicBtnCargarVehiculos();
            }
        });
        pnlBotones2.add(btnCargarVehiculo);
        
        pnlCampos.add(pnlBotones);
        pnlCampos.add(pnlBotones2);
        
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
            long cedula, telefono;
            String nombre, direccion;
            
            cedula = Long.parseLong(txtCedula.getText());
            nombre = txtNombre.getText();
            direccion = txtDireccion.getText();
            try{
            telefono = Long.parseLong(txtTelefono.getText());
            }
            catch(Exception ex){
                throw new Exception("Formato de teléfono incorrecto");
            }
            
            Cliente unCliente = new Cliente(cedula, nombre, direccion, telefono);
            
            Logica.FabricaLogica.GetLogicaCliente().Agregar(unCliente);
            clicBtnLimpiar();
            lblMensaje.setText("Cliente agregado correctamente");
        }    
       catch(Exception ex){
           lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnEliminar() {
        try{
            if(JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar al cliente con cédula " + cliente.getCedula()+"?", "Confirmación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.OK_OPTION){
                Logica.FabricaLogica.GetLogicaCliente().Eliminar(cliente);
                clicBtnLimpiar();
                lblMensaje.setText("Cliente eliminado correctamente");
            }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnModificar() {
        try{
             long cedula, telefono;
            String nombre, direccion;
            
            cedula = cliente.getCedula();
            
            nombre = txtNombre.getText();
            direccion = txtDireccion.getText();
            try{
            telefono = Long.parseLong(txtTelefono.getText());
            }
            catch(Exception ex){
                throw new Exception("Formato de teléfono incorrecto");
            }
            
            Cliente unCliente = new Cliente(cedula, nombre, direccion, telefono);
            
            FabricaLogica.GetLogicaCliente().Modificar(unCliente);
            clicBtnLimpiar();
            lblMensaje.setText("Cliente editado correctamente");
           }    
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void clicBtnVerVehiculos(){
        try{
        if(ventanaPrincipal.ventanaVehiculosDeCliente != null) {
            ventanaPrincipal.ventanaVehiculosDeCliente.setVisible(false);
            ventanaPrincipal.ventanaVehiculosDeCliente.dispose();
            ventanaPrincipal.ventanaVehiculosDeCliente=null;
        }
        ventanaPrincipal.ventanaVehiculosDeCliente= VentanaVehiculosDeCliente.getInstancia(ventanaPrincipal, cliente);
        ventanaPrincipal.escritorio.add(ventanaPrincipal.ventanaVehiculosDeCliente);
        ventanaPrincipal.ventanaVehiculosDeCliente.setVisible(true);
        
        ventanaPrincipal.ventanaVehiculosDeCliente.setSelected(true);
        }catch(Exception ex){
        
        }
    }
    protected void clicBtnCargarVehiculos(){
        try{
        if(ventanaPrincipal.ventanaVehiculos != null) {
            ventanaPrincipal.ventanaVehiculos.setVisible(false);
            ventanaPrincipal.ventanaVehiculos.dispose();
            ventanaPrincipal.ventanaVehiculos=null;
        }
        ventanaPrincipal.ventanaVehiculos = VentanaVehiculos.getInstancia(ventanaPrincipal, cliente);
        ventanaPrincipal.escritorio.add(ventanaPrincipal.ventanaVehiculos);
        ventanaPrincipal.ventanaVehiculos.setVisible(true);
        
        ventanaPrincipal.ventanaVehiculos.setSelected(true);
        }catch(Exception ex){
        
        }
    }
    
    protected void clicBtnLimpiar() {
        try{
            txtCedula.setText("");
            txtNombre.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnModificar.setEnabled(false);
            txtCedula.setEnabled(true);
            cliente = null;
            lblMensaje.setText("");
            btnVerVehiculos.setEnabled(false);
            btnCargarVehiculo.setEnabled(false);
        }    
        catch(Exception ex){
            lblMensaje.setText("No se pudo limpiar la ventana");
        }
        }
    
    protected Cliente buscarCliente(Long cedula){
        try{
           Cliente unCliente = Logica.FabricaLogica.GetLogicaCliente().Buscar(cedula);
           
           if(unCliente != null){
               cliente = unCliente;
               habilitarCampos(true);
               lblMensaje.setText("Cliente encontrado.");
           }
           else{
               habilitarCampos(false);
               lblMensaje.setText("El cliente no existe. Puede agregarlo");
           }
        }
        catch(Exception ex){
            lblMensaje.setText(ex.getMessage());
        }
        
        return cliente;
    } 
    
    protected void habilitarCampos(boolean encontrado){
        txtCedula.setEnabled(false);    
        if(encontrado){
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(true);
            btnModificar.setEnabled(true);            
            btnVerVehiculos.setEnabled(true);
            btnCargarVehiculo.setEnabled(true);
            
            txtNombre.setText(cliente.getNombre());
            txtDireccion.setText(cliente.getDireccion());
            txtTelefono.setText(String.valueOf(cliente.getTelefono()));
        }else{
            btnAgregar.setEnabled(true);
            btnEliminar.setEnabled(false);
            btnModificar.setEnabled(false);
            btnVerVehiculos.setEnabled(false);
            btnCargarVehiculo.setEnabled(false);
        }
    }
    
    @Override
    public void dispose(){
        instancia=null;
        ventanaPrincipal.ventanaClientes=null;
    }
}


