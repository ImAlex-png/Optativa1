package com.daw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Pedido;
import com.daw.persistence.repositories.PedidoRepository;
import com.daw.service.dto.PedidoDTO;
import com.daw.service.dto.PizzaPedidoInputDTO;
import com.daw.service.dto.PizzaPedidoOutputDTO;
import com.daw.service.exception.ClienteNotFoundException;
import com.daw.service.exception.PedidoNotFoundException;
import com.daw.service.mappers.PedidoMapper;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PizzaPedidoService pizzaPedidoService;

	public List<PedidoDTO> findAll() {
		return PedidoMapper.toDTOsFuncional(this.pedidoRepository.findAll());
	}

	public PedidoDTO findById(int idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		return PedidoMapper.toDTO(this.pedidoRepository.findById(idPedido).get());
	}

	public Pedido findEntityById(int idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		return this.pedidoRepository.findById(idPedido).get();
	}

	public PedidoDTO create(Pedido pedido) {
		pedido.setId(0);
		pedido.setFecha(LocalDateTime.now());
		pedido.setTotal(0.0);

		return PedidoMapper.toDTO(this.pedidoRepository.save(pedido));
	}

	public PedidoDTO update(int idPedido, Pedido pedido) {
		Pedido pedidoBD = this.findEntityById(idPedido);
		pedidoBD.setIdCliente(pedido.getIdCliente());
		pedidoBD.setFecha(pedido.getFecha());
		pedidoBD.setTotal(pedido.getTotal());
		pedidoBD.setMetodo(pedido.getMetodo());
		pedidoBD.setNotas(pedido.getNotas());

		return PedidoMapper.toDTO(this.pedidoRepository.save(pedido));
	}

	public void deleteById(int idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		this.pedidoRepository.deleteById(idPedido);
	}

	// CRUDs PizzaPedido
	// findAll
	public List<PizzaPedidoOutputDTO> findPizzasByIdPedido(int idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pizzaPedidoService.findByIdPedido(idPedido);
	}

	// findById
	public PizzaPedidoOutputDTO findPizzaPedidoById(int idPedido, int idPizzaPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pizzaPedidoService.findDTOById(idPizzaPedido);
	}

	// create
	public PizzaPedidoOutputDTO createPizzaPedido(int idPedido, PizzaPedidoInputDTO dto) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pizzaPedidoService.createDTO(dto);
	}

	// update
	public PizzaPedidoOutputDTO updatePizzaPedido(int idPedido, int idPizzaPedido, PizzaPedidoInputDTO dto) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pizzaPedidoService.updateDTO(idPizzaPedido, dto);
	}
	
	// delete
	public void deletePizzaPedidoById(int idPedido, int idPizzaPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		this.pizzaPedidoService.deleteById(idPizzaPedido);
	}
	
	//Funciones auxiliares
	private void recalcularTotal(Pedido pedido) {
		double total = 0.0;
		
		for(PizzaPedidoOutputDTO pp : this.pizzaPedidoService.findByIdPedido(pedido.getId())) {
			total += pp.getPrecio();
		}
		
		pedido.setTotal(total);
		this.pedidoRepository.save(pedido);
	}

}