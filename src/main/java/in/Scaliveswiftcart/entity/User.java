package in.Scaliveswiftcart.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
	@Column(nullable = false, length = 100)
   private String fullName;
	@Column(nullable = false, unique = true, length = 150)
   private String email;
	@Column(length = 20)
   private String phone;
	@Column(nullable = false)
   private String password;
	@Column(length = 255)
   private String address;
   @Column(nullable = false)
   @Builder.Default
   private Boolean isActive = false;
   
   @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private Cart cart;
   
   @Builder.Default
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Order> orders = new ArrayList<>();
  
}
