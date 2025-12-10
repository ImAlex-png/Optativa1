package com.daw.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pizzaPedidos")
public class PizzaPedidoController {

	

	
//	@GetMapping("/{idPizzaPedido}")
//	public ResponseEntity<?> findById(@PathVariable int idPizzaPedido){
//		try {
//			return ResponseEntity.ok(this.pizzaPedidoService.findById(idPizzaPedido));
//		}
//		catch(PizzaPedidoNotFoundException ex) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//		}
//	}
	
}