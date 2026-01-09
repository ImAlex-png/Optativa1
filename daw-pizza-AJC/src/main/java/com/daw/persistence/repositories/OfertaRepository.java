package com.daw.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.daw.persistence.entities.Oferta;

public interface OfertaRepository extends ListCrudRepository<Oferta, Integer> {

    List<Oferta> findByPizzaId(int pizzaId);

    @Modifying
    @Query("UPDATE Oferta o SET o.activa = false WHERE o.pizza.id = :pizzaId AND o.id != :ofertaId")
    void deactivateOtherOffers(@Param("pizzaId") int pizzaId, @Param("ofertaId") int ofertaId);

    // If we assume global offers for simple logical default fallback
    @Modifying
    @Query("UPDATE Oferta o SET o.activa = false WHERE o.id != :ofertaId")
    void deactivateAllOtherOffers(@Param("ofertaId") int ofertaId);
}
