package es.gigashop.beans;

import java.util.List;

public class Filtro {

    private List<Categoria> categorias;
    private Double precioMin;
    private Double precioMax;
    private List<String> marcas;

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Double getPrecioMin() {
        return precioMin;
    }

    public Double getPrecioMax() {
        return precioMax;
    }

    public List<String> getMarcas() {
        return marcas;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setPrecioMin(String precio) {
        // Para el caso de "0-100", "101-200", etc.
        if (precio != null && precio.contains("-")) {
            this.precioMin = Double.parseDouble(precio.split("-")[0]);
        } else if (precio != null) {
            // En caso de que el precio no tenga el formato "min-max", solo tomamos el valor como mínimo.
            this.precioMin = Double.parseDouble(precio);
        }
    }

    public void setPrecioMax(String precio) {
        try {
            if (precio != null && !precio.isEmpty()) {
                this.precioMax = Double.parseDouble(precio);
            } else {
                this.precioMax = null; // Si el precio es null o vacío, se establece en null
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear precioMax: " + precio);
            this.precioMax = null;
        }
    }

    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

}
