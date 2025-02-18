package com.prac.source.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac.source.common.ResponseApi;
import com.prac.source.common.responseCode;
import com.prac.source.product.dto.ProductDto;
import com.prac.source.product.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	private ProductService productService;
	
	@Autowired	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(value="/list")
	public ResponseApi<List<ProductDto>> list(){
		
		List<ProductDto> productList = productService.getList();
		
		if(productList.isEmpty()) {
			return new ResponseApi<>(false,null,responseCode.FAIL_READ.getMessage());
		}else {
			return new ResponseApi<>(true,productList,responseCode.SUCCESS_READ.getMessage());
		}
		
	}

}
