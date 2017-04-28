
package Presentacion;

import EntidadesCompartidas.Grua;
import EntidadesCompartidas.Operario;
import EntidadesCompartidas.SolicitudDeServicio;
import EntidadesCompartidas.Vehiculo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaSolicitudes extends JInternalFrame{
    protected JPanel pnlCentro;
    protected JPanel pnlCampos;
    protected JPanel pnlContenedorBtnYdescr;
    protected JPanel pnlDescripcion;
    protected JPanel pnlBotones;
    protected JLabel lblTitulo;
    protected JLabel lblNumeroSerie;
    protected JTextField txtNumeroSerie;
    protected JLabel lblFecha;
    protected JLabel lblFechaServicio;
    protected DateFormat formateador;
    protected JSpinner spFecha;
    protected JLabel lblDireccion;
    protected JTextField txtDireccion;
    protected JLabel lblVehiculo;
    protected JTextField txtVehiculo;
    protected JLabel lblGruas;
    protected JComboBox cboGruas;
    protected JLabel lblOperarios;
    protected DefaultListModel lmModelo;
    protected JScrollPane spnBarrasDesplazamientoOperarios;
    protected JList lstOperarios;
    protected JButton btnAgregar;
    protected JButton btnCancelado;
    protected JButton btnLimpiar;
    protected JLabel lblDescripcion;
    protected JLabel lblMensaje;
    
    private SolicitudDeServicio unaSolicitud = null; 
    private List<Grua> listaGruas = new ArrayList<>();//lista de gruas disponibles
    private ArrayList<Operario> listaOperarios = new ArrayList<>();//lista de carga de operarios disponibles
    private ArrayList<Operario> operarios = new ArrayList<>();//lista para seleccion de operarios en altas
    private Vehiculo unVehiculo = null; //vehiculo buscado para el alta
    
    private static VentanaSolicitudes instancia = null;
    
    public static VentanaSolicitudes getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaSolicitudes(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaSolicitudes(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Alta y Cancelación de Solicitudes de Servicios ");
        
        lblTitulo = new JLabel("Alta y Cancelación de Solicitud de Servicios");
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
        pnlCampos.setBorder(new TitledBorder("AC-Solicitud de Servicios: "));
        pnlCampos.setLayout(new GridLayout(6,2,3,3));
        pnlCentro.add(pnlCampos);
        
        lblNumeroSerie = new JLabel("Número de serie: ");
        pnlCampos.add(lblNumeroSerie);
        
        txtNumeroSerie = new JTextField();
        txtNumeroSerie.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {

            }

            @Override
            public void focusLost(FocusEvent fe) {
                ManejadorPerdidaFocusNumeroSerie(fe);
            }
        });
        txtNumeroSerie.setToolTipText("El ID es autogenerado.");
        pnlCampos.add(txtNumeroSerie);
        
        lblFecha = new JLabel("Fecha: ");
        pnlCampos.add(lblFecha);
        
        formateador = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault());  
        lblFechaServicio = new JLabel(formateador.format(new Date()));
        pnlCampos.add(lblFechaServicio);
        
        lblDireccion = new JLabel("Dirección: ");
        pnlCampos.add(lblDireccion);
        
        txtDireccion = new JTextField();
        pnlCampos.add(txtDireccion);
        
        lblVehiculo = new JLabel("Matricula del Vehiculo: ");
        pnlCampos.add(lblVehiculo);
        
        txtVehiculo = new JTextField(); 
        pnlCampos.add(txtVehiculo);
        
        lblGruas = new JLabel("Grúas: ");
        pnlCampos.add(lblGruas);
        
        cboGruas = new JComboBox();
        pnlCampos.add(cboGruas);
        
        JPanel pnlOperarios = new JPanel();
        pnlOperarios.setBorder(new TitledBorder("Seleccione 1 o 2 operarios: "));
        pnlOperarios.setLayout(new GridLayout(1,2,3,3));
        pnlCentro.add(pnlOperarios);
        
        lblOperarios = new JLabel("Operarios: ");
        pnlOperarios.add(lblOperarios);
        
        lmModelo = new DefaultListModel();
        lstOperarios = new JList(lmModelo);
        lstOperarios.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ManejarSeleccionOperarios(e);
            }
            
        });
        spnBarrasDesplazamientoOperarios = new JScrollPane(lstOperarios);
        pnlOperarios.add(spnBarrasDesplazamientoOperarios);
        
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
                ManejadorClicAgregar(e);
            }
        });
        btnAgregar.setEnabled(false);
        pnlBotones.add(btnAgregar);
        
        btnCancelado = new JButton("Cancelar");
        btnCancelado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicCancelar(e);
            }
        });
        btnCancelado.setEnabled(false);
        pnlBotones.add(btnCancelado);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManejadorClicLimpiar(e);
            }
        });
        pnlBotones.add(btnLimpiar);
       
        pnlDescripcion = new JPanel();
        lblDescripcion = new JLabel("<html>Deje Vacio número de serie para agregar una nueva solicitud <br/> o un ingrese un número para buscar o Cancelar una existente.<html/>");
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        lblDescripcion.setFont(new Font("Dialog", 0, 9));
        pnlDescripcion.add(lblDescripcion);
        
        pnlContenedorBtnYdescr.add(pnlDescripcion);
        
        //------------------------------mensaje---------------------------------
        
        lblMensaje = new JLabel();
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        getContentPane().add(lblMensaje, BorderLayout.SOUTH);
        
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        
        setPreferredSize(new Dimension(600, 520));
        pack();
        setLocation(85,10);    
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaSolicitudes = null;
    }
    
    //-----------------------MANEJADORES DE EVENTOS----------------------------
    protected void ManejadorPerdidaFocusNumeroSerie(FocusEvent fe)
    {
        try 
        {       
            int numeroBuscado = 0;
            
            try 
            {
                //convierto la caja de texto a int para asegurar que si voy a la base sea por un número correcto
                numeroBuscado= Integer.parseInt(txtNumeroSerie.getText().trim());
                
                if(numeroBuscado != 0)
                    unaSolicitud = FabricaLogica.GetLogicaSolicitudServicio().BuscarSolicitudDeServicio(numeroBuscado);           
            } 
            catch (NumberFormatException ne)
            {
                Limpiar();
                btnAgregar.setEnabled(true);
            }
            
            //si la solicitud fue encontrada se dehabilita agregar y se muestran los datos para cancelarla si se desea
            if (unaSolicitud != null)
            {
                btnAgregar.setEnabled(false);
                btnCancelado.setEnabled(true);
                DeshabilitarCampos();
                MostrarDatos();
            }
            else
            {
                Limpiar();
                btnAgregar.setEnabled(true);
            }
               
            if (listaOperarios.isEmpty()) 
            {
                btnAgregar.setEnabled(false);
                lblMensaje.setText("No hay oprarios disponibles.");
            }
            
            if (listaGruas.isEmpty()) 
            {
                btnAgregar.setEnabled(false);
                lblMensaje.setText("No hay grúas disponibles.");
            }
        }    
        catch (Exception e) 
        {
            lblMensaje.setText(e.getMessage());
        }
    }
    
    protected void ManejadorClicAgregar(ActionEvent e)
    {
        try 
        {
            unaSolicitud = new SolicitudDeServicio();
            unaSolicitud.setNumeroSerie(1);
            unaSolicitud.setDireccion(txtDireccion.getText().trim());
            unaSolicitud.setFecha(new Date());
            unaSolicitud.setCancelacion(false);
              
            if (!(txtVehiculo.getText().isEmpty())) 
            {
                unVehiculo = FabricaLogica.GetLogicaVehiculo().Buscar(txtVehiculo.getText().trim());
 
                if (unVehiculo == null)
                { 
                    lblMensaje.setText("No se encontro un vehiculo con esa matricula.");
                }
                else
                {
                    unaSolicitud.setVehiculo(unVehiculo);
                    lblMensaje.setText("");
                }
            } 
            else
            {
                throw new Exception("Debe proporcionar la matricula de un vehiculo para la solicitud");
            }
     
            if (operarios.isEmpty()) 
            {
                 throw new Exception("Debe seleccionar 1 o 2 operarios para la solicitud");
            }
            else
            {
                unaSolicitud.setOperarios(operarios);
            }
            
            //compruebo que lo seleccionado no sea la primera opcion de "seleccione una grúa"
            if (cboGruas.getSelectedIndex() != 0) {
                unaSolicitud.setGrua(listaGruas.get(cboGruas.getSelectedIndex() -1));
                listaGruas.remove(cboGruas.getSelectedIndex()-1);
            }
            else
            {
                throw new Exception("Debe seleccionar una grúa para realizar la soliciutd");
            }
            
            FabricaLogica.GetLogicaSolicitudServicio().AltaSolicitudDeServicio(unaSolicitud);
            
            listaOperarios.clear();
            
            try 
            {
                //este try es para capturar el error que no hay operarios disponibles
                //y evitar que no se muestre el mensaje de agregador correcto
                Limpiar();
            } 
            catch (Exception exe) 
            {
                //no hace nada ERROR no hay mas operarios disponibles luego del ultimo alta
            }
            
            lblMensaje.setText("¡Solicitud agregada con exito!");

            txtNumeroSerie.requestFocus();
        } 
        catch (Exception ex) 
        {
            //RECARGO LAS GRUAS PARA EVITAR QUE EN CASO DE ERRAR LAS ELIMINADAS EN EL MODELO VUELVAN A APARECER
            try 
            {
                CargarGruas();
            }
            catch (Exception er){}
            lblMensaje.setText(ex.getMessage());
        }
    }
     
    protected void ManejadorClicCancelar(ActionEvent e)
    {
         try 
        {
            if (JOptionPane.showConfirmDialog(null, "¿Esta seguro de cancelar esta solicitud?", "¡Advertencia!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
            {
                FabricaLogica.GetLogicaSolicitudServicio().CancelarSolicitudDeServicio(unaSolicitud);
            
                lblMensaje.setText("¡Solicitud Cancelada con exito!");
            }
            
            Limpiar();
            txtNumeroSerie.requestFocus();
        } 
        catch (Exception ex) 
        {
            lblMensaje.setText(ex.getMessage());
        }
    }
    
    protected void ManejadorClicLimpiar(ActionEvent e) 
    {
        try 
        {     
            lblMensaje.setText("");
            txtNumeroSerie.requestFocus();
            Limpiar();
        } 
        catch (Exception ex) 
        {
            lblMensaje.setText(ex.getMessage());
        }    
    }
    
    protected void ManejarSeleccionOperarios(ListSelectionEvent e) 
    {
        try
        {
            int[] indices = lstOperarios.getSelectedIndices();
            
            operarios = new ArrayList<>();
            
            if (indices.length > 2)
            {
                lstOperarios.clearSelection();
                throw new Exception("Solo Puede seleccionar 2 operarios");
            }

            for (int i = 0; i < indices.length; i++) 
            {    
                Operario ope = listaOperarios.get(indices[i]);
                operarios.add(ope);
                lmModelo.removeElement(ope);
            }

        }
        catch (Exception ex)
        {
            lblMensaje.setText(ex.getMessage());
        }
        
    }
    
    //-----------------------MANTENIMIENTO DE DATOS Y CAMPOS-BOTONES----------------------------
    
    protected void CargarGruas() throws Exception
    {
        listaGruas = FabricaLogica.GetLogicaGrua().ListarGruas();
        
        //pregunto si esta vacia para asegurarme que hay gruas disponibles en la BD
        if (listaGruas.isEmpty()|| listaGruas == null) 
        {
            cboGruas.removeAllItems();
            cboGruas.addItem("No hay grúas disponibles");
        }
        else
        {
            //si hay gruas disponibles en BD se cargan y se selececiona la opcion por defecto
            cboGruas.removeAllItems();
            cboGruas.addItem("Seleccione una grúa");

            for (Grua g : listaGruas) 
            {
                cboGruas.addItem(g.getNumero());
            }

            cboGruas.setSelectedIndex(0);
        }     
    }
    
    protected void CargarOperarios() throws Exception
    {
        listaOperarios = FabricaLogica.GetLogicaOperario().ListarOperarios();
        lmModelo.removeAllElements();
        lstOperarios.removeAll();  

        if (!(listaOperarios.isEmpty())) 
        {
            lmModelo.removeAllElements();

            for (Operario o : listaOperarios) 
            {
                lmModelo.addElement(o.getNombre());
            }
        }
        else
        {
            lstOperarios.removeAll(); 
            btnAgregar.setEnabled(false);
        }
    }
    
    protected void MostrarDatos()
    {
        lblFechaServicio.setText(formateador.format(unaSolicitud.getFecha()));
        txtDireccion.setText(unaSolicitud.getDireccion());
        txtVehiculo.setText(unaSolicitud.getVehiculo().getMatricula());
        cboGruas.removeAllItems();
        cboGruas.addItem(unaSolicitud.getGrua().getNumero());  
        lmModelo.removeAllElements();
        lmModelo.addElement(unaSolicitud.getOperarios().get(0).getNombre());
        
        //COMPROBACION PARA EVITAR QUE SE PRODUZCA UN ERROR SI LA SOLICITUD SOLO TIENE UN OPERARIO ENCARGADO
        if (unaSolicitud.getOperarios().size() > 1) 
        {
            lmModelo.addElement(unaSolicitud.getOperarios().get(1).getNombre());
        }
        
    }
    
    protected void DeshabilitarCampos()
    {
        txtNumeroSerie.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtVehiculo.setEnabled(false);
        cboGruas.setEnabled(false);
        lstOperarios.setEnabled(false);
    }
    
    protected void HabilitarCampos()
    {
        txtNumeroSerie.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtVehiculo.setEnabled(true);
        cboGruas.setEnabled(true);
        lstOperarios.setEnabled(true);
    }
    
    protected void Limpiar()throws Exception
    {
        txtNumeroSerie.setText("");
        lblFechaServicio.setText(formateador.format(new Date()));
        txtDireccion.setText("");
        txtVehiculo.setText("");
        HabilitarCampos();
        CargarOperarios();
        CargarGruas();
        btnAgregar.setEnabled(false);
        btnCancelado.setEnabled(false);
        unaSolicitud = null;
        unVehiculo = null;
    }

}
