package project.bookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {
            "user",
            "cartItems",
            "cartItems.book"
    })
    Optional<ShoppingCart> findByUserId(Long userId);
}
