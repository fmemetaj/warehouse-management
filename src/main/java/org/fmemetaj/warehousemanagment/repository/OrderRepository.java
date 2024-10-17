package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findAllByUserIdAndStatus(Long userId, Order.OrderStatus status);
}
