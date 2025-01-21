package es.gigashop.beans;

import java.io.Serializable;

public class Producto implements Serializable {

    private Short idProducto;
    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String marca;
    private String imagen;


    public Short getIdProducto() {
        return idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getMarca() {
        return marca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setIdProducto(Short idProducto) {
        this.idProducto = idProducto;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    

}
