package com.daw.service.mappers;

import com.daw.persistence.entities.PizzaPedido;
import com.daw.service.dto.PizzaPedidoOutputDTO;

public class PizzaPedidoMapper {
	
	public static PizzaPedidoOutputDTO toDTO(PizzaPedido pizzaPedido) {
		PizzaPedidoOutputDTO dto = new PizzaPedidoOutputDTO();
		
		dto.setId(pizzaPedido.getId());
		dto.setCantidad(pizzaPedido.getCantidad());
		dto.setPrecio(pizzaPedido.getPrecio());
		dto.setIdPizza(pizzaPedido.getIdPizza());
		dto.setPizza(pizzaPedido.getPizza().getNombre());
		
		return dto;
	}

}