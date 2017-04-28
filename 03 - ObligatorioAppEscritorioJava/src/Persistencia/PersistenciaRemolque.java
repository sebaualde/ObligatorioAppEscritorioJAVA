package Persistencia;

import EntidadesCompartidas.Remolque;
import EntidadesCompartidas.Servicio;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class PersistenciaRemolque implements IPersistenciaRemolque {
    
    private static PersistenciaRemolque instancia = null;
    private PersistenciaRemolque(){}
    public static PersistenciaRemolque getInstanciaPersRemolque(){
        if(instancia != null){
            return instancia;
        }
        else{
            return new PersistenciaRemolque();
        }
    }
    private Connection conexion = null;
    
    @Override
    public void AltaServicioDeRemolque(Remolque pRemolque) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();

            consulta = conexion.prepareCall("{CALL AltaServicioRemolque(?,?,?,?)}");
            consulta.setDouble(1, pRemolque.getImporteTotal());
            consulta.setInt(2, pRemolque.getUnaSolicitud().getNumeroSerie());
            consulta.setInt(3, pRemolque.getCantidadKm());
            consulta.registerOutParameter(4, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(4);
            
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
    
    @Override
    public boolean ServicioGratis(Servicio pServicio)throws Exception
    {
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        boolean unServicioGratis = true;
                
        try 
        {
            conexion = Conexion.getConexion();
            consulta = conexion.prepareStatement("SELECT * FROM Servicios INNER JOIN SolicitudesDeServicios ON NumSolicitud = NumSerie INNER JOIN Vehiculos ON SolicitudesDeServicios.Matricula = Vehiculos.Matricula WHERE CedulaCliente = ? AND MONTH(SolicitudesDeServicios.Fecha) = MONTH(NOW()) AND YEAR(SolicitudesDeServicios.Fecha) = YEAR(NOW())");
            
            consulta.setLong(1, pServicio.getUnaSolicitud().getVehiculo().getPropietario().getCedula());
            
            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                unServicioGratis =  false;
            } 
        }
        finally 
        {
            try 
            {
                if (resultado != null) 
                {
                    resultado.close();
                }

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
                throw new Exception("¡ERROR! Ocurrio un error cerrar los recursos.");
            }  
        }
        return unServicioGratis;
    }
}
