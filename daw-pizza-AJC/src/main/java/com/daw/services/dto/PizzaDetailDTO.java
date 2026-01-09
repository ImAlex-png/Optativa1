package com.daw.services.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PizzaDetailDTO {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponible;
    private boolean vegana;
    private boolean vegetariana;
    private List<OfertaDTO> ofertasActivas;
}
