package in.Scaliveswiftcart.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.Scaliveswiftcart.dto.request.ProductRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateProductRequestDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;
import in.Scaliveswiftcart.dto.response.ProductResponseDTO;
import in.Scaliveswiftcart.entity.Product;
import in.Scaliveswiftcart.exception.DuplicateResourceException;
import in.Scaliveswiftcart.exception.ResourceNotFoundException;
import in.Scaliveswiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	 private ProductRepository repo;
	 
	 @Autowired
	public ProductServiceImpl(ProductRepository repo) {
		this.repo = repo;
	}

	@Override
	public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
		if(productRequest.getSku() !=null && repo.existsBySku(productRequest.getSku())) {
			throw new DuplicateResourceException("Product", "sku", productRequest.getSku());
		}
		  Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.stockQuantity(productRequest.getStockQuantity())
				.category(productRequest.getCategory())
				.brand(productRequest.getBrand())
				.imageUrl(productRequest.getImageUrl())
				.sku(productRequest.getSku())
				.isAvailable(productRequest.getIsAvailableBoolean()!=null ? productRequest.getIsAvailableBoolean():true)
				.build();
		  
		  Product savedProduct= repo.save(product);
		  return mapToProductResponseDTO(savedProduct);
	}

	@Override
	public ProductResponseDTO getProductById(Long id) {
		Product product = findProductById(id);
		return mapToProductResponseDTO(product);
		
	}

	@Override
	public ProductResponseDTO getProductBySku(String sku) {
		Optional<Product> opt= repo.findBySku(sku);
		if(opt.isPresent()) {
			return mapToProductResponseDTO(opt.get());
		}
		throw new ResourceNotFoundException("Product", "sku", sku);
	}

	@Override
	public List<ProductResponseDTO> getAllProduct() {
		List<Product> products = repo.findAll();
		List<ProductResponseDTO> responselist = new ArrayList<>();
		for(Product product: products) {
			responselist.add(mapToProductResponseDTO(product));
		}
		return responselist;
	}

	@Override
	public PageResponseDTO<ProductResponseDTO> getAllProductsPaginated(int page, int size, String sortBy,
			String sortDir) {
		Pageable pageable = createPageable(page, size, sortBy, sortDir);
		Page<Product> productPage  = repo.findAll(pageable);
		return mapToPageResponseDTO(productPage);
	}

	@Override
	public List<ProductResponseDTO> getAvailableProducts() {
		List<Product> productList = repo.findByIsAvailableTrue();
		List<ProductResponseDTO> responseList = new ArrayList<>();
		for(Product product: productList) {
			responseList.add(mapToProductResponseDTO(product));
		}
		return responseList;
	}

	@Override
	public List<ProductResponseDTO> getProductByCategory(String category) {
		List<Product> productList = repo.findByCategoryIgnoreCase(category);
		List<ProductResponseDTO> responseList = new ArrayList<>();
		for(Product product: productList) {
			responseList.add(mapToProductResponseDTO(product));
		}
		return responseList;
	}

	@Override
	public List<ProductResponseDTO> getProductsByPriceRange(Double min, Double max) {
		List<Product> productList = repo.findByPriceBetween(min, max);
		List<ProductResponseDTO> responseList = new ArrayList<>();
		for(Product product: productList) {
			responseList.add(mapToProductResponseDTO(product));
		}
		return responseList;
	}

	@Override
	public PageResponseDTO<ProductResponseDTO> searchProducts(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> productPage = repo.searchProducts(keyword, pageable);
		return mapToPageResponseDTO(productPage);
	}

	@Override
	public ProductResponseDTO updateProduct(Long id, UpdateProductRequestDTO productRequest) {
		Product product = findProductById(id);
		
		if(productRequest.getName() == null &&
				productRequest.getDescription()== null &&
				productRequest.getPrice() == null &&
				productRequest.getStockQuantity() ==null &&
				productRequest.getCategory() == null &&
			    productRequest.getBrand() == null &&
			    productRequest.getImageUrl() == null &&
			    productRequest.getSku() == null &&
			    productRequest.getIsAvailable() == null)	{
			    	
			    	throw new IllegalArgumentException("At least one feild required for updation");
			    }
		
		if(productRequest.getName()!=null) {
			if(productRequest.getName().isBlank()) {
				throw new IllegalArgumentException("Name must be required for updation");
			}
			product.setName(productRequest.getName().trim());
		}
		
		if(productRequest.getDescription()!=null) {
			if(productRequest.getDescription().isBlank()) {
				throw new IllegalArgumentException("Description must be required for updation");
			}
			product.setDescription(productRequest.getDescription().trim());
		}
		
		if(productRequest.getPrice()!=null) {
			product.setPrice(productRequest.getPrice());
		}
		
		if(productRequest.getStockQuantity()!=null) {
			if(productRequest.getStockQuantity() <0) {
				throw new IllegalArgumentException("Product quantity cannot be negative");
			}
			product.setStockQuantity(productRequest.getStockQuantity());
		}
		
		if(productRequest.getCategory()!=null) {
			if(productRequest.getCategory().isBlank()) {
				throw new IllegalArgumentException("Product category cannot be left blank");
			}
			product.setCategory(productRequest.getCategory());
		}
		
		if(productRequest.getBrand()!=null) {
			if(productRequest.getBrand().isBlank()) {
				throw new IllegalArgumentException("Product brand cannot be left blank");
			}
			product.setBrand(productRequest.getBrand());
		}
		
		if(productRequest.getImageUrl()!=null) {
			if(productRequest.getImageUrl().isBlank()) {
				throw new IllegalArgumentException("Product imageUrl cannot be left blank");
			}
			product.setImageUrl(productRequest.getImageUrl());
		}
		if(productRequest.getSku()!=null) {
			if(productRequest.getSku().isBlank()) {
				throw new IllegalArgumentException("Product sku cannot be left blank");
			}
			String sku = productRequest.getSku();
			if(!sku.equals(product.getSku()) && repo.existsBySku(sku)) {
				throw new DuplicateResourceException("Product", "sku", sku);
			}
			product.setSku(sku);
 		}
		if(productRequest.getIsAvailable()!=null) {
			product.setIsAvailable(productRequest.getIsAvailable());
		}
		
		Product updatedProduct = repo.save(product);
		return mapToProductResponseDTO(updatedProduct);
	}

	@Override
	public void updateStock(Long id, Integer quantity) {
		Product product = findProductById(id);
		int newQuantity = product.getStockQuantity() + quantity;
		if(newQuantity <0) {
			throw new DuplicateResourceException("Product quantity can not be negative"+product.getStockQuantity());
		}
		product.setStockQuantity(newQuantity);
		repo.save(product);
	}

	@Override
	public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {
		if(threshold <0) {
			throw new IllegalArgumentException("threshold cannot be negative: "+ threshold);
		}
		List<Product> productList = repo.findLowStockProducts(threshold);
		List<ProductResponseDTO> responseList = new ArrayList<>();
		for(Product product: productList) {
			responseList.add(mapToProductResponseDTO(product));
		}
		return responseList;
	}

	@Override
	public boolean existsBySku(String sku) {
		return repo.existsBySku(sku);
	}
	
	private Product findProductById(Long id) {
		Optional<Product> opt = repo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new ResourceNotFoundException("Product", "id", id);
	}
	
	private Pageable createPageable(int page, int size, String sortBy ,String sortDir ) {
		Sort sort;	
		if(sortDir.equalsIgnoreCase("desc")) {
			sort = Sort.by(sortBy).descending();
		}else {
			sort = Sort.by(sortBy).ascending();
		}
		return PageRequest.of(page, size, sort);
	}
	
	private ProductResponseDTO mapToProductResponseDTO(Product product) {
		return ProductResponseDTO.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.category(product.getCategory())
				.brand(product.getBrand())
				.imageUrl(product.getImageUrl())
				.sku(product.getSku())
			    .isAvailable(product.getIsAvailable())
			    .inStock(product.getStockQuantity()>0)
			    .build();
	}
	
	private PageResponseDTO<ProductResponseDTO> mapToPageResponseDTO(Page<Product> productPage) {
		List<ProductResponseDTO> products = new ArrayList<>();
		 for(Product product: productPage) {
			 products.add(mapToProductResponseDTO(product));
		 }
	
		return PageResponseDTO.<ProductResponseDTO>builder()
				  .content(products)
				  .pageNumber(productPage.getNumber())
				  .pageSize(productPage.getSize())
				  .totalElements(productPage.getTotalElements())
				  .totalPages(productPage.getTotalPages())
				  .first(productPage.isFirst())
				  .last(productPage.isLast())
				  .hasNext(productPage.hasNext())
	              .hasPrivious(productPage.hasPrevious())
	              .build();
	
	}
}
