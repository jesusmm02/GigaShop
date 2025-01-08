package es.gigashop.beans;

import java.sql.Timestamp;

public class Usuario {

    private Short idUsuario;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String nif;
    private String telefono;
    private String direccion;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private Timestamp ultimoAcceso;
    private String avatar;

    public Short getIdUsuario() {
        return idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getNif() {
        return nif;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setIdUsuario(Short idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    
    
}
