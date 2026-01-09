package com.daw.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfertaDTO {
    private int id;
    private String texto;
    private boolean activa;
    private double descuento;
    private int idPizza; // To link with Pizza
}
