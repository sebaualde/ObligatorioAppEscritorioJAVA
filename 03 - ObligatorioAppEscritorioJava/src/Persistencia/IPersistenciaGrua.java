/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import EntidadesCompartidas.Grua;
import java.util.ArrayList;

/**
 *
 * @author S.U.R
 */
public interface IPersistenciaGrua 
{
    void AltaGrua(Grua pGrua) throws Exception;
    void BajaGrua(Grua pGrua)throws Exception;
    Grua BuscarGrua(int pNumGrua)throws Exception;
    ArrayList<Grua> ListarGruas()throws Exception;
}
