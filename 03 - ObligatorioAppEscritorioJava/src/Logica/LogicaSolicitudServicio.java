
package Logica;

import EntidadesCompartidas.SolicitudDeServicio;
import Persistencia.FabricaPersistencia;
import java.util.ArrayList;

class LogicaSolicitudServicio implements ILogicaSolicitudServicio
{
    private static LogicaSolicitudServicio instancia = null;
    private LogicaSolicitudServicio(){}
    public static LogicaSolicitudServicio GetInstancia()
    {
        if (instancia == null) 
        {
            instancia = new LogicaSolicitudServicio();
        }
        return instancia;
    }
    
    @Override
    public void AltaSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception
    {
        ValidarSolicitud(pSolicitud);
        
        FabricaPersistencia.GetPersistenciaSolicitudServicio().AltaSolicitudDeServicio(pSolicitud);
    }
    
    @Override
    public void CancelarSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception
    {
        ValidarSolicitud(pSolicitud);
        
        FabricaPersistencia.GetPersistenciaSolicitudServicio().CancelarSolicitudDeServicio(pSolicitud);
    }
    
    @Override
    public SolicitudDeServicio BuscarSolicitudDeServicio(int pNumSerie) throws Exception
    {
        return FabricaPersistencia.GetPersistenciaSolicitudServicio().BuscarSolicitudDeServicio(pNumSerie);
    }
    
    @Override
    public ArrayList<SolicitudDeServicio> ListarSolicitudesPendientes() throws Exception
    {
        return (FabricaPersistencia.GetPersistenciaSolicitudServicio().ListarSolicitudesPendientes());
    }
    
    @Override
    public ArrayList<SolicitudDeServicio> ListarSolicitudesCanceladas() throws Exception
    {
        return (FabricaPersistencia.GetPersistenciaSolicitudServicio().ListarSolicitudesCanceladas());
    }
    
    public static void ValidarSolicitud(SolicitudDeServicio pSolicitud) throws Exception
    {
        if (pSolicitud == null) 
        {
            throw new Exception("La solicitud es nula.");
        }
        
        if (pSolicitud.getNumeroSerie() < 0) 
        {
            throw new Exception("La número de solicitud no puede ser menor que 0.");
        }
        
        if (pSolicitud.getDireccion().isEmpty() || pSolicitud.getDireccion() == null) 
        {
            throw new Exception("La dirección de solicitud no puede quedar vacia ni ser nula.");
        }
        
        if (pSolicitud.getVehiculo() == null) 
        {
            throw new Exception("Debe proporcionar un vheiculo para la solicitud.");
        }
        
        if (pSolicitud.getGrua() == null) 
        {
            throw new Exception("Debe proporcionar una grúa para relizar la solicitud.");            
        }
        
        if (pSolicitud.getOperarios().get(0).getIdEmpleado().isEmpty() || pSolicitud.getOperarios().get(0).getIdEmpleado() == null ||pSolicitud.getOperarios().get(0).getIdEmpleado() == "N/D") 
        {
            if (pSolicitud.getOperarios().get(1).getIdEmpleado().isEmpty() || pSolicitud.getOperarios().get(1).getIdEmpleado() == null ||pSolicitud.getOperarios().get(1).getIdEmpleado() == "N/D") 
            {
                throw new Exception("Debe proporcionar al menos un operario para relizar la solicitud.");     
            }
        }
    }
}
