package es.gigashop.DAO;

import es.gigashop.beans.Usuario;

public interface IUsuarioDAO {
    
    /**
     * Crea un nuevo usuario en la base de datos
     * @param usuario objeto de Usuario que se va a añadir a la base de datos
     * @return usuario registrado
     */
    public boolean registrarUsuario(Usuario usuario);
    
    public boolean emailDisponible(String email);
    
    /**
     * Actualiza el usuario que esté en ese momento en sesión
     * @param usuario objeto Usuario
     * @return 
     */
    public boolean actualizarUsuario(Usuario usuario); 
    
    /**
     * 
     * @param email
     * @param passwordIngresada
     * @return 
     */
    public boolean validarPassword(String email, String passwordIngresada);
    
    /**
     * 
     * @param email
     * @return 
     */
    public Usuario obtenerUsuarioPorEmail(String email);
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();

    
    
}
