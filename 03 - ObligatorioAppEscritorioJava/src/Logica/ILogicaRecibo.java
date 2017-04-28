/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import EntidadesCompartidas.Recibo;
import java.util.ArrayList;

/**
 *
 * @author sistemas
 */
public interface ILogicaRecibo {
     void GenerearRecibos(double mensualidad) throws Exception;
    ArrayList<Recibo> ListarRecibosDelMes(int mes, int año) throws Exception;
}
