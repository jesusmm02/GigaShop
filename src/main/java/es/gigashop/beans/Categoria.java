package es.gigashop.beans;

public class Categoria {

    private Byte idCategoria;
    private String nombre;
    private String imagen;

    public byte getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setIdCategoria(byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    
    
}
