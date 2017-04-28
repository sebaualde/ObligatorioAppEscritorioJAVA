/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import EntidadesCompartidas.Remolque;
import EntidadesCompartidas.SolicitudDeServicio;
import Logica.FabricaLogica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 *
 * @author S.U.R
 */
public class VentanaRemolques extends JInternalFrame{
    
    protected JLabel lblTitulo;
    protected JLabel lblNumSolicitud;
    protected JLabel lblCantidadKms;
    protected JLabel lblMensaje;
    
    protected JPanel pnlCentral;
    protected JPanel pnlCampos;
    protected JPanel pnlBotones;
    
    protected JTextField txtNumeroSolicitud;
    protected JTextField txtCantidadKms;
    
    protected JButton btnAgregar;
    protected JButton btnLimpiar;
    
    private SolicitudDeServicio unaSolicitud = null; 
    private Remolque unServicioDeRemolque = null;
    
    private static VentanaRemolques instancia = null;
    
    public static VentanaRemolques getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaRemolques(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaRemolques(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Alta de Remolques");
        
        lblTitulo = new JLabel("Alta de Remolques");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        lblTitulo.setFont(new Font("Dialog", 1, 24));
        getContentPane().add(lblTitulo, BorderLayout.NORTH);
        
        pnlCentral = new JPanel();
        pnlCentral.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        pnlCentral.setLayout(new BoxLayout(pnlCentral, BoxLayout.Y_AXIS));
        getContentPane().add(pnlCentral, BorderLayout.CENTER);
        
        pnlCampos = new JPanel();
        pnlCampos.setBorder(new TitledBorder("Alta Remolques: "));
        pnlCampos.setLayout(new GridLayout(2,2,3,3));
        pnlCentral.add(pnlCampos);
        
        lblNumSolicitud = new JLabel("Número de serie de la solicitud: ");
        pnlCampos.add(lblNumSolicitud);
        
        txtNumeroSolicitud = new JTextField();
        txtNumeroSolicitud.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                   Limpiar();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                PerdidaFocusNumeroSerie(fe);
            }
        });
        pnlCampos.add(txtNumeroSolicitud);
        
        lblCantidadKms = new JLabel("Cantidad de Kms: ");
        pnlCampos.add(lblCantidadKms);
        
        txtCantidadKms = new JTextField();
        pnlCampos.add(txtCantidadKms);
        
        pnlBotones = new JPanel();
        pnlBotones.setLayout(new BoxLayout(pnlBotones, BoxLayout.Y_AXIS));
        pnlCentral.add(pnlBotones);
        
        pnlBotones = new JPanel();
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        pnlCentral.add(pnlBotones);
        
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicAgregar(e);
            }
        });
        btnAgregar.setEnabled(false);
        pnlBotones.add(btnAgregar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Limpiar();
                lblMensaje.setText("");
            }
        });
        pnlBotones.add(btnLimpiar);
        
        lblMensaje = new JLabel();
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        getContentPane().add(lblMensaje, BorderLayout.SOUTH);
        
        setClosable(true);
        setMaximizable(false);
        setIconifiable(true);
        setResizable(false);
        
        setPreferredSize(new Dimension(600, 320));
        pack();
        setLocation(85,10); 
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaRemolques = null;
    }
    
     protected void PerdidaFocusNumeroSerie(FocusEvent fe)
    {
        try 
        {       
            int numeroSerie = 0;
            
            try 
            {
               numeroSerie = Integer.parseInt(txtNumeroSolicitud.getText());
                
                if(numeroSerie != 0)
                    unaSolicitud = FabricaLogica.GetLogicaSolicitudServicio().BuscarSolicitudDeServicio(numeroSerie);           
            } 
            catch (NumberFormatException ne)
            {
                btnAgregar.setEnabled(true);
            }

            if (unaSolicitud != null)
            {
                lblMensaje.setText("Solicitud de servicio encontrada!");
                txtNumeroSolicitud.setEnabled(false);
                btnAgregar.setEnabled(true);
            }
            else
            {
                txtNumeroSolicitud.requestFocus();
                lblMensaje.setText("No existe una solicitud de servicio con ese numero");
                btnAgregar.setEnabled(false);
            }
        }    
        catch (Exception e) 
        {
            txtNumeroSolicitud.requestFocus();
            lblMensaje.setText(e.getMessage());
        }
    }
     
    protected void ManejadorClicAgregar(ActionEvent e)
    {
        try 
        {
            unServicioDeRemolque = new Remolque();
            unServicioDeRemolque.setNumSerie(1);
            unServicioDeRemolque.setUnaSolicitud(unaSolicitud);
            try
            {
            unServicioDeRemolque.setCantidadKm(Integer.parseInt(txtCantidadKms.getText()));
            }
            catch(Exception ex){
                throw new Exception("Formato de Km incorrecto, debe ser numerico");
            }
            
            
            FabricaLogica.GetLogicaServicio().AltaServicio(unServicioDeRemolque, ventanaPrincipal.Tarifas);
             
            lblMensaje.setText("¡Servicio de remolque agregada con exito!");
            Limpiar();
            txtNumeroSolicitud.requestFocus();
        } 
        catch (Exception ex) 
        {
            lblMensaje.setText(ex.getMessage());
        }
    }
     
     protected void Limpiar()
     {
         txtNumeroSolicitud.setText("");
         txtNumeroSolicitud.setEnabled(true);
         txtNumeroSolicitud.requestFocus();
         txtCantidadKms.setText("");
         btnAgregar.setEnabled(false);
         unServicioDeRemolque = null;
         unaSolicitud = null;
     }
}