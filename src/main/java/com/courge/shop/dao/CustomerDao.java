package com.courge.shop.dao;

import com.courge.shop.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerDao {

    Customer createCustomer( Customer newCustomer );
    Page<Customer> findAll( Pageable pageable );
    Customer findByUUID( String uuid );
    Customer findByEmail( String email );
    Customer findByTelephone( String telephone );
    Customer updateCustomer( Customer updatedCustomer );
    String deleteCustomer( String uuid );

}