/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Conexion 
{
    private static Connection _conexion = null;
    
    public static Connection getConexion() throws Exception
    {
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            _conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppEscritorioJava", "root", "teto2005");       
        } 
        catch (ClassNotFoundException | SQLException e) 
        {
            throw new Exception("Error al conectarse a la Base de Datos");
        }
        
        return _conexion;
    }
}
            
            
     
