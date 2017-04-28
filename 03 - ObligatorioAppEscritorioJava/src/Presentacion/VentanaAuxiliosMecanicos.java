package Presentacion;

import EntidadesCompartidas.AuxilioMecanico;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class VentanaAuxiliosMecanicos extends JInternalFrame{
    
    protected JLabel lblTitulo;
    protected JLabel lblNumSolicitud;
    protected JLabel lblDescripcionProblema;
    protected JLabel lblDescripcionReparacion;
    protected JLabel lblCostoRepuesto;
    protected JLabel lblMensaje;
    
    protected JPanel pnlCentral;
    protected JPanel pnlCampos;
    protected JPanel pnlBotones;
    
    protected JTextField txtNumeroSolicitud;
    protected JTextField txtCostoRepuesto;
    
    protected JTextArea txtaDescripcionProblema;
    protected JTextArea txtaDescripcionReparacion;
    
    protected JScrollPane scpProblema;
    protected JScrollPane scpReparacion;
    
    protected JButton btnAgregar;
    protected JButton btnLimpiar;
    
    private SolicitudDeServicio unaSolicitud = null; 
    private AuxilioMecanico unServicioDeAuxilio = null;
    
    private static VentanaAuxiliosMecanicos instancia = null;
    
    public static VentanaAuxiliosMecanicos getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaAuxiliosMecanicos(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaAuxiliosMecanicos(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Alta de Auxilios Mecanicos");
        
        lblTitulo = new JLabel("Alta de Auxilios Mecanicos");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        lblTitulo.setFont(new Font("Dialog", 1, 24));
        getContentPane().add(lblTitulo, BorderLayout.NORTH);
        
        pnlCentral = new JPanel();
        pnlCentral.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        pnlCentral.setLayout(new BoxLayout(pnlCentral, BoxLayout.Y_AXIS));
        getContentPane().add(pnlCentral, BorderLayout.CENTER);
        
        pnlCampos = new JPanel();
        pnlCampos.setBorder(new TitledBorder("Alta Auxilio Mecanico: "));
        pnlCampos.setLayout(new GridLayout(4,2,3,3));
        pnlCentral.add(pnlCampos);
        
        lblNumSolicitud = new JLabel("Número de serie de la solicitud: ");
        pnlCampos.add(lblNumSolicitud);
        
        txtNumeroSolicitud = new JTextField();
        txtNumeroSolicitud.setPreferredSize(new Dimension(200, 35));
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
        
        lblDescripcionProblema = new JLabel("Descripcion del problema: ");
        pnlCampos.add(lblDescripcionProblema);
        
        txtaDescripcionProblema = new JTextArea();
        txtaDescripcionProblema.setPreferredSize(new Dimension(200, 100));
        
        scpProblema = new JScrollPane(txtaDescripcionProblema);
        pnlCampos.add(scpProblema);
        
        lblDescripcionReparacion = new JLabel("Descripcion de la reparacion: ");
        pnlCampos.add(lblDescripcionReparacion);
        
        txtaDescripcionReparacion = new JTextArea();
        txtaDescripcionReparacion.setPreferredSize(new Dimension(200, 100));
        
        scpReparacion = new JScrollPane(txtaDescripcionReparacion);
        pnlCampos.add(scpReparacion);
        
        lblCostoRepuesto = new JLabel("Costo de los repuestos: ");
        pnlCampos.add(lblCostoRepuesto);
        
        txtCostoRepuesto = new JTextField();
        txtCostoRepuesto.setPreferredSize(new Dimension(200, 35));
        pnlCampos.add(txtCostoRepuesto);
        
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
        
        setPreferredSize(new Dimension(600, 500));
        pack();
        setLocation(85,10); 
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaAuxiliosMecanicos = null;
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
            }
        }    
        catch (Exception e) 
        {
            txtNumeroSolicitud.requestFocus();
            lblMensaje.setText(e.getMessage());
        }
    }
     
     protected void Limpiar()
     {
         txtNumeroSolicitud.setText("");
         txtNumeroSolicitud.requestFocus();
         txtNumeroSolicitud.setEnabled(true);
         txtCostoRepuesto.setText("");
         txtaDescripcionProblema.setText("");
         txtaDescripcionReparacion.setText("");
         btnAgregar.setEnabled(false);
         unaSolicitud = null;
         unServicioDeAuxilio = null;
     }
     
     protected void ManejadorClicAgregar(ActionEvent e)
    {
        try 
        {
            unServicioDeAuxilio = new AuxilioMecanico();
            unServicioDeAuxilio.setNumSerie(1);
            unServicioDeAuxilio.setUnaSolicitud(unaSolicitud);
            unServicioDeAuxilio.setDescripcionProblema(txtaDescripcionProblema.getText());
            unServicioDeAuxilio.setDescripcionReparacion(txtaDescripcionReparacion.getText());
            try
            {
                if (txtCostoRepuesto.getText().isEmpty())
                    unServicioDeAuxilio.setCostoRepuesto(0);
                else
                    unServicioDeAuxilio.setCostoRepuesto(Integer.parseInt(txtCostoRepuesto.getText()));
            }
            catch(Exception ex){
                throw new Exception("Formato de costo de repuestos incorrecto, debe ser numerico");
            }

            FabricaLogica.GetLogicaServicio().AltaServicio(unServicioDeAuxilio, ventanaPrincipal.Tarifas);
             
            lblMensaje.setText("¡Servicio de auxilio agregada con exito!");

            txtNumeroSolicitud.requestFocus();
        } 
        catch (Exception ex) 
        {
            lblMensaje.setText(ex.getMessage());
        }
    }
}
