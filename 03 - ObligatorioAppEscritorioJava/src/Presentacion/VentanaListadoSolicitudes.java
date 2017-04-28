
package Presentacion;

import EntidadesCompartidas.Operario;
import EntidadesCompartidas.SolicitudDeServicio;
import Logica.FabricaLogica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;


public class VentanaListadoSolicitudes extends JInternalFrame
{
    protected JTabbedPane jtpPestanias;
    protected JPanel pnlPendientes;
    protected JPanel pnlCancelados;
    protected JLabel lblTitulo;
    protected DefaultListModel lmModeloPendientes;
    protected JScrollPane spnBarrasDesplazamientoPendientes;
    protected JList lstPendientes;
    protected DefaultListModel lmModeloCancelados;
    protected JScrollPane spnBarrasDesplazamientoCancelados;
    protected JList lstCancelados;
    protected JLabel lblMensaje;
    
    private static VentanaListadoSolicitudes instancia;
    
    public static VentanaListadoSolicitudes getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaListadoSolicitudes(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;
    
    public VentanaListadoSolicitudes(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("Listados de solicitudes");
        
        lblTitulo = new JLabel("Listados de solicitudes");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        lblTitulo.setFont(new Font("Dialog", 1, 24));
        getContentPane().add(lblTitulo, BorderLayout.NORTH);
        
        jtpPestanias = new JTabbedPane();
        getContentPane().add(jtpPestanias, BorderLayout.CENTER);
        
        pnlPendientes = new JPanel();
        
        lmModeloPendientes = new DefaultListModel();
        lstPendientes = new JList(lmModeloPendientes);
        
        spnBarrasDesplazamientoPendientes = new JScrollPane(lstPendientes);
        spnBarrasDesplazamientoPendientes.setName("Pendientes");
        jtpPestanias.add(spnBarrasDesplazamientoPendientes);
        ListarSolcitudesPendientes();
        
        pnlCancelados = new JPanel();
        
        lmModeloCancelados = new DefaultListModel();
        lstCancelados = new JList(lmModeloCancelados);
        
        spnBarrasDesplazamientoCancelados = new JScrollPane(lstCancelados);
        spnBarrasDesplazamientoCancelados.setName("Canceladas");
        jtpPestanias.add(spnBarrasDesplazamientoCancelados);
        ListarSolcitudesCanceladas();
        
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
        ventanaPrincipal.ventanaListadoSolicitudes = null;
    }
    
    protected void ListarSolcitudesPendientes()
    {
        try 
        {
            ArrayList<SolicitudDeServicio> ListaSolicitudes = FabricaLogica.GetLogicaSolicitudServicio().ListarSolicitudesPendientes();

            lmModeloPendientes.removeAllElements();

            if (!(ListaSolicitudes.isEmpty())) 
            {
                for (SolicitudDeServicio s : ListaSolicitudes) 
                {
                    lmModeloPendientes.addElement(s.toString());
                }
            }
        } 
        catch (Exception e) 
        {
            lblMensaje.setText(e.getMessage());
        }
    }
    
    protected void ListarSolcitudesCanceladas()
    {
        try 
        {
            ArrayList<SolicitudDeServicio> ListaSolicitudes = FabricaLogica.GetLogicaSolicitudServicio().ListarSolicitudesCanceladas();

            lmModeloCancelados.removeAllElements();

            if (!(ListaSolicitudes.isEmpty())) 
            {
                for (SolicitudDeServicio s : ListaSolicitudes) {
                    lmModeloCancelados.addElement(s.toString());
                }
            }
        } 
        catch (Exception e) 
        {
            lblMensaje.setText(e.getMessage());
        }
    }
    
}
