package com.daw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.persistence.entities.Oferta;
import com.daw.persistence.repositories.OfertaRepository;
import com.daw.services.exceptions.OfertaNotFoundException;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository ofertaRepository;

    public List<Oferta> findAll() {
        return this.ofertaRepository.findAll();
    }

    public Oferta findById(int id) {
        return this.ofertaRepository.findById(id)
                .orElseThrow(() -> new OfertaNotFoundException("La oferta con ID " + id + " no existe."));
    }

    @Transactional
    public Oferta create(Oferta oferta) {
        // Logic: Mark active by default
        oferta.setActiva(true);
        oferta.setId(0); // Ensure creation

        Oferta saved = this.ofertaRepository.save(oferta);

        // Logic: Mark others inactive
        if (saved.getPizza() != null) {
            this.ofertaRepository.deactivateOtherOffers(saved.getPizza().getId(), saved.getId());
        }

        return saved;
    }

    @Transactional
    public Oferta update(int id, Oferta oferta) {
        if (!this.ofertaRepository.existsById(id)) {
            throw new OfertaNotFoundException("La oferta con ID " + id + " no existe.");
        }
        oferta.setId(id);

        // If updating to active, disable others
        if (oferta.isActiva()) {
            if (oferta.getPizza() != null) {
                this.ofertaRepository.deactivateOtherOffers(oferta.getPizza().getId(), id);
            }
        }

        return this.ofertaRepository.save(oferta);
    }

    public void delete(int id) {
        if (!this.ofertaRepository.existsById(id)) {
            throw new OfertaNotFoundException("La oferta con ID " + id + " no existe.");
        }
        this.ofertaRepository.deleteById(id);
    }

    @Transactional
    public void activar(int id) {
        Oferta oferta = this.findById(id);
        oferta.setActiva(true);
        this.ofertaRepository.save(oferta);
        if (oferta.getPizza() != null) {
            this.ofertaRepository.deactivateOtherOffers(oferta.getPizza().getId(), id);
        }
    }

    @Transactional
    public void desactivar(int id) {
        Oferta oferta = this.findById(id);
        oferta.setActiva(false);
        this.ofertaRepository.save(oferta);
    }
}
