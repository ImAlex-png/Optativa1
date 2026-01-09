package com.daw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Pizza;
import com.daw.persistence.entities.PizzaPedido;
import com.daw.persistence.repositories.PizzaPedidoRepository;
import com.daw.services.dto.PizzaPedidoInputDTO;
import com.daw.services.dto.PizzaPedidoOutputDTO;
import com.daw.services.exceptions.PizzaNotFoundException;
import com.daw.services.exceptions.PizzaPedidoNotFoundException;
import com.daw.services.mappers.PizzaPedidoMapper;

@Service
public class PizzaPedidoService {

	@Autowired
	private PizzaPedidoRepository pizzaPedidoRepository;

	@Autowired
	private PizzaService pizzaService;

	public List<PizzaPedido> findAll() {
		return this.pizzaPedidoRepository.findAll();
	}

	public PizzaPedido findById(int idPizzaPedido) {
		if (!this.pizzaPedidoRepository.existsById(idPizzaPedido)) {
			throw new PizzaPedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pizzaPedidoRepository.findById(idPizzaPedido).get();
	}

	public PizzaPedido create(PizzaPedido pizzaPedido) {
		pizzaPedido.setId(0);

		return this.pizzaPedidoRepository.save(pizzaPedido);
	}

	public PizzaPedido update(int idPizzaPedido, PizzaPedido pizzaPedido) {
		if (idPizzaPedido != pizzaPedido.getId()) {
			throw new PizzaPedidoNotFoundException("El ID del path y del body no coinciden. ");
		}

		PizzaPedido pizzaPedidoBD = this.findById(idPizzaPedido);
		pizzaPedidoBD.setIdPedido(pizzaPedido.getIdPedido());
		pizzaPedidoBD.setIdPizza(pizzaPedido.getIdPizza());
		pizzaPedidoBD.setPrecio(pizzaPedido.getPrecio());
		pizzaPedidoBD.setCantidad(pizzaPedido.getCantidad());

		return this.pizzaPedidoRepository.save(pizzaPedidoBD);
	}

	public void deleteById(int idPizzaPedido) {
		if (!this.pizzaPedidoRepository.existsById(idPizzaPedido)) {
			throw new PizzaNotFoundException("El ID indicado no existe. ");
		}

		this.pizzaPedidoRepository.deleteById(idPizzaPedido);
	}

	// CRUDs Controller Pedido
	// findAll de PizzaPedido
	public List<PizzaPedidoOutputDTO> findByIdPedido(int idPedido) {
		return PizzaPedidoMapper.toDtos(this.pizzaPedidoRepository.findByIdPedido(idPedido));
	}

	public PizzaPedidoOutputDTO findDTOById(int idPizzaPedido) {
		if (!this.pizzaPedidoRepository.existsById(idPizzaPedido)) {
			throw new PizzaPedidoNotFoundException("El ID indicado no existe. ");
		}

		return PizzaPedidoMapper.toDTO(this.pizzaPedidoRepository.findById(idPizzaPedido).get());
	}

	@org.springframework.transaction.annotation.Transactional
	public PizzaPedidoOutputDTO createDTO(PizzaPedidoInputDTO dto) {
		PizzaPedido entity = PizzaPedidoMapper.toEntity(dto);
		entity.setId(0);

		Pizza pizza = this.pizzaService.findById(entity.getIdPizza());

		double precioUnitario = pizza.getPrecio();
		if (pizza.getOfertas() != null) {
			java.util.Optional<com.daw.persistence.entities.Oferta> ofertaActiva = pizza.getOfertas().stream()
					.filter(com.daw.persistence.entities.Oferta::isActiva)
					.findFirst();

			if (ofertaActiva.isPresent()) {
				// Assuming discount is percentage (e.g. 20.0 for 20%)
				precioUnitario = precioUnitario * (1 - ofertaActiva.get().getDescuento() / 100.0);
			}
		}

		entity.setPrecio(entity.getCantidad() * precioUnitario);

		// Cuando ejecutamos el save, no vienen las entidades relacionadas (pizza y
		// pedido), por lo que
		// tenemos que ponersela para que no lance un NullPointerException en el Mapper
		// cuando queramos
		// hacer pizzaPedido.getPizza().getNombre()
		entity.setPizza(pizza);

		return PizzaPedidoMapper.toDTO(this.pizzaPedidoRepository.save(entity));
	}

	@org.springframework.transaction.annotation.Transactional
	public PizzaPedidoOutputDTO updateDTO(int idPizzaPedido, PizzaPedidoInputDTO dto) {
		if (idPizzaPedido != dto.getId()) {
			throw new PizzaPedidoNotFoundException("El ID del path y del body no coinciden. ");
		}

		PizzaPedido pizzaPedidoBD = this.findById(dto.getId());
		pizzaPedidoBD.setIdPedido(dto.getIdPedido());
		pizzaPedidoBD.setIdPizza(dto.getIdPizza());
		pizzaPedidoBD.setCantidad(dto.getCantidad());

		Pizza pizza = this.pizzaService.findById(dto.getIdPizza());

		double precioUnitario = pizza.getPrecio();
		if (pizza.getOfertas() != null) {
			java.util.Optional<com.daw.persistence.entities.Oferta> ofertaActiva = pizza.getOfertas().stream()
					.filter(com.daw.persistence.entities.Oferta::isActiva)
					.findFirst();

			if (ofertaActiva.isPresent()) {
				precioUnitario = precioUnitario * (1 - ofertaActiva.get().getDescuento() / 100.0);
			}
		}

		pizzaPedidoBD.setPrecio(dto.getCantidad() * precioUnitario);

		pizzaPedidoBD.setPizza(pizza);

		return PizzaPedidoMapper.toDTO(this.pizzaPedidoRepository.save(pizzaPedidoBD));
	}

}
