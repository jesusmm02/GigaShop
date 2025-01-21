package es.gigashop.beans;

import java.io.Serializable;
import java.util.Objects;

public class Categoria implements Serializable {

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Categoria categoria = (Categoria) obj;
        return idCategoria.equals(categoria.idCategoria); // Comparación basada en el ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria); // Genera el hash en base al ID
    }

}
