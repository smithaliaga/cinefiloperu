package com.pe.cinefilos.object.entities;

import java.io.Serializable;

public class Pago implements Serializable {

    public String numeroTarjetaOfuscado;
    public String fechaTransaccion;
    public double subTotal;
    public double igv;
    public double total;

}
