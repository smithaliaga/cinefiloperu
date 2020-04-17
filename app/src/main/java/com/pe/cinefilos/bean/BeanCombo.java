package com.pe.cinefilos.bean;

public class BeanCombo {

    private long codigo;
    private String descripcion;
    private long codigoPadre;
    private String descripcionPadre;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getCodigoPadre() {
        return codigoPadre;
    }

    public void setCodigoPadre(long codigoPadre) {
        this.codigoPadre = codigoPadre;
    }

    public String getDescripcionPadre() {
        return descripcionPadre;
    }

    public void setDescripcionPadre(String descripcionPadre) {
        this.descripcionPadre = descripcionPadre;
    }

    public BeanCombo(long codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public BeanCombo(long codigo, String descripcion, long codigoPadre, String descripcionPadre) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.codigoPadre = codigoPadre;
        this.descripcionPadre = descripcionPadre;
    }
}
