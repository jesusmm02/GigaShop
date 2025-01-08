package es.gigashop.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ConnectionFactory {

    /*
    * Declaramos el pool de conexiones
     */
    static DataSource dataSource = null;
    static Connection conexion = null;
    static final String DATASOURCE_NAME = "java:comp/env/jdbc/GigaShop";

    public static Connection getConnection() {

        try {
            /*
            * Para buscar y acceder a un recurso defnido en el Servidor de Aplicaciones
            *   - Creamos el contexto de búsqueda mediante la clase InitialContext.
            *   - Realizamos la búsqueda del recurso haciendo el casting correspondiente con la sentecia lookup
             */
            Context contextoInicial = new InitialContext();
            dataSource = (DataSource) contextoInicial.lookup(DATASOURCE_NAME);
            conexion = dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            /*
            * Existe un error al intentar crear el pool de conexiones. Escribimos el logger y se visualiza error500.jsp
             */
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);

        }
        return conexion;
    }

    public static void closeConnection() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}