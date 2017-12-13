package com.courge.shop.service.query;

import com.courge.shop.model.Customer;
import org.springframework.data.domain.Page;

public interface CustomerQuery {

    Page<Customer> findAll( int pageNumber, int pageSize );
    Customer findById( String uuid );

}
