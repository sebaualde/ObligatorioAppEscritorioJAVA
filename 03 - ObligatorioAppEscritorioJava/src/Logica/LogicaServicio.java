package Logica;

import EntidadesCompartidas.AuxilioMecanico;
import EntidadesCompartidas.Remolque;
import EntidadesCompartidas.Servicio;
import Persistencia.FabricaPersistencia;
import java.util.ArrayList;

class LogicaServicio implements ILogicaServicio{
    
    private static LogicaServicio instancia = null;
    private LogicaServicio(){}
    public static LogicaServicio GetInstanciaLogicaServicio()
    {
        if (instancia == null) 
        {
            instancia = new LogicaServicio();
        }
        return instancia;
    }

    private ArrayList<Double> Tarifas = new ArrayList<>();
    
    @Override
    public void AltaServicio(Servicio pServicio, ArrayList<Double> pTarifas) throws Exception
    {
        Tarifas = pTarifas;
        
        if (pServicio instanceof Remolque)
        {
            ValidarRemolque((Remolque) pServicio);
            CostoTotal(pServicio);
            FabricaPersistencia.getPersistenciaRemolque().AltaServicioDeRemolque((Remolque)pServicio);
        }

        else if (pServicio instanceof AuxilioMecanico)
        {
            ValidarAuxilioMecanico((AuxilioMecanico)pServicio);
            CostoTotal(pServicio);
            FabricaPersistencia.getPersistenciaAuxilioMecanico().AltaServicioDeAuxilioMecanico((AuxilioMecanico)pServicio);
        }

        else
                throw new Exception("El tipo de servicio no es valido");
    }
    
    private void CostoTotal(Servicio pServicio) throws Exception
    {
        try 
        {
            if (pServicio instanceof Remolque)
            {
                if(((Remolque)pServicio).getCantidadKm() > 0 && ((Remolque)pServicio).getCantidadKm() <= 50)
                {
                    if (!(FabricaPersistencia.getPersistenciaRemolque().ServicioGratis(pServicio)))
                        pServicio.setImporteTotal(Tarifas.get(2));
                }
                else if(((Remolque)pServicio).getCantidadKm() > 50 && ((Remolque)pServicio).getCantidadKm() <= 200)
                {
                    pServicio.setImporteTotal(Tarifas.get(3));
                }
                else if(((Remolque)pServicio).getCantidadKm() > 200 && ((Remolque)pServicio).getCantidadKm() <= 500)
                {
                    pServicio.setImporteTotal(Tarifas.get(4));
                }        
                else if(((Remolque)pServicio).getCantidadKm() > 500 && ((Remolque)pServicio).getCantidadKm() <= 1000)
                {
                    pServicio.setImporteTotal(Tarifas.get(5));
                }
                else
                {
                    throw new Exception("Cantidad de Kms no valida");
                }
            }

            else if (pServicio instanceof AuxilioMecanico)
            {
                if (!(FabricaPersistencia.getPersistenciaRemolque().ServicioGratis(pServicio)))
                    pServicio.setImporteTotal((Tarifas.get(1)) + (((AuxilioMecanico)pServicio).getCostoRepuesto())); 
                else
                    pServicio.setImporteTotal(((AuxilioMecanico)pServicio).getCostoRepuesto());

            }

            else
                throw new Exception("El tipo de servicio no es valido");
        } 
        catch (Exception e) 
        {
            throw new Exception(e.getMessage());
        }
    }
                 
    private static void ValidarAuxilioMecanico(AuxilioMecanico pServicio) throws Exception
    {
        if (pServicio == null) 
        {
            throw new Exception("La solicitud es nula.");
        }

        if (pServicio.getDescripcionProblema().isEmpty() || pServicio.getDescripcionProblema() == null) 
        {
            throw new Exception("La descripcion del problema no puede quedar vacia.");
        }
        
        if (pServicio.getDescripcionReparacion().isEmpty() || pServicio.getDescripcionReparacion() == null) 
        {
            throw new Exception("La descripcion de la reparacian no puede quedar vacia.");
        }
        
        if (pServicio.getImporteTotal() < 0) 
        {
            throw new Exception("El importe total debe ser mayor o igual a 0");
        }
    }
    
    private static void ValidarRemolque(Remolque pServicio) throws Exception
    {
        if (pServicio == null) 
        {
            throw new Exception("La solicitud es nula.");
        }

        if (pServicio.getCantidadKm() <= 0 || pServicio.getCantidadKm() > 1000) 
        {
            throw new Exception("La cantidad de Kms debe ser mayor que 0 y menor o igual que 1000");
        }
    }
    
}
