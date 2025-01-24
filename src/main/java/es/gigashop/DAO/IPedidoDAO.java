package es.gigashop.DAO;

import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
import java.sql.SQLException;
import java.util.List;


public interface IPedidoDAO {

    /**
     * 
     * @param pedido 
     */
    //public void guardarPedido(Pedido pedido);
    
    /**
     * 
     * @param idUsuario
     * @return 
     */
    public Pedido obtenerPedidoPorUsuario(Short idUsuario);
    
    /**
     * 
     * @param idPedido
     * @return 
     */
    public List<LineaPedido> obtenerLineasPedido(Short idPedido);
    
    /**
     * 
     * @param pedido 
     * @return  
     */
    public Short insertarPedido(Pedido pedido);
    
    /**
     * 
     * @param pedido
     * @throws SQLException 
     */
    public void actualizarEstadoPedido(Pedido pedido) throws SQLException;
    
    /**
     * 
     * @param idUsuario
     * @return 
     */
    public List<LineaPedido> obtenerLineasPedidosFinalizados(int idUsuario) throws SQLException;
    
    /**
     * 
     * @param idPedido
     * @return 
     */
    public boolean eliminarPedido(Short idPedido);
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
