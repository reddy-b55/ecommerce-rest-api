package com.example.ecommerce.dto.product;

import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(

        Long id,

        String name,

        String description,

        BigDecimal price,

        Integer stock,

        ProductCategory category,

        ProductStatus status,

        String imageUrl,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}