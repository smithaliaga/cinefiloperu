package com.pe.cinefilos.entities;

import java.io.Serializable;

public class Pelicula implements Serializable {

    private Long codigoPelicula;
    private String nombre;
    private String descripcion;
    private String image;
    private String video;

    public Long getCodigoPelicula() {
        return codigoPelicula;
    }

    public void setCodigoPelicula(Long codigoPelicula) {
        this.codigoPelicula = codigoPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
