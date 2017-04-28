
package Logica;

public class FabricaLogica 
{
    public static ILogicaGrua GetLogicaGrua()
    {
        return (LogicaGrua.GetInstancia());
    }
    
    public static ILogicaOperario GetLogicaOperario()
    {
        return (LogicaOperario.GetInstancia());
    }
    
    public static ILogicaSolicitudServicio GetLogicaSolicitudServicio()
    {
        return (LogicaSolicitudServicio.GetInstancia());
    }
    
    public static ILogicaServicio GetLogicaServicio()
    {
        return (LogicaServicio.GetInstanciaLogicaServicio());
    }
    
    public static ILogicaCliente GetLogicaCliente()
    {
        return (LogicaCliente.GetInstancia());
    }
    public static ILogicaVehiculo GetLogicaVehiculo()
    {
        return (LogicaVehiculo.GetInstancia());
    }
    public static ILogicaRecibo GetLogicaRecibo()
    {
        return (LogicaRecibo.GetInstancia());
    }
}
