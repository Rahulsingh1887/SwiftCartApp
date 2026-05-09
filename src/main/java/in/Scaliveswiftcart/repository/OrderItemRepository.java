package in.Scaliveswiftcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.Scaliveswiftcart.entity.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Integer>{

}
