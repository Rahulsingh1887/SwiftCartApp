package in.Scaliveswiftcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.Scaliveswiftcart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
	Optional<Cart> findByUserId(Long userId);
	boolean existsByUserId(Long UserId);
	void deleteByUserId(Long userId);
	
	@Query("SELECT c FROM Cart c "+
	"LEFT JOIN FETCH c.cartItems ci "+
	"LEFT JOIN FETCH ci.product "+ 
	" WHERE c.user.id =:userId ")
	Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);
	
	
}
