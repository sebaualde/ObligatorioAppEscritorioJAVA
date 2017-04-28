/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import EntidadesCompartidas.Grua;
import java.util.ArrayList;

public interface ILogicaGrua 
{
    Grua BuscarGrua(int pNumGrua) throws Exception;
    void AltaGrua(Grua pGrua) throws Exception;
    void BajaGrua(Grua pGrua) throws Exception;
    ArrayList<Grua> ListarGruas()throws Exception;
}
