package com.anvi.dabba.controller;

import com.anvi.dabba.model.Product;
import com.anvi.dabba.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/v1")
public interface ProductControl {
    @PostMapping("/product/add")
    public ResponseEntity<String> addProduct(@RequestBody Map<String,String> requestMap);

    @GetMapping("/product/get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct();

    @PostMapping("/product/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id);

//    Optional<Product> findById(int id);
//
//    void save(Product productFromMap);
//
//    List<ProductWrapper> getAllProducts();
//
//    void deleteById(Integer id);
}
