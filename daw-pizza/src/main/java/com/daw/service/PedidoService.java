package com.daw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Pedido;
import com.daw.persistence.repositories.PedidoRepository;
import com.daw.service.exception.ClienteNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;	
	
	public List<Pedido> findAll(){
		return this.pedidoRepository.findAll();
	}
	
	public Pedido findById(int idPedido) {
		if(this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}
		
		return this.pedidoRepository.findById(idPedido).get();
	}
	
	public Pedido create(Pedido pedido) {
		pedido.setId(0);
		
		return this.pedidoRepository.save(pedido);
	}
	
	public Pedido update(int idPedido, Pedido pedido) {
		Pedido pedidoBD = this.findById(idPedido);
		pedidoBD.setIdCliente(pedido.getIdCliente());
		pedidoBD.setFecha(pedido.getFecha());
		pedidoBD.setTotal(pedido.getTotal());
		pedidoBD.setMetodo(pedido.getMetodo());
		pedidoBD.setNotas(pedido.getNotas());
		
		return this.pedidoRepository.save(pedido);
	}
	
	public void deleteById(int idPedido) {
		if(!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}
		
		this.pedidoRepository.deleteById(idPedido);
	}
}
