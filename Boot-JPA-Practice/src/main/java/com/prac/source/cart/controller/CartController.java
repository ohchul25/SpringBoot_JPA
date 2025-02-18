package com.prac.source.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac.source.cart.dto.CartDto;
import com.prac.source.cart.entity.Cart;
import com.prac.source.cart.service.CartService;
import com.prac.source.common.ResponseApi;
import com.prac.source.common.responseCode;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	private CartService cartService;
	
	@Autowired	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@GetMapping(value="/list/{customerId}")
	public ResponseApi<List<CartDto>> list(@PathVariable int customerId){
		
		List<CartDto> list = cartService.getList(customerId);
		
		if(list.isEmpty()) {
			return new ResponseApi<>(false,null,responseCode.FAIL_READ.getMessage());
		}else {
			return new ResponseApi<>(true,list,responseCode.SUCCESS_READ.getMessage());
		}
	}
	
	@PostMapping(value="/{customerId}/{productId}/{stock}")
	public ResponseApi<Cart> insert(@PathVariable("customerId") int customerId
										, @PathVariable("productId") int productId
										, @PathVariable("stock") int stock) throws Exception {
		
		return new ResponseApi<>(true, cartService.insert(customerId, productId, stock),responseCode.SUCCESS_INSERT.getMessage());
	}
	
	@PutMapping(value="/{customerId}/{productId}/{stock}")
	public ResponseApi<Cart> update(@PathVariable("customerId") int customerId
										, @PathVariable("productId") int productId
										, @PathVariable("stock") int stock) throws Exception{
		return new ResponseApi<>(true, cartService.update(customerId, productId, stock),responseCode.SUCCESS_UPDATE.getMessage());
	}
	
	@DeleteMapping(value="/{customerId}/{productId}")
	public ResponseApi<Cart> delete (@PathVariable("customerId") int customerId
								, @PathVariable("productId") int productId) throws Exception{
		// 삭제완료 처리
		return new ResponseApi<>(true, cartService.delete(customerId, productId),responseCode.SUCCESS_DELETE.getMessage());
	}

}
