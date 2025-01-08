package es.gigashop.beans;

public class LineaPedido {

    private Short idLinea;
    private Pedido pedido;
    private Producto producto;
    private Byte cantidad;

    public Short getIdLinea() {
        return idLinea;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public Byte getCantidad() {
        return cantidad;
    }

    public void setIdLinea(Short idLinea) {
        this.idLinea = idLinea;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(Byte cantidad) {
        this.cantidad = cantidad;
    }
    
    

}
