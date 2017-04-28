/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

/**
 *
 * @author S.U.R
 */
public class FabricaPersistencia 
{
    public static IPersistenciaGrua GetPersistenciaGrua()
    {
        return (PersistenciaGrua.GetInstancia());
    }
    
    public static IPersistenciaOperario GetPersistenciaOperario()
    {
        return (PersistenciaOperario.GetInstancia());
    }
    
    public static IPersistenciaSolicitudServicio GetPersistenciaSolicitudServicio()
    {
        return (PersistenciaSolicitudServicio.GetInstancia());
    }
    
    public static IPersistenciaCliente GetPersistenciaCliente(){
        return PersistenciaCliente.getInstanciaPersCli();
    }
    
    public static IPersistenciaVehiculo GetIPersistenciaVehiculo()
    {
        return (PersistenciaVehiculo.getInstanciaPersVehiculo());
    }
    
    public static IPersistenciaAuxilioMecanico getPersistenciaAuxilioMecanico()
    {
        return (PersistenciaAuxilioMecanico.getInstanciaPersAuxilioMecanico());
    }
    
    public static IPersistenciaRemolque getPersistenciaRemolque()
    {
        return (PersistenciaRemolque.getInstanciaPersRemolque());
    }
    
    public static IPersistenciaRecibo getPersistenciaRecibo()
    {
        return (PersistenciaRecibo.getInstanciaPersRecibo());
    }
    
    
}
