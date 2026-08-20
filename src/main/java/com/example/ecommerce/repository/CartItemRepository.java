package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    // ============================================================
    // FIND ALL ITEMS BY SHOPPING CART ID
    // ============================================================

    List<CartItem> findByShoppingCartId(Long cartId);


    // ============================================================
    // FIND CART ITEM BY CART ID AND PRODUCT ID
    // ============================================================

    Optional<CartItem> findByShoppingCartIdAndProductId(
            Long cartId,
            Long productId
    );


    // ============================================================
    // DELETE ALL ITEMS BY CART ID
    // ============================================================

    @Modifying
    @Transactional
    void deleteByShoppingCartId(Long shoppingCartId);
}