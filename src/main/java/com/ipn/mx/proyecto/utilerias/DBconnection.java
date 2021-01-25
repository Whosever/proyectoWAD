/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.utilerias;

import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alvaro Zúñiga
 */
@NoArgsConstructor
public class DBconnection {
    
    public Connection getConnection(){
        Connection con = null;
        String usr = "nbvsdayccarflw";
        String pwd = "1ac9b9b6d95dd4c1c8dcc2e55853f2346962bbdcd078b402509a64469847faeb";
        String driver = "org.postgresql.Driver";
        //String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:postgresql://ec2-34-230-231-71.compute-1.amazonaws.com:5432/d20nlhs27e80os?sslmode=require";
        //String url = "jdbc:mysql://localhost:3306/3CM4?serverTimezone=America/Mexico_City&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&useSSL=false";
        
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, usr, pwd);
        } catch (ClassNotFoundException | SQLException ex) {
        }
        return con;
    }
}
