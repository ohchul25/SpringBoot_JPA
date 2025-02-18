package com.prac.source.cart.dto;

import java.util.Date;

import com.prac.source.cart.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
	
    private int cartId;      // 장바구니 ID
    private int customerId;  // 고객 ID
    private int productId;   // 상품 ID (Product 엔티티 참조)
    private int stock;       // 재고 수량
    private Date createdAt;  // 생성 시간
    private Date updatedAt;  // 수정 시간

    // ✅ Cart -> CartDto 변환 메서드
    public static CartDto entityToDto(Cart cart) {
        return new CartDto(
            cart.getCartId(),
            cart.getCustomerId(),
            cart.getProduct().getProductId(),  // Product 객체에서 ID만 추출
            cart.getCartStock(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }

}
