package in.Scaliveswiftcart.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.Scaliveswiftcart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
     
	
	List<Order> findByUserIdOrderByOrderDateDesc(Long UserId);
	Page<Order> findByUserId(Long userId, Pageable pageable);
	
	List<Order> findByStatus(String status);
	
	@Query("SELECT o FROM Order o "+
	"LEFT JOIN FETCH o.orderItems oi "+
	"LEFT JOIN FETCH oi.product "+
	"WHERE o.id=:orderId ")
	Optional<Order> findByIdWithItems(@Param("orderId")Long orderId);
    
	@Query("SELECT o FROM Order o "+
	"LEFT JOIN FETCH o.orderItems oi "+
	"LEFT JOIN FETCH oi.product "+
	"WHERE o.orderNumber=:orderNumber ")
	Optional<Order> findByOrderNumberWithItems(@Param("orderNumber") String orderNumber);
	
	@Query("SELECT o FROM Order o WHERE o.orderNumber LIKE %:keyword% "+
	"OR o.user.fullName LIKE %:keyword% "+
	"OR o.user.email LIKE %:keyword% ")
	Page<Order> SearchOrder(@Param("keyword") String keyword, Pageable pageable);
}

