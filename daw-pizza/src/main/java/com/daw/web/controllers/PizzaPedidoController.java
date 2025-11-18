package com.daw.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PizzaPedido;
import com.daw.service.PizzaPedidoService;

@RestController
@RequestMapping("/pizzaPedidos")
public class PizzaPedidoController {

	@Autowired
	private PizzaPedidoService pizzaPedidoService;
	
	@GetMapping
	public ResponseEntity<List<PizzaPedido>> list(){
		return ResponseEntity.ok(this.pizzaPedidoService.findAll());
	}
}
