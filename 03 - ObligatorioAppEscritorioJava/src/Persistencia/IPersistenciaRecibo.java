/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;
import EntidadesCompartidas.Recibo;
import java.util.ArrayList;
/**
 *
 * @author dario
 */
public interface IPersistenciaRecibo {    
    void GenerearRecibos(double mensualidad) throws Exception;
    ArrayList<Recibo> ListarRecibosDelMes(int mes, int anio) throws Exception;
    
}
