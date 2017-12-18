package com.courge.shop.controller;

import com.courge.shop.Exception.BadRequestException;
import com.courge.shop.Exception.NotFoundServiceException;
import com.courge.shop.model.Product;
import com.courge.shop.service.command.ProductCommand;
import com.courge.shop.service.query.ProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @RequestMapping ( method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "size", required = false) Integer size) {
        if (page == null || size == null) {
            page = 1;
            size = 50;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<Product> products = this.productQuery.findAll(page, size);
        if (page > products.getTotalPages()) {
            throw NotFoundServiceException.create("Page number does not exist");
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Product> getProduct( @PathVariable("productId") String productId ) {
        Product product = this.productQuery.findById(productId);
        if (product == null ) {
            throw NotFoundServiceException.create("Product with id:{0} does not exist", productId);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Product> updateProduct( @RequestBody Product product,
                                                 @PathVariable("productId") String productId ) {
        if (this.productQuery.findById(productId) == null) {
            throw NotFoundServiceException.create("Product with id:{0} does not exist", productId);
        }
        Product updatedProduct= this.productCommand.updateProductCommand(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @RequestMapping( value = "/{productId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<HttpStatus> deleteProduct( @PathVariable("productId") String productId ) {
        this.productCommand.deleteProductCommand(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
