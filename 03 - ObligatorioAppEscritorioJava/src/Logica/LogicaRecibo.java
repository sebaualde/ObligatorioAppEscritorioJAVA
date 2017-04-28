package Logica;
import EntidadesCompartidas.Recibo;
import java.util.ArrayList;

class LogicaRecibo implements ILogicaRecibo{
    
    private static LogicaRecibo _instancia = null;
    private LogicaRecibo(){}
    public static LogicaRecibo GetInstancia()
    {
        if(_instancia == null)
        {
            _instancia = new LogicaRecibo();
        }
        return _instancia;
    }
    
    @Override
    public void GenerearRecibos(double mensualidad) throws Exception{
        Persistencia.FabricaPersistencia.getPersistenciaRecibo().GenerearRecibos(mensualidad);
    }
    
    @Override
    public ArrayList<Recibo> ListarRecibosDelMes(int mes, int anio) throws Exception{
        return Persistencia.FabricaPersistencia.getPersistenciaRecibo().ListarRecibosDelMes(mes, anio);
    }
}
