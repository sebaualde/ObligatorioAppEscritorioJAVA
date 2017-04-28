/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import EntidadesCompartidas.SolicitudDeServicio;
import java.util.ArrayList;

public interface ILogicaSolicitudServicio {
    void AltaSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception;
    void CancelarSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception;
    SolicitudDeServicio BuscarSolicitudDeServicio(int pNumSerie) throws Exception;
    ArrayList<SolicitudDeServicio> ListarSolicitudesPendientes() throws Exception;
    ArrayList<SolicitudDeServicio> ListarSolicitudesCanceladas() throws Exception;
}
