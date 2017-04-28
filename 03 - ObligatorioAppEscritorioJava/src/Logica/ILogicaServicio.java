package Logica;

import EntidadesCompartidas.Servicio;
import java.util.ArrayList;

public interface ILogicaServicio {
    void AltaServicio(Servicio pServicio, ArrayList<Double> pTarifas) throws Exception;
}
