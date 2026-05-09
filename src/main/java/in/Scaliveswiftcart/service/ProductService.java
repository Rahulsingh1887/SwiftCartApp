package in.Scaliveswiftcart.service;

import java.util.List;

import in.Scaliveswiftcart.dto.request.ProductRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateProductRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateUserRequestDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;
import in.Scaliveswiftcart.dto.response.ProductResponseDTO;

public interface ProductService {
  
	ProductResponseDTO createProduct(ProductRequestDTO productRequest);
	
	ProductResponseDTO getProductById(Long id);
	
	ProductResponseDTO getProductBySku(String sku);
	
	List<ProductResponseDTO> getAllProduct();
	
	PageResponseDTO<ProductResponseDTO> getAllProductsPaginated(int page, int size, String sortBy, String sortDir);
	
	List<ProductResponseDTO> getAvailableProducts();
	
	List<ProductResponseDTO> getProductByCategory(String category);
	
	List<ProductResponseDTO> getProductsByPriceRange(Double min, Double max);
	
	PageResponseDTO<ProductResponseDTO> searchProducts(String keyword, int page, int size);
	
	ProductResponseDTO updateProduct(Long id, UpdateProductRequestDTO productRequest);
	
	void updateStock(Long id, Integer quantity);
	
	List<ProductResponseDTO> getLowStockProducts(Integer threshold);
	
	boolean existsBySku(String sku);
	
	
}
