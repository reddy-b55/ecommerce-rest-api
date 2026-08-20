package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.cart.ShoppingCartRequestDto;
import com.example.ecommerce.dto.cart.ShoppingCartResponseDto;

import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.ShoppingCart;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ShoppingCartRepository;
import com.example.ecommerce.service.ShoppingCartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl
        implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final CustomerRepository customerRepository;

    private final CartItemRepository cartItemRepository;


    // ============================================================
    // CREATE
    // ============================================================

    @Override
    public ShoppingCartResponseDto create(
            ShoppingCartRequestDto request) {

        log.info(
                "Creating shopping cart for customer ID: {}",
                request.customerId()
        );

        Customer customer =
                customerRepository.findById(
                        request.customerId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + request.customerId()
                        )
                );


        if (shoppingCartRepository
                .existsByCustomerId(request.customerId())) {

            throw new IllegalStateException(
                    "Shopping cart already exists for customer ID: "
                            + request.customerId()
            );
        }


        ShoppingCart cart =
                new ShoppingCart(customer);


        ShoppingCart savedCart =
                shoppingCartRepository.save(cart);


        log.info(
                "Shopping cart created successfully. Cart ID: {}",
                savedCart.getId()
        );


        return mapToResponse(savedCart);
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getById(
            Long id) {

        ShoppingCart cart =
                shoppingCartRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shopping cart not found with ID: "
                                                + id
                                )
                        );


        return mapToResponse(cart);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartResponseDto> getAll() {

        return shoppingCartRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET BY CUSTOMER ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getByCustomerId(
            Long customerId) {

        ShoppingCart cart =
                shoppingCartRepository
                        .findByCustomerId(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shopping cart not found for customer ID: "
                                                + customerId
                                )
                        );


        return mapToResponse(cart);
    }


    // ============================================================
    // DELETE
    // ============================================================

    @Override
    public void delete(Long id) {

        ShoppingCart cart =
                shoppingCartRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shopping cart not found with ID: "
                                                + id
                                )
                        );


        shoppingCartRepository.delete(cart);


        log.info(
                "Shopping cart deleted successfully. Cart ID: {}",
                id
        );
    }


    // ============================================================
    // CLEAR CART
    // ============================================================

    @Override
    public ShoppingCartResponseDto clearCart(
            Long id) {

        ShoppingCart cart =
                shoppingCartRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shopping cart not found with ID: "
                                                + id
                                )
                        );


        cartItemRepository.deleteByShoppingCartId(id);


        cart.clearItems();


        ShoppingCart updatedCart =
                shoppingCartRepository.save(cart);


        log.info(
                "Shopping cart cleared successfully. Cart ID: {}",
                id
        );


        return mapToResponse(updatedCart);
    }


    // ============================================================
    // FIND ABANDONED CARTS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartResponseDto> findAbandonedCarts(
            int hours) {

        if (hours <= 0) {

            throw new IllegalArgumentException(
                    "Hours must be greater than zero"
            );
        }


        LocalDateTime cutoffTime =
                LocalDateTime.now()
                        .minusHours(hours);


        log.info(
                "Searching abandoned carts older than {} hours",
                hours
        );


        return shoppingCartRepository
                .findAbandonedCarts(cutoffTime)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // ENTITY -> DTO
    // ============================================================

    private ShoppingCartResponseDto mapToResponse(
            ShoppingCart cart) {

        List<com.example.ecommerce.dto.cartitem.CartItemResponseDto> itemResponses =
                cart.getCartItems() == null ? List.of() :
                        cart.getCartItems().stream()
                                .map(item -> new com.example.ecommerce.dto.cartitem.CartItemResponseDto(
                                        item.getId(),
                                        item.getShoppingCart().getId(),
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getQuantity(),
                                        item.getProduct().getPrice(),
                                        item.getCreatedAt(),
                                        item.getUpdatedAt()
                                ))
                                .toList();

        return new ShoppingCartResponseDto(

                cart.getId(),

                cart.getCustomer().getId(),

                cart.getCustomer().getName(),

                itemResponses,

                cart.getCreatedAt(),

                cart.getUpdatedAt()
        );
    }
}