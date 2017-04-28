package Persistencia;

import EntidadesCompartidas.Operario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

class PersistenciaOperario implements IPersistenciaOperario
{
    private static PersistenciaOperario instancia = null;
    private PersistenciaOperario(){}
    public static PersistenciaOperario GetInstancia()
    {
        if (instancia == null)
        {
            instancia = new PersistenciaOperario();
        }
        return instancia;
    }
    
    private Connection conexion = null;
    
    @Override
    public void AltaOperario(Operario pOperario) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL AltaOperario(?, ?, ?, ?)}");
            consulta.setString(1, pOperario.getIdEmpleado());
            consulta.setString(2, pOperario.getNombre());
            //le doy el formato correcto que acepta la base de datos mysql
            DateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
            consulta.setString(3,  formato.format(pOperario.getFechaIngreso()));
            consulta.registerOutParameter(4, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(4);

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
    public void BajaOperario(Operario pOperario)throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL BajaOperario(?, ?)}");
            consulta.setString(1, pOperario.getIdEmpleado());
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
    public Operario BuscarOperario(String pNumOperario)throws Exception
    {
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        Operario unOperario = null;
                
        try 
        {
            conexion = Conexion.getConexion();
            consulta = conexion.prepareStatement("SELECT * FROM Operarios WHERE IdEmpleado = ?");
            consulta.setString(1, pNumOperario);

            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                resultado.beforeFirst();
                
                unOperario = new Operario();
 
                while (resultado.next()) 
                {
                    unOperario.setIdEmpleado(pNumOperario);
                    unOperario.setNombre(resultado.getString("Nombre"));
                    unOperario.setFechaIngreso(resultado.getDate("FechaIngreso"));   
                } 
            } 
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al buscar el operario.");
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
        return unOperario;
    }
    
    @Override
    public ArrayList<Operario> ListarOperarios() throws Exception
    {
        CallableStatement consulta = null;
        ResultSet resultado = null;
        ArrayList<Operario> ListaOperarios = new ArrayList<>();
        Operario unOperario = null;
  
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL ListarOperariosDisponibles()}");
            
            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                resultado.beforeFirst();
                
                while (resultado.next()) 
                {
                    ListaOperarios.add(new Operario(resultado.getString("IdEmpleado"), resultado.getString("Nombre"), resultado.getDate("FechaIngreso")));
                } 
            } 
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al listar operarios Disponibles");
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
        return ListaOperarios;
    }
}
