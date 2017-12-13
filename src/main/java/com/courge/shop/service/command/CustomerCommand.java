package com.courge.shop.service.command;

import com.courge.shop.model.Customer;

public interface CustomerCommand {

    Customer createCustomerCommand( Customer newCustomer );
    Customer updateCustomerCommand( Customer updatedCustomer );
    void deleteCustomerCommand( String uuid );

}
