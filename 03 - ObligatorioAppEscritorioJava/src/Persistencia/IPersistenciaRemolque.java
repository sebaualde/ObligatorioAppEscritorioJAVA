package Persistencia;
import EntidadesCompartidas.Remolque;
import EntidadesCompartidas.Servicio;

public interface IPersistenciaRemolque {
    void AltaServicioDeRemolque(Remolque pRemolque) throws Exception;
    boolean ServicioGratis(Servicio pServicio)throws Exception;
}
