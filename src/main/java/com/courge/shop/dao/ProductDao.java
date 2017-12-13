package com.courge.shop.dao;

import com.courge.shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDao {

    Product createProduct( Product newProduct );
    Page<Product> findAll( Pageable pageable );
    Product findById( String uuid );
    Product findByName( String name );
    Product updateProduct( Product updatedProduct );
    String deleteProduct( String uuid );

}
