package com.courge.shop.controller;

import com.courge.shop.Exception.BadRequestException;
import com.courge.shop.Exception.NotFoundServiceException;
import com.courge.shop.model.Customer;
import com.courge.shop.service.command.CustomerCommand;
import com.courge.shop.service.query.CustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerCommand customerCommand;

    @Autowired
    private CustomerQuery customerQuery;

    @RequestMapping( method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Customer> createCustomer( @RequestBody Customer customer ) {
        Customer createdCustomer = this.customerCommand.createCustomerCommand(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @RequestMapping ( method = RequestMethod.GET,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getCustomers( @RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        if( page == null || size == null ) {
            page = 1;
            size = 50;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<Customer> customers = this.customerQuery.findAll(page, size);
        if (page > customers.getTotalPages()) {
            throw BadRequestException.create("Page number {0} should not be greater that total number of pages", page);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.GET,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getCustomer( @PathVariable("customerId") String customerId ) {
        Customer customer = this.customerQuery.findById(customerId);
        if (customer == null ) {
            throw NotFoundServiceException.create("Customer with id: {0} does not exist", customerId);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> updateCustomer( @RequestBody Customer customer,
                                                  @PathVariable("customerId") String customerId ) {
        if ( this.customerQuery.findById(customerId) == null ){
            throw NotFoundServiceException.create("Customer with id: {0} does not exist.", customerId);
        }
        Customer updatedCustomer = this.customerCommand.updateCustomerCommand(customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Customer> deleteCustomer( @PathVariable("customerId") String customerId ) {
        this.customerCommand.deleteCustomerCommand(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
