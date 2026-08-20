package com.example.ecommerce.scheduler;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCancellationScheduler {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Cancels pending orders older than 30 minutes.
     *
     * Runs according to:
     * app.scheduler.order-cancellation-rate
     */
    @Scheduled(fixedRateString = "${app.scheduler.order-cancellation-rate}")
    @Transactional
    public void cancelExpiredOrders() {

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(30);

        List<Order> expiredOrders =
                orderRepository.findPendingOrdersOlderThan(
                        OrderStatus.PENDING,
                        cutoffTime
                );

        if (expiredOrders.isEmpty()) {
            log.debug("No expired pending orders found.");
            return;
        }

        for (Order order : expiredOrders) {

            order.setStatus(OrderStatus.CANCELLED);

            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Product product = item.getProduct();
                    if (product != null) {
                        product.setStock(product.getStock() + item.getQuantity());
                        productRepository.save(product);
                    }
                }
            }

            log.info(
                    "Order {} automatically cancelled because it was pending before {}",
                    order.getId(),
                    cutoffTime
            );
        }

        orderRepository.saveAll(expiredOrders);

        log.info(
                "{} expired order(s) cancelled successfully.",
                expiredOrders.size()
        );
    }
}