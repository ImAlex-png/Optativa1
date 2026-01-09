package com.daw.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.Oferta;
import com.daw.persistence.entities.Pizza;
import com.daw.services.OfertaService;
import com.daw.services.PizzaService;
import com.daw.services.dto.OfertaDTO;
import com.daw.services.exceptions.OfertaNotFoundException;
import com.daw.services.exceptions.PizzaNotFoundException;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private PizzaService pizzaService;

    @GetMapping
    public ResponseEntity<List<Oferta>> list() {
        return ResponseEntity.ok(this.ofertaService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OfertaDTO ofertaDTO) {
        try {
            Oferta oferta = new Oferta();
            oferta.setTexto(ofertaDTO.getTexto());
            oferta.setDescuento(ofertaDTO.getDescuento());

            if (ofertaDTO.getIdPizza() > 0) {
                Pizza pizza = this.pizzaService.findById(ofertaDTO.getIdPizza());
                oferta.setPizza(pizza);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(this.ofertaService.create(oferta));
        } catch (PizzaNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody OfertaDTO ofertaDTO) {
        try {
            Oferta oferta = new Oferta();
            oferta.setTexto(ofertaDTO.getTexto());
            oferta.setActiva(ofertaDTO.isActiva());
            oferta.setDescuento(ofertaDTO.getDescuento());

            if (ofertaDTO.getIdPizza() > 0) {
                Pizza pizza = this.pizzaService.findById(ofertaDTO.getIdPizza());
                oferta.setPizza(pizza);
            }

            return ResponseEntity.ok(this.ofertaService.update(id, oferta));
        } catch (OfertaNotFoundException | PizzaNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            this.ofertaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (OfertaNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable int id) {
        try {
            this.ofertaService.activar(id);
            return ResponseEntity.ok().build();
        } catch (OfertaNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable int id) {
        try {
            this.ofertaService.desactivar(id);
            return ResponseEntity.ok().build();
        } catch (OfertaNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
