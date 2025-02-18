package com.prac.source.cart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac.source.cart.dto.CartDto;
import com.prac.source.cart.entity.Cart;
import com.prac.source.cart.repository.CartRepository;
import com.prac.source.common.CommonFunction;
import com.prac.source.product.entity.Product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {
	
	private CartRepository cartRepository;
	private CommonFunction commonFunction;
	
	@Autowired	
	public CartService(CartRepository cartRepository
						,CommonFunction commonFunction) {
		this.cartRepository = cartRepository;
		this.commonFunction = commonFunction;
	}
	
    public List<CartDto> getList(int customerId) {
    	
        return cartRepository.findByCustomerId(customerId).stream()
                .map(CartDto::entityToDto) // Cart → CartDto 변환
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Cart insert(int customerId, int productId, int stock) {
    	commonFunction.checkUser(customerId);
    	
    	Cart findInfo = cartRepository.findByCustomerIdAndProduct_ProductId(customerId, productId);
        
        if (findInfo != null) {
            throw new IllegalArgumentException("이미 추가된 상품입니다. customerId : " + customerId + ", productId : " + productId);
        }
        
        Product product = commonFunction.getProduct(productId);
        
        // 수정수량이 상품재고보다 적으면 수정불가
        if(product.getProductStock() < stock) {
        	throw new IllegalArgumentException("재고수량이 적습니다. productId : " + productId);
        }
        
        
    	Cart cart = Cart.builder()
    			.customerId(customerId)
    			.product(commonFunction.getProduct(productId))
    			.cartStock(stock)
    			.build();
    	
    	Cart saveCart = cartRepository.save(cart);
    	
        if (saveCart == null || saveCart.getCartId() == 0) {
            throw new RuntimeException("Cart 저장에 실패했습니다.");
        }
    	
    	return saveCart;
    }
    
    @Transactional
    public Cart update(int customerId, int productId, int stock) throws Exception{
    	commonFunction.checkUser(customerId);
    	Cart findInfo = cartRepository.findByCustomerIdAndProduct_ProductId(customerId, productId);
        Product product = commonFunction.getProduct(productId);
        
        // 고객ID에 상품ID가 있는지 체크
        if (findInfo == null) {
            throw new IllegalArgumentException("장바구니에 상품이 없습니다. customerId : " + customerId + ", productId : " + productId);
        }
        
        // 수정수량이 상품재고보다 적으면 수정불가
        if(product.getProductStock() < stock) {
        	throw new IllegalArgumentException("재고수량이 적습니다. productId : " + productId);
        }
        
    	Cart cart = Cart.builder()
    			.cartId(findInfo.getCartId())
    			.customerId(findInfo.getCustomerId())
    			.product(product)
    			.cartStock(stock)
    			.build();
    	
    	Cart saveCart = cartRepository.save(cart);
    	
        if (saveCart == null || saveCart.getCartId() == 0) {
            throw new RuntimeException("Cart 수정에 실패했습니다.");
        }
    	
    	return saveCart;
    }
    
    @Transactional
    public Cart delete(int customerId, int productId) throws Exception{
    	commonFunction.checkUser(customerId);
        Cart cart = cartRepository.findByCustomerIdAndProduct_ProductId(customerId, productId);
      
        if (cart == null) {
            throw new RuntimeException("장바구니가 없습니다. customerId : " + customerId);
        }
        System.err.println(customerId);
        System.err.println(productId);
        
    	cartRepository.deleteByCustomerIdAndProductId(customerId,productId);

    	return cart;
    }

}
