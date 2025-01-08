package es.gigashop.beans;

import java.util.Date;
import java.util.List;

public class Pedido {

    private Short idPedido;
    private Date Fecha;

    public enum Estado {
        c,
        f
    }
    private Estado estado;
    private Usuario usuario;
    private Double importe;
    private Double iva;
    private List<LineaPedido> lineasPedidos;

    public Short getIdPedido() {
        return idPedido;
    }

    public Date getFecha() {
        return Fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Double getImporte() {
        return importe;
    }

    public Double getIva() {
        return iva;
    }

    public List<LineaPedido> getLineasPedidos() {
        return lineasPedidos;
    }

    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public void setLineasPedidos(List<LineaPedido> lineasPedidos) {
        this.lineasPedidos = lineasPedidos;
    }

    
    
}
