package com.anvi.dabba.serviceImpl;

import com.anvi.dabba.jwt.JwtFilter;
import com.anvi.dabba.model.Product;
import com.anvi.dabba.repo.ProductRepo;
import com.anvi.dabba.service.ProductService;
import com.anvi.dabba.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(this.validateProductMap(requestMap,false)){
                    productRepo.save(this.getProductFromMap(requestMap,false));
                    return new ResponseEntity<>("New Product was added successfully", HttpStatus.OK);
                }
                return new ResponseEntity<>("Invalid Data",HttpStatus.BAD_REQUEST);
            }
            else return new ResponseEntity<>("Unauthorized Access",HttpStatus.UNAUTHORIZED);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productRepo.getAllProducts(),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(this.validateProductMap(requestMap,true)){
                    Optional<Product> productRepoById=productRepo.findById(Integer.parseInt(requestMap.get("id")));
                    if(productRepoById.isPresent()){
                        Product productFromMap=this.getProductFromMap(requestMap,true);
                        productFromMap.setStatus(productRepoById.get().getStatus());
                        productRepo.save(productFromMap);
                        return new ResponseEntity<>("Product Updated Successfully",HttpStatus.OK) ;

                    }else return new ResponseEntity<>("Product with id " + requestMap.get("id") + " does not exists", HttpStatus.NOT_FOUND);
                }else return new ResponseEntity<>("Data is Invalid",HttpStatus.BAD_REQUEST);
            }else return new ResponseEntity<>("Unauthorized Access",HttpStatus.UNAUTHORIZED);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong in updating logic",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Product> product=productRepo.findById(id);
                if(product.isPresent()){
                    productRepo.deleteById(id);
                    return new ResponseEntity<>("Product was deleted successfully",HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Product with id:"+id+"does not exist",HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("You are not authorized for this action",HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong due to server",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Product product=new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else product.setStatus("true");
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;

    }

    private boolean validateProductMap(Map<String,String> requestMap,boolean validateId){
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id")&& validateId){
                return true;
            }else return !validateId;
        }
        return false;
    }
}
