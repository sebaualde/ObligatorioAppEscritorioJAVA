
package Presentacion;

import EntidadesCompartidas.Grua;
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

public class VentanaGruas extends JInternalFrame{
    protected JLabel lblTitulo;
    protected JPanel pnlCentro;
    protected JPanel pnlCampos;
    protected JPanel pnlContenedorBtnYdescr;
    protected JPanel pnlDescripcion;
    protected JPanel pnlBotones;
    protected JLabel lblDescripcion;
    protected JLabel lblNumero;
    protected JTextField txtNumero;
    protected JLabel lblPesoMaximo;
    protected JTextField txtPesoMaximo;  
    protected JButton btnAgregar;
    protected JButton btnEliminar;
    protected JButton btnLimpiar;
    protected JLabel lblMensaje;
    private Grua unaGrua = null;
    
    private static VentanaGruas instancia;
    
    public static VentanaGruas getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaGruas(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaGruas(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Alta y Baja de Grúas");
        
        lblTitulo = new JLabel("Alta y Baja de Grúas");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        lblTitulo.setFont(new Font("Dialog", 1, 24));
        getContentPane().add(lblTitulo, BorderLayout.NORTH);
          
        pnlCentro = new JPanel();
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        getContentPane().add(pnlCentro, BorderLayout.CENTER);
        
        //------------------------------campos---------------------------------
        
        pnlCampos = new JPanel();
        pnlCampos.setBorder(new TitledBorder("AB - Grúas: "));
        pnlCampos.setLayout(new GridLayout(2,2,3,3));
        pnlCentro.add(pnlCampos);
        
        lblNumero = new JLabel("Número de Grúa: ");
        pnlCampos.add(lblNumero);
        
        txtNumero = new JTextField();
        txtNumero.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
               Limpiar();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                ManejadorDejarFocus(fe); 
            }
        });
        txtNumero.requestFocus();
        pnlCampos.add(txtNumero);
        
        lblPesoMaximo = new JLabel("Peso máximo de carga: ");
        pnlCampos.add(lblPesoMaximo);
        
        txtPesoMaximo = new JTextField();
        pnlCampos.add(txtPesoMaximo);     
          
        //------------------------------botones---------------------------------
        
        pnlContenedorBtnYdescr = new JPanel();
        pnlContenedorBtnYdescr.setLayout(new BoxLayout(pnlContenedorBtnYdescr, BoxLayout.Y_AXIS));
        pnlCentro.add(pnlContenedorBtnYdescr);
        
        pnlBotones = new JPanel();
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        pnlContenedorBtnYdescr.add(pnlBotones);
        
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadroClicAgregar(e);
            }
        });
        btnAgregar.setEnabled(false);
        pnlBotones.add(btnAgregar);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadroClicEliminar(e);
            }
        });
        btnEliminar.setEnabled(false);
        pnlBotones.add(btnEliminar);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadroClicLimpiar(e);
            }
        });
        pnlBotones.add(btnLimpiar);
        
        pnlDescripcion = new JPanel();
        lblDescripcion = new JLabel("<html>Ingrese el número de una grúa para buscar y cambie <br/>de campo para habilitar Agregar o Eliminar.<html/>");
        lblDescripcion.setFont(new Font("Dialog", 0, 9));
        pnlBotones.add(lblDescripcion, BorderLayout.NORTH);
        pnlDescripcion.add(lblDescripcion);
        
        pnlContenedorBtnYdescr.add(pnlDescripcion);
        
        //------------------------------mensaje---------------------------------
        
        lblMensaje = new JLabel();
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        getContentPane().add(lblMensaje, BorderLayout.SOUTH);
        
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        
        setPreferredSize(new Dimension(400, 320));
        pack();
        setLocation(50,50);
        
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaGruas = null;
    }
    
    protected void ManejadorDejarFocus(FocusEvent fe) 
    {
        try 
        {
            unaGrua = FabricaLogica.GetLogicaGrua().BuscarGrua(Integer.parseInt(txtNumero.getText()));
            
            if (unaGrua == null)
            {
                lblMensaje.setText("No se encontro ninguna grúa con ese número.");
                txtNumero.setEnabled(false);
                btnAgregar.setEnabled(true);
            }
            else
            {
                txtPesoMaximo.setText(Integer.toString(unaGrua.getPesoMaximoCarga()));
                lblMensaje.setText("");
                txtNumero.setEnabled(false);
                txtPesoMaximo.setEnabled(false);
                btnEliminar.setEnabled(true);
            }
   
        } 
        catch (NumberFormatException nfe)
        {
            txtNumero.requestFocus();
            lblMensaje.setText("El formato del número no es correcto o exede los 2147483647.");
        }
        catch (Exception e) 
        {
            txtNumero.requestFocus();
            lblMensaje.setText(e.getMessage());
        }
        
    }
    
    protected void ManejadroClicAgregar(ActionEvent e)
    {
        try 
        {
            if (txtPesoMaximo.getText().trim().isEmpty()) 
            {
                throw new Exception("El peso no puede quedar vacío.");
            }
            
            unaGrua = new Grua(Integer.parseInt(txtNumero.getText().trim()), Integer.parseInt(txtPesoMaximo.getText().trim()));
            
            FabricaLogica.GetLogicaGrua().AltaGrua(unaGrua);
            
            lblMensaje.setText("¡Grúa agregada con exito!");
            Limpiar();
        } 
        catch (NumberFormatException nfe)
        {
            txtPesoMaximo.requestFocus();
            lblMensaje.setText("El formato del peso no es correcto o exede los 2147483647.");
        }
        catch (Exception ex) 
        {
            txtPesoMaximo.requestFocus();
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void ManejadroClicEliminar(ActionEvent e)
    {
        try 
        {  
            FabricaLogica.GetLogicaGrua().BajaGrua(unaGrua);
            
            lblMensaje.setText("¡Grúa eliminada con exito!");
            Limpiar();
        } 
        catch (Exception ex) 
        {
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void ManejadroClicLimpiar(ActionEvent e)
    {
        Limpiar();
        lblMensaje.setText("");
        
    }
    
    protected void Limpiar()
    {
        
        txtNumero.setText("");
        txtPesoMaximo.setText("");
        txtNumero.setEnabled(true);
        txtNumero.requestFocus();
        unaGrua = null;
        txtNumero.setEnabled(true);
        txtPesoMaximo.setEnabled(true);
        btnEliminar.setEnabled(false);
        btnAgregar.setEnabled(false);
    }
}
