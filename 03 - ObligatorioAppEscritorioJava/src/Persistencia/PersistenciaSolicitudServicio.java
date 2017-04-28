
package Persistencia;

import EntidadesCompartidas.SolicitudDeServicio;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class PersistenciaSolicitudServicio implements IPersistenciaSolicitudServicio 
{
    private static PersistenciaSolicitudServicio instancia = null;
    private PersistenciaSolicitudServicio(){}
    public static PersistenciaSolicitudServicio GetInstancia()
    {
        if (instancia == null) 
        {
            instancia = new PersistenciaSolicitudServicio();
        }
        return instancia;
    }
    
    private Connection conexion = null;
    
    @Override
    public void AltaSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();

            consulta = conexion.prepareCall("{CALL AgregarSolicitudServicio(?,?,?,?,?,?)}");
            consulta.setString(1, pSolicitud.getDireccion());
            consulta.setString(2, pSolicitud.getVehiculo().getMatricula());
            consulta.setString(3, pSolicitud.getOperarios().get(0).getIdEmpleado());
            if (pSolicitud.getOperarios().size() >  1) {
                consulta.setString(4, pSolicitud.getOperarios().get(1).getIdEmpleado());
            }
            else
            {
                consulta.setString(4, "");
            }
            consulta.setInt(5, pSolicitud.getGrua().getNumero());
            consulta.registerOutParameter(6, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(6);
            
            if (error != null) 
            {
                throw new Exception(error);
            }
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
    public void CancelarSolicitudDeServicio(SolicitudDeServicio pSolicitud) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();

            consulta = conexion.prepareCall("{CALL CancelacionSolicitudServicio(?, ?)}");
            consulta.setInt(1, pSolicitud.getNumeroSerie());
            consulta.registerOutParameter(2, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(2);
            
            if (error != null) 
            {
                throw new Exception(error);
            }
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
    public SolicitudDeServicio BuscarSolicitudDeServicio(int pNumSerie) throws Exception
    {
        CallableStatement consulta = null;
        ResultSet resultado = null;
        
        SolicitudDeServicio unaSolicitud = null;
        ArrayList operarios = new  ArrayList();

        try 
        {
            conexion = Conexion.getConexion();

            consulta = conexion.prepareCall("{CALL BuscarSolicitudDeServicio(?)}");
            consulta.setInt(1, pNumSerie);
            
            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                resultado.beforeFirst();
  
                while (resultado.next()) 
                {
                    //pregunto si es null porque si no lo es, quiere decir que la solicitud tenia 2 operarios asignados
                    if(unaSolicitud == null)
                    {
                        unaSolicitud = new SolicitudDeServicio();
                        
                        unaSolicitud.setNumeroSerie(pNumSerie);
                        unaSolicitud.setFecha(resultado.getDate("Fecha"));
                        unaSolicitud.setDireccion(resultado.getString("Direccion"));
                        unaSolicitud.setCancelacion(resultado.getBoolean("Cancelado"));
                        unaSolicitud.setGrua(PersistenciaGrua.GetInstancia().BuscarGrua(resultado.getInt("NroGrua")));
                        unaSolicitud.setVehiculo(PersistenciaVehiculo.getInstanciaPersVehiculo().Buscar(resultado.getString("Matricula")));
                        operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado")));
   
                    }
                    else
                    {
                        operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado")));
                    }      
                }   
            }   
            if(unaSolicitud != null)
            {
                unaSolicitud.setOperarios(operarios);
            }
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al buscar la solicitud.");
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
        
        return unaSolicitud;
    }
    
    @Override
    public ArrayList<SolicitudDeServicio> ListarSolicitudesPendientes() throws Exception
    {
        Statement consulta = null;
        ResultSet resultado = null;
        
        ArrayList<SolicitudDeServicio> ListaSolicitudes = new ArrayList<>();
        SolicitudDeServicio unaSolicitud = new SolicitudDeServicio();
        ArrayList operarios = new  ArrayList();
  
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.createStatement();
            
            resultado = consulta.executeQuery("SELECT * FROM SolicitudesDeServicios INNER JOIN OperariosAsignadosASolicitudes ON NumSolicitud = NumSerie WHERE Cancelado = 0 AND NOT EXISTS(SELECT * FROM Servicios WHERE NumSerie = NumSolicitud) ORDER BY Fecha;");

            while (resultado.next()) 
            {
                if (unaSolicitud.getNumeroSerie() != resultado.getInt("NumSerie")) 
                {
                    unaSolicitud = new SolicitudDeServicio();
                    operarios = new  ArrayList();
                    
                    unaSolicitud.setNumeroSerie(resultado.getInt("NumSerie"));
                    unaSolicitud.setFecha(resultado.getDate("Fecha"));
                    unaSolicitud.setDireccion(resultado.getString("Direccion"));
                    unaSolicitud.setCancelacion(resultado.getBoolean("Cancelado"));
                    unaSolicitud.setGrua(PersistenciaGrua.GetInstancia().BuscarGrua(resultado.getInt("NroGrua")));
                    unaSolicitud.setVehiculo(PersistenciaVehiculo.getInstanciaPersVehiculo().Buscar(resultado.getString("Matricula")));
                    
                    operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado")));
                    unaSolicitud.setOperarios(operarios);
                    ListaSolicitudes.add(unaSolicitud);
                }
                else
                {                    
                    operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado"))); 
                    unaSolicitud.setOperarios(operarios);
                } 
            }   
            
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al listar las Solicitudes pendientes.");
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
        return ListaSolicitudes;
    }
    
    @Override
    public ArrayList<SolicitudDeServicio> ListarSolicitudesCanceladas() throws Exception
    {
        Statement consulta = null;
        ResultSet resultado = null;
        
        ArrayList<SolicitudDeServicio> ListaSolicitudes = new ArrayList<>();
        SolicitudDeServicio unaSolicitud = new SolicitudDeServicio();
        ArrayList operarios = new  ArrayList();

        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.createStatement();
            
            resultado = consulta.executeQuery("SELECT * FROM SolicitudesDeServicios INNER JOIN OperariosAsignadosASolicitudes ON NumSolicitud = NumSerie WHERE Cancelado = 1 ORDER BY Fecha;");

            while (resultado.next()) 
            {
                if (unaSolicitud.getNumeroSerie() != resultado.getInt("NumSerie")) 
                {
                    unaSolicitud = new SolicitudDeServicio();
                    operarios = new  ArrayList();
                    
                    unaSolicitud.setNumeroSerie(resultado.getInt("NumSerie"));
                    unaSolicitud.setFecha(resultado.getDate("Fecha"));
                    unaSolicitud.setDireccion(resultado.getString("Direccion"));
                    unaSolicitud.setCancelacion(resultado.getBoolean("Cancelado"));
                    unaSolicitud.setGrua(PersistenciaGrua.GetInstancia().BuscarGrua(resultado.getInt("NroGrua")));
                    unaSolicitud.setVehiculo(PersistenciaVehiculo.getInstanciaPersVehiculo().Buscar(resultado.getString("Matricula")));
                    
                    operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado")));
                    unaSolicitud.setOperarios(operarios);
                    ListaSolicitudes.add(unaSolicitud);
                }
                else
                {
                    operarios.add(PersistenciaOperario.GetInstancia().BuscarOperario(resultado.getString("IdEmpleado"))); 
                    unaSolicitud.setOperarios(operarios);
                } 
            }           
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al listar las Solicitudes canceladas.");
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
        return ListaSolicitudes;
    }
}
