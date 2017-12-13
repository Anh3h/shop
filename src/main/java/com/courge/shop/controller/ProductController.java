package com.courge.shop.controller;

import com.courge.shop.model.Product;
import com.courge.shop.service.command.ProductCommand;
import com.courge.shop.service.query.ProductQuery;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductCommand productCommand;

    @Autowired
    private ProductQuery productQuery;

    @RequestMapping( method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Product> createProduct(@RequestBody Product product ) {
        Product createdProduct = this.productCommand.createProductCommand(product);
        if ( createdProduct == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @RequestMapping ( method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getProducts(@RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        if (page <= 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<Product> products = this.productQuery.findAll(page, size);
        if (page > products.getTotalPages()) {
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getProduct( @PathVariable("productId") String productId ) {
        Product product = this.productQuery.findById(productId);

        if (product == null ) {
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> updateProduct( @RequestBody Product product ) {
        Product updatedProduct= this.productCommand.updateProductCommand(product);

        if ( updatedProduct == null ){
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Product> deleteProduct( @PathVariable("productId") String productId ) {
        this.productCommand.deleteProductCommand(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    Map<String, String> errorMessage(){
        Map<String, String> errorMsg = new HashMap<>();
        errorMsg.put("code", "404");
        errorMsg.put("message", "Product not found");
        return errorMsg;
    }

}
