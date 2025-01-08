package es.gigashop.DAO;

import es.gigashop.beans.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Boolean registrarUsuario(Usuario usuario) {
        Boolean emailRepetido = Boolean.FALSE;
        Connection connection = null;
        PreparedStatement preparada = null;
        String sql = "INSERT INTO usuarios (email,password,nombre,apellidos,nif,telefono,direccion,codigoPostal,localidad,provincia) "
                + "VALUES (?,md5(?),?,?,?,?,?,?,?,?)";
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, usuario.getEmail());
            preparada.setString(2, usuario.getPassword());
            preparada.setString(3, usuario.getNombre());
            preparada.setString(4, usuario.getApellidos());
            preparada.setString(5, usuario.getNif());
            preparada.setString(6, usuario.getTelefono());
            preparada.setString(7, usuario.getDireccion());
            preparada.setString(8, usuario.getCodigoPostal());
            preparada.setString(9, usuario.getLocalidad());
            preparada.setString(10, usuario.getProvincia());
            
            preparada.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                /*
                * Si existe el registro quiere decir que se intenta almacenar un registro con email duplicado
                 */
                emailRepetido = Boolean.TRUE;
            } else {

                // Existe un error al intentar insertar un registro. Escribimos el logger y se visualiza error500.jsp
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            this.closeConnection();
        }
        return emailRepetido;
    }
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
    
}
