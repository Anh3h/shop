package com.courge.shop.service.query;

import com.courge.shop.model.Product;
import org.springframework.data.domain.Page;

public interface ProductQuery {

    Page<Product> findAll( int pageNumber, int pageSize );
    Product findById( String uuid );

}
