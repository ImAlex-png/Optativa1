package com.daw.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oferta")
@Getter
@Setter
@NoArgsConstructor
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String texto;

    @Column(columnDefinition = "BOOLEAN")
    private boolean activa;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private double descuento;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_pizza", referencedColumnName = "id")
    private Pizza pizza;
}
