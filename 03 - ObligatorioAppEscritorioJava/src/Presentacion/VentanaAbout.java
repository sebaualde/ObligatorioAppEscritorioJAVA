
package Presentacion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaAbout extends JInternalFrame 
{
    protected JPanel pnlCentro;
    protected ImageIcon iconImagen;
    protected Image imgLogo; 
    protected JLabel lblLogo;
    protected JLabel lblDescripcion;
    protected JEditorPane txtEspecificaciones;
    
    private static VentanaAbout instancia;
    
    public static VentanaAbout getInstancia(VentanaPrincipal ventanaPrincipal)
    {
        if (instancia == null) 
        {
            instancia = new VentanaAbout(ventanaPrincipal); 
        }
        
        return instancia;
    }
    
    protected VentanaPrincipal ventanaPrincipal;

    public VentanaAbout(VentanaPrincipal pVentanaPrincipal) 
    {
        ventanaPrincipal = pVentanaPrincipal;
        
        inicializarComponentes();
    }
    
    protected void inicializarComponentes()
    {
        setTitle("About");
        
        iconImagen = new ImageIcon(getClass().getResource("/Recursos/about.png"));
        imgLogo = iconImagen.getImage().getScaledInstance(375, 120, java.awt.Image.SCALE_SMOOTH);
        iconImagen = new ImageIcon(imgLogo);
        lblLogo = new JLabel(iconImagen);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        getContentPane().add(lblLogo, BorderLayout.NORTH);
        
        pnlCentro = new JPanel();
        getContentPane().add(pnlCentro, BorderLayout.CENTER);
        
        lblDescripcion = new JLabel("<html>El obligatorio de aplicaciónes de escritorio en java fue desarrollado<br/>  por los estudiantes Darío Stramil, Marcelo Mesa y Seabstian Ualde. <br/>Presentado para su corrección el día 27 de junio de 2016 por parte<br/> del docente Raúl Collazo.</html>");
        pnlCentro.add(lblDescripcion);
        
        txtEspecificaciones = new JEditorPane("text/html", "");
        txtEspecificaciones.setBorder(BorderFactory.createEmptyBorder(20,8,8,8));
        txtEspecificaciones.setText("<b>Lenguaje:</b> Java <br/><b>Comunicacion con BD mediante:</b> JDBC<br/><b>Interfaz:</b> Swing<br/> <b>Plataforma de desarrollo:</b> NetBeans 8.0.2<br/><b>Base De Datos:</b> MySql");
        txtEspecificaciones.setEditable(false);
        pnlCentro.add(txtEspecificaciones);

        setClosable(true);
        setMaximizable(false);
        setIconifiable(true);
        setResizable(false);
        
        setPreferredSize(new Dimension(400, 400));
        pack();
        setLocation(50,50);
    }
    
    @Override
    public void dispose()
    {
        instancia = null;
        ventanaPrincipal.ventanaAbout = null;
    }
}
