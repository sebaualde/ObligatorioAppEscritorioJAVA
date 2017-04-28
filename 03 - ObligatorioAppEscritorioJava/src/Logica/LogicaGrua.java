
package Logica;

import EntidadesCompartidas.Grua;
import Persistencia.FabricaPersistencia;
import java.util.ArrayList;

class LogicaGrua implements ILogicaGrua
{
    private static LogicaGrua _instancia = null;
    private LogicaGrua(){}
    public static LogicaGrua GetInstancia()
    {
        if(_instancia == null)
        {
            _instancia = new LogicaGrua();
        }
        return _instancia;
    }
    
    @Override
    public void AltaGrua(Grua pGrua) throws Exception
    {
        ValidarGrua(pGrua);
        
        FabricaPersistencia.GetPersistenciaGrua().AltaGrua(pGrua);
    }
    
    @Override
    public void BajaGrua(Grua pGrua) throws Exception
    {
         FabricaPersistencia.GetPersistenciaGrua().BajaGrua(pGrua);
    }
    
    @Override
    public Grua BuscarGrua(int pNumGrua) throws Exception
    {
         return (FabricaPersistencia.GetPersistenciaGrua().BuscarGrua(pNumGrua));
    }
    
    @Override
    public ArrayList<Grua> ListarGruas()throws Exception
    {
         return (FabricaPersistencia.GetPersistenciaGrua().ListarGruas());
    }
    
    public static void ValidarGrua(Grua pGrua) throws Exception
    {
        if (pGrua == null) 
        {
            throw new Exception("La grúa es nula.");
        }
        
        if (pGrua.getNumero() < 0 || pGrua.getNumero() > 2147483647) 
        {
            throw new Exception("El número de la grúa no puede ser menor que cero ni mayor a 2147483647.");
        }
        
        if (pGrua.getPesoMaximoCarga() < 0 || pGrua.getPesoMaximoCarga() > 2147483647) 
        {
            throw new Exception("El peso máximo de carga no puede ser menor a cero ni mayor a 2147483647 kg.");
        }              
    }
}
