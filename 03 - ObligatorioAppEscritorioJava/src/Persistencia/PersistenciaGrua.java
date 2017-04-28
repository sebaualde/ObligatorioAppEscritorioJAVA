package Persistencia;

import EntidadesCompartidas.Grua;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class PersistenciaGrua implements IPersistenciaGrua 
{
    private static PersistenciaGrua instancia = null;
    private PersistenciaGrua(){}
    public static PersistenciaGrua GetInstancia()
    {
        if (instancia == null)
        {
            instancia = new PersistenciaGrua();
        }
        return instancia;       
    }
    
    private Connection conexion = null;
    
    @Override
    public void AltaGrua(Grua pGrua) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL AltaGrua(?, ?, ?)}");
            consulta.setInt(1, pGrua.getNumero());
            consulta.setInt(2, pGrua.getPesoMaximoCarga());
            consulta.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            consulta.executeUpdate();
            
            String error = consulta.getString(3);

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
    public void BajaGrua(Grua pGrua) throws Exception
    {
        CallableStatement consulta = null;
        
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL BajaGrua(?, ?)}");
            consulta.setInt(1, pGrua.getNumero());
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
    public Grua BuscarGrua(int pNumGrua) throws Exception
    {
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        Grua unaGrua = null;
                
        try 
        {
            conexion = Conexion.getConexion();
            consulta = conexion.prepareStatement("SELECT * FROM Gruas WHERE Numero = ? AND Eliminado = 0");
            consulta.setInt(1, pNumGrua);
            
            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                resultado.beforeFirst();
                unaGrua = new Grua();
                
                while (resultado.next()) 
                {
                    unaGrua.setNumero(resultado.getInt("Numero"));
                    unaGrua.setPesoMaximoCarga(resultado.getInt("PesoMaxCarga"));   
                } 
            } 
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al buscar la grua.");
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
        
        return unaGrua;
        
    }
    
    @Override
    public ArrayList<Grua> ListarGruas() throws Exception
    {
        CallableStatement consulta = null;
        ResultSet resultado = null;
        ArrayList<Grua> ListaGruas = new ArrayList<>();
        Grua unaGrua = null;
  
        try 
        {
            conexion = Conexion.getConexion();
            
            consulta = conexion.prepareCall("{ CALL ListarGruasDisponibles()}");
            
            resultado = consulta.executeQuery();

            if (resultado.next()) 
            {
                resultado.beforeFirst();
                
                while (resultado.next()) 
                {
                    ListaGruas.add(new Grua(resultado.getInt("Numero"), resultado.getInt("PesoMaxCarga")));
                } 
            } 
            else
            {
                throw new Exception("No hay Grúas disponibles.");
            }
        } 
        catch (Exception ex) 
        {
            throw new Exception("Error al listar las grúas.");
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
        return ListaGruas;
    }
}
