package in.Scaliveswiftcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.Scaliveswiftcart.dto.request.ProductRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateProductRequestDTO;
import in.Scaliveswiftcart.dto.response.ApiResponseDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;
import in.Scaliveswiftcart.dto.response.ProductResponseDTO;
import in.Scaliveswiftcart.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {
	private ProductService service;
	@Autowired
  public ProductController(ProductService service) {
		this.service = service;
	}
	
	 @PostMapping
     public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO dto){
	  ProductResponseDTO responseDTO = service.createProduct(dto);
	  return new ResponseEntity<>(ApiResponseDTO.success("Product created successfully", responseDTO), HttpStatus.CREATED);
  }
  
	 @GetMapping("/{id}")
     public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductById(@PathVariable Long id){
		 ProductResponseDTO res = service.getProductById(id);
		 return ResponseEntity.ok(ApiResponseDTO.success(res));
	 }
	 
	 @GetMapping("/sku/{sku}")
	 public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductBySku(@PathVariable String sku){
		 ProductResponseDTO res = service.getProductBySku(sku);
		 return ResponseEntity.ok(ApiResponseDTO.success(res)); 
	 }
	 
	 @GetMapping("/all")
	 public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAllProduct(){
		 List<ProductResponseDTO> productList = service.getAllProduct();
		 return ResponseEntity.ok(ApiResponseDTO.success("fetched"+ productList.size()+ "products"+ productList));
	 }
	 
	 @GetMapping
	 public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getAllProductPeginated(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortby, @RequestParam(defaultValue = "asc") String sortDir){
		 PageResponseDTO<ProductResponseDTO> products = service.getAllProductsPaginated(page, size, sortby, sortDir);
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 }
	 
	 @GetMapping("/available")
	 public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAvailableProducts() {
		 List<ProductResponseDTO> products = service.getAvailableProducts();
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 }
	 
	 @GetMapping("/category/{category}")
	 public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductByCategory(@PathVariable String category){
		 List<ProductResponseDTO> products = service.getProductByCategory(category);
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 }
	 
	 @GetMapping("/price-range")
	 public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductByPriceRange(@RequestParam  double min, @RequestParam double max){
		 List<ProductResponseDTO> products = service.getProductsByPriceRange(min, max);
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 }
	 
	 @GetMapping("/search")
	 public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> searchProduct(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
		 PageResponseDTO<ProductResponseDTO> products = service.searchProducts(keyword, page, size);
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequestDTO productRequest){
		 ProductResponseDTO products = service.updateProduct(id, productRequest);
		 return ResponseEntity.ok(ApiResponseDTO.success("Product Updated Successfully"+ products));
	 }
	 
	 @PatchMapping("{id}/stock")
	 public ResponseEntity<ApiResponseDTO<Void>> updateStock(@PathVariable Long id, @RequestParam Integer quantity){
		 service.updateStock(id, quantity);
		 return ResponseEntity.ok(ApiResponseDTO.success("Stock Updated Successfully"));
	 }
	 
	 @GetMapping("/low-stock")
	 public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getLowStockProduct(@RequestParam(defaultValue = "10")  Integer threshold){
		 List<ProductResponseDTO> products = service.getLowStockProducts(threshold);
		 return ResponseEntity.ok(ApiResponseDTO.success(products));
	 } 
	 
	 @GetMapping("/check-sku/{sku}")
	 public ResponseEntity<ApiResponseDTO<Boolean>> checkExist(@PathVariable String sku){
		 boolean exist = service.existsBySku(sku);
		 return ResponseEntity.ok(ApiResponseDTO.success(exist?"SKU alredy exist": "sku is available to use"));
		 
	 }
	 
	 
}

