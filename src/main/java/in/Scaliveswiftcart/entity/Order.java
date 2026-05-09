package in.Scaliveswiftcart.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
	@Column(nullable = false, unique = true, length = 50)
   private String orderNumber;
	
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name= "user_id", nullable = false)
   private User user;
   
   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<OrderItem> orderItems = new ArrayList<>();
   
   
   private Double totalAmount;
   
   public final static String STATUS_CONFIRMED = "CONFIRMED"; 
   public final static String STATUS_CANCELLED = "CANCELLED";
   @Builder.Default
   private String status = STATUS_CONFIRMED;
   
   private String notes;
   
   @CreationTimestamp
   @Column(nullable = false)
   private LocalDateTime addedAt;
   
   @UpdateTimestamp
   private LocalDateTime updatedAt;
   
   private LocalDateTime orderDate;
	  
   @PrePersist
   public void generateOrderNumber() {
	   if(orderNumber == null) {
		   orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	   }
	   if(orderDate == null) {
		   orderDate = LocalDateTime.now();
	   }
   }
   
   public void addOrderItem(OrderItem item) {
	    orderItems.add(item);
	    item.setOrder(this);
	   
   }
   
   public void calculateTotals() {
	   double total = 0.0;
	   for(OrderItem item:orderItems) {
		   total = total+ item.getSubTotal();
	   }
	   this.totalAmount = total;
   }
}
