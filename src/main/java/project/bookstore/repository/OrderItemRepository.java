package project.bookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
