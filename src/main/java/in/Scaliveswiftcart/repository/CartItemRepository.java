package in.Scaliveswiftcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.Scaliveswiftcart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
   Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
   
   boolean existsByCartIdAndProductId(Long cartId, Long productId);
}
