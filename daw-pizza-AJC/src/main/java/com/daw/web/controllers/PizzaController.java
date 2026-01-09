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

import com.daw.persistence.entities.Pizza;
import com.daw.services.PizzaService;
import com.daw.services.exceptions.PizzaException;
import com.daw.services.exceptions.PizzaNotFoundException;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {

	@Autowired
	private PizzaService pizzaService;

	@GetMapping
	public ResponseEntity<List<Pizza>> list() {
		return ResponseEntity.ok(this.pizzaService.findAll());
	}

	@GetMapping("/{idPizza}")
	public ResponseEntity<?> findById(@PathVariable int idPizza) {
		try {
			Pizza pizza = this.pizzaService.findById(idPizza);

			com.daw.services.dto.PizzaDetailDTO dto = new com.daw.services.dto.PizzaDetailDTO();
			dto.setId(pizza.getId());
			dto.setNombre(pizza.getNombre());
			dto.setDescripcion(pizza.getDescripcion());
			dto.setPrecio(pizza.getPrecio());
			dto.setDisponible(pizza.isDisponible());
			dto.setVegana(pizza.isVegana());
			dto.setVegetariana(pizza.isVegetariana());

			if (pizza.getOfertas() != null) {
				List<com.daw.services.dto.OfertaDTO> ofertasDTO = pizza.getOfertas().stream()
						.filter(com.daw.persistence.entities.Oferta::isActiva)
						.map(oferta -> {
							com.daw.services.dto.OfertaDTO oDto = new com.daw.services.dto.OfertaDTO();
							oDto.setId(oferta.getId());
							oDto.setTexto(oferta.getTexto());
							oDto.setActiva(oferta.isActiva());
							oDto.setDescuento(oferta.getDescuento());
							oDto.setIdPizza(oferta.getPizza().getId());
							return oDto;
						})
						.collect(java.util.stream.Collectors.toList());
				dto.setOfertasActivas(ofertasDTO);
			} else {
				dto.setOfertasActivas(new java.util.ArrayList<>());
			}

			return ResponseEntity.ok(dto);
		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Pizza pizza) {
		// try {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pizzaService.create(pizza));
		// }
		// catch(PizzaException ex) {
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		// }
	}

	@PutMapping("/{idPizza}")
	public ResponseEntity<?> update(@PathVariable int idPizza, @RequestBody Pizza pizza) {
		try {
			return ResponseEntity.ok(this.pizzaService.update(idPizza, pizza));
		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (PizzaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{idPizza}")
	public ResponseEntity<?> delete(@PathVariable int idPizza) {
		try {
			this.pizzaService.deleteById(idPizza);
			return ResponseEntity.ok().build();
		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

}
