package es.gigashop.DAO;

import es.gigashop.beans.LineaPedido;
import es.gigashop.beans.Pedido;
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
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
