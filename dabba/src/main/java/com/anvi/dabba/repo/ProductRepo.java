package com.anvi.dabba.repo;

import com.anvi.dabba.model.Product;
import com.anvi.dabba.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    List<ProductWrapper> getAllProducts();

}
