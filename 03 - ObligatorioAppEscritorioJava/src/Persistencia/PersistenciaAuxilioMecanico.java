package Persistencia;

import EntidadesCompartidas.AuxilioMecanico;
import java.sql.CallableStatement;
import java.sql.Connection;

class PersistenciaAuxilioMecanico implements IPersistenciaAuxilioMecanico{
    
    private static PersistenciaAuxilioMecanico instancia = null;
    private PersistenciaAuxilioMecanico(){}
    public static PersistenciaAuxilioMecanico getInstanciaPersAuxilioMecanico(){
        if(instancia != null){
            return instancia;
        }
        else{
            return new PersistenciaAuxilioMecanico();
        }
    }
    private Connection conexion = null;
    
    @Override
    public void AltaServicioDeAuxilioMecanico(AuxilioMecanico pAuxilioMecanico) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();

            consulta = conexion.prepareCall("{CALL AltaServicioAuxilioMecanico(?,?,?,?,?,?)}");
            consulta.setDouble(1, pAuxilioMecanico.getImporteTotal());
            consulta.setInt(2, pAuxilioMecanico.getUnaSolicitud().getNumeroSerie());
            consulta.setString(3, pAuxilioMecanico.getDescripcionProblema());
            consulta.setString(4, pAuxilioMecanico.getDescripcionReparacion());
            consulta.setDouble(5, pAuxilioMecanico.getCostoRepuesto());
            consulta.registerOutParameter(6, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(6);
            
            if (error != null) 
            {
                throw new Exception(error);
            }
        } 
        catch (Exception e) 
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            try 
            {
                if (consulta != null) 
                {
                    consulta.close();
                }
                
                if (conexion != null) 
                {
                    conexion.close();
                }
            } 
            catch (Exception e)
            {
                throw new Exception(e.getMessage());
            }
        }
    }
}
