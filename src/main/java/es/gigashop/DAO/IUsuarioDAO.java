package es.gigashop.DAO;

import es.gigashop.beans.Usuario;

public interface IUsuarioDAO {
    
    /**
     * Crea un nuevo usuario en la base de datos
     * @param usuario objeto de Usuario que se va a añadir a la base de datos
     * @return usuario registrado
     */
    public Boolean registrarUsuario(Usuario usuario);
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
