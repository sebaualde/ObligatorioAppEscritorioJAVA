/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import EntidadesCompartidas.Operario;
import java.util.ArrayList;

/**
 *
 * @author S.U.R
 */
public interface ILogicaOperario 
{
    void AltaOperario(Operario pOperario) throws Exception;
    void BajaOperario(Operario pOperario)throws Exception;
    Operario BuscarOperario(String pNumOperario)throws Exception;
    ArrayList<Operario> ListarOperarios() throws Exception; 
}
