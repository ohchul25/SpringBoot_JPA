--
CREATE USER 'prac'@'%' IDENTIFIED BY '1234';

--
GRANT ALL PRIVILEGES ON prac.* TO 'prac'@'%' IDENTIFIED BY '1234';

--
FLUSH PRIVILEGES;

-- 스키마 생성
CREATE DATABASE prac;

-- 스키마 교채
use prac;

-- 상품 테이블
CREATE TABLE product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 ID',
    product_name VARCHAR(255) NOT NULL COMMENT '상품 이름',
    product_description VARCHAR(1000) COMMENT '상품 설명',
    product_price BIGINT NOT NULL COMMENT '상품 가격',
    product_stock INT NOT NULL COMMENT '재고 수량',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정 시간'
) DEFAULT CHARSET=utf8 
;

-- 고객 테이블
CREATE TABLE customer (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고객 ID',
    customer_name VARCHAR(50) NOT NULL UNIQUE COMMENT '고객 이름',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정 시간'
) DEFAULT CHARSET=utf8 
;

-- 장바구니 테이블
CREATE TABLE cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
    customer_id bigint NOT NULL COMMENT '고객 ID',
    product_id bigint NOT NULL COMMENT '상품 ID',
    cart_stock INT NOT NULL COMMENT '수량',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정 시간'
) DEFAULT CHARSET=utf8 
;

-- 주문 테이블
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
    customer_id bigint NOT NULL COMMENT '고객 ID',
    order_amount INT NOT NULL COMMENT '주문금액',
    order_status VARCHAR(10) NOT NULL COMMENT '주문상태(0:주문요청, 1:주문성공, 2:주문실패)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정 시간'
) DEFAULT CHARSET=utf8 
;

-- 주문 상세 테이블
CREATE TABLE order_detail (
    sn BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '순번',
    order_id bigint NOT NULL COMMENT '주문 ID',
    product_id bigint NOT NULL COMMENT '상품 ID',
    order_detail_stock INT NOT NULL COMMENT '주문 당시 수량',
    order_detail_name VARCHAR(255) NOT NULL COMMENT '주문 당시 상품 이름',
    order_detail_description VARCHAR(1000) COMMENT '주문 당시 상품 설명',
    order_detail_price BIGINT NOT NULL COMMENT '주문 당시 상품 가격',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간'
) DEFAULT CHARSET=utf8 
;

-- 결제 테이블
CREATE TABLE payment (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '결재 ID',
    order_id bigint NOT NULL COMMENT '고객 ID',
    customer_id bigint NOT NULL COMMENT '고객 ID',
    payment_amount INT NOT NULL COMMENT '결제금액',
    payment_status VARCHAR(10) NOT NULL COMMENT '결제상태(0:결제요청,1:결제성공,2:결제실패)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정 시간'
) DEFAULT CHARSET=utf8 
;
