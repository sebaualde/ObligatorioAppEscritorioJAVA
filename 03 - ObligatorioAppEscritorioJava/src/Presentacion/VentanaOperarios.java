
package Presentacion;

import EntidadesCompartidas.Operario;
import Logica.FabricaLogica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VentanaOperarios extends JInternalFrame{
    protected JPanel pnlCentro;
    protected JPanel pnlCampos;
    protected JPanel pnlContenedorBtnYdescr;
    protected JPanel pnlDescripcion;
    protected JPanel pnlBotones;
    protected JLabel lblTitulo;
    protected JLabel lblId;
    protected JTextField txtIdOperario;
    protected JLabel lblNombre;
    protected JTextField txtNombre;
    protected JLabel lblFecha;
    protected SpinnerDateModel modeloSpinnerFecha;
    protected JSpinner spFecha;
    protected JButton btnAgregar;
    protected JButton btnEliminar;
    protected JButton btnLimpiar;
    protected JLabel lblDescripcion;
    protected JLabel lblMensaje;
    
    private Operario unOperario;
    private static VentanaOperarios instancia = null;
    
    
    public static VentanaOperarios getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaOperarios(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaOperarios(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Alta y Baja de Operarios");
        
        lblTitulo = new JLabel("Alta y Baja de Operarios");
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
        pnlCampos.setBorder(new TitledBorder("AB - Operarios: "));
        pnlCampos.setLayout(new GridLayout(3,2,3,3));
        pnlCentro.add(pnlCampos);
        
        lblId = new JLabel("ID: ");
        pnlCampos.add(lblId);
        
        txtIdOperario = new JTextField();
        txtIdOperario.setToolTipText("El ID debe tener 3 Letras y 3 Números (Ej: XXX123)");
        txtIdOperario.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                
            }

            @Override
            public void focusLost(FocusEvent fe) {
                ManejadorPerdidaFocus(fe);
            }
        });
        pnlCampos.add(txtIdOperario);
        
        lblNombre = new JLabel("Nombre: ");
        pnlCampos.add(lblNombre);
        
        txtNombre = new JTextField();
        pnlCampos.add(txtNombre);
        
        lblFecha = new JLabel("Fecha de Ingreso: ");
        pnlCampos.add(lblFecha);
        
        modeloSpinnerFecha = new SpinnerDateModel();
        spFecha = new JSpinner(modeloSpinnerFecha);
        spFecha.setEditor(new JSpinner.DateEditor(spFecha, "dd 'de' MMMM 'de' yyyy")); 
        spFecha.setPreferredSize(new Dimension(130, 20));
        spFecha.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ManejadorFecha(e);
            }
        });
        pnlCampos.add(spFecha);
        
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
        lblDescripcion = new JLabel("<html>Ingrese un número de oprario y cambie de campo <br/>para buscarlo y habilitar Agregar o Eliminar.<html/>");
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));
        lblDescripcion.setFont(new Font("Dialog", 0, 9));
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
        
        setPreferredSize(new Dimension(400, 350));
        pack();
        setLocation(130,130);
        
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaOperarios = null;
    }
    
    protected void ManejadorFecha(ChangeEvent e)
    {
        if(unOperario == null)//comprueba que si hay un operario se pueda mostrar una fecha anterior a la de hoy el el spinner
        {
            //estbalezco el formato para compara
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

            //obtengo fecha seleccionada del spinner
            Date fechaSeleccionada = (Date)modeloSpinnerFecha.getValue();
            formato.format(fechaSeleccionada);

            //resto un dia para evitar que se seleccione una fecha de ingreso anterior a la de hoy
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(new Date().getTime());
            cal.add(Calendar.DATE, -1);        

            if (fechaSeleccionada.before(new Date(cal.getTimeInMillis())))
            {
                spFecha.setValue(new Date()); 
            }
        }
    }
    
    protected void ManejadorPerdidaFocus(FocusEvent fe)
    {
        try 
        {
            
            if (txtIdOperario.getText().isEmpty()) 
            {
                txtIdOperario.requestFocus();
                lblMensaje.setText("El ID no puede quedar vacío.");
            }
            else
            {
                if (txtIdOperario.getText().length() != 6) 
                {
                    throw new Exception("El Id debe tener 6 caracteres.");
                }
                
                unOperario = FabricaLogica.GetLogicaOperario().BuscarOperario(txtIdOperario.getText().trim().toUpperCase());
                
                if (unOperario == null)
                {
                    lblMensaje.setText("No se encontro ningun operario con ese ID.");
                    btnAgregar.setEnabled(true);
                }
                else
                {
                    txtNombre.setText(unOperario.getNombre());
                    spFecha.setValue(unOperario.getFechaIngreso());

                    lblMensaje.setText("");
                    txtIdOperario.setEnabled(false);
                    txtNombre.setEnabled(false);
                    spFecha.setEnabled(false);
                    btnEliminar.setEnabled(true);
                }
            }
        } 
        catch (Exception e) 
        {
            txtIdOperario.requestFocus();
            lblMensaje.setText(e.getMessage());
        }
    }
    
    protected void ManejadroClicAgregar(ActionEvent e)
    {
        try 
        {         
            unOperario = new Operario(txtIdOperario.getText().trim().toUpperCase(), txtNombre.getText().trim(), (Date)modeloSpinnerFecha.getValue());
 
            FabricaLogica.GetLogicaOperario().AltaOperario(unOperario);
            
            lblMensaje.setText("¡Operario agregado con exito!");
            Limpiar();
        } 
        catch (Exception ex) 
        {
            unOperario = null;
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void ManejadroClicEliminar(ActionEvent e)
    {
        try 
        {  
            FabricaLogica.GetLogicaOperario().BajaOperario(unOperario);
            
            lblMensaje.setText("¡Operario eliminado con exito!");
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
        txtIdOperario.setText("");
        txtNombre.setText("");
        spFecha.setValue(new Date()); 
        txtIdOperario.setEnabled(true);
        txtNombre.setEnabled(true);
        spFecha.setEnabled(true);
        btnAgregar.setEnabled(false);
        btnEliminar.setEnabled(false);
        unOperario = null;
    }
}
