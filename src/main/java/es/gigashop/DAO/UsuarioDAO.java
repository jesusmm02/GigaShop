package es.gigashop.DAO;

import es.gigashop.beans.Usuario;
import es.gigashop.models.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        Boolean emailRepetido = Boolean.FALSE;
        Connection connection = null;
        PreparedStatement preparada = null;
        String sql = "INSERT INTO usuarios (email,password,nombre,apellidos,nif,telefono,direccion,codigoPostal,localidad,provincia,ultimoAcceso) "
                + "VALUES (?,md5(?),?,?,?,?,?,?,?,?,?)";
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
            preparada.setTimestamp(11, usuario.getUltimoAcceso());

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
    public boolean emailDisponible(String email) {
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE email = ?";
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Disponible si no hay coincidencias
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si hay error, asumimos no disponible
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(
                "UPDATE usuarios SET nombre = ?, apellidos = ?, telefono = ?, direccion = ?, "
                + "codigoPostal = ?, provincia = ?, localidad = ?, password = ? WHERE email = ?")) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getDireccion());
            ps.setString(5, usuario.getCodigoPostal());
            ps.setString(6, usuario.getProvincia());
            ps.setString(7, usuario.getLocalidad());
            ps.setString(8, usuario.getPassword());
            ps.setString(9, usuario.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean validarPassword(String email, String passwordIngresada) {
        boolean esValida = false;
        String sql = "SELECT password FROM usuarios WHERE email = ?";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Recuperar la contraseña almacenada
                    String passwordAlmacenada = rs.getString("password");

                    // Hashear la contraseña ingresada
                    String hashedPasswordIngresada = Utils.hashMD5(passwordIngresada);

                    // Comparar los hashes
                    esValida = hashedPasswordIngresada.equals(passwordAlmacenada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esValida;
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getShort("idUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setDireccion(rs.getString("direccion"));
                    usuario.setCodigoPostal(rs.getString("codigoPostal"));
                    usuario.setProvincia(rs.getString("provincia"));
                    usuario.setLocalidad(rs.getString("localidad"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
