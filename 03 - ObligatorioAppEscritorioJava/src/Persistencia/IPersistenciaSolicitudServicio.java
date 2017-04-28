
package Persistencia;

import EntidadesCompartidas.SolicitudDeServicio;
import java.util.ArrayList;

public interface IPersistenciaSolicitudServicio 
{
    void AltaSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception;
    void CancelarSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception;
    SolicitudDeServicio BuscarSolicitudDeServicio(int pNumSerie) throws Exception;
    ArrayList<SolicitudDeServicio> ListarSolicitudesPendientes() throws Exception;
    ArrayList<SolicitudDeServicio> ListarSolicitudesCanceladas() throws Exception;
}
