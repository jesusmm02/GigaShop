package es.gigashop.DAO;

import es.gigashop.beans.LineaPedido;

public interface ILineaPedidoDAO {

    /**
     *
     * @param idPedido
     * @param idProducto
     * @return
     */
    public LineaPedido obtenerLineaPedido(Short idPedido, Short idProducto);
    
    /**
     * 
     * @param lineaPedido 
     */
    public void insertarOActualizarLineaPedido(LineaPedido lineaPedido);
    
    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
    
}
