package com.courge.shop.controller;

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
        if ( createdCustomer == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @RequestMapping ( method = RequestMethod.GET,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getCustomers( @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        if (page <= 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<Customer> customers = this.customerQuery.findAll(page, size);
        if (page > customers.getTotalPages()) {
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.GET,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> getCustomer( @PathVariable("customerId") String customerId ) {
        Customer customer = this.customerQuery.findById(customerId);
        if (customer == null ) {
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> updateCustomer( @RequestBody Customer customer ) {
        Customer updatedCustomer = this.customerCommand.updateCustomerCommand(customer);
        if ( updatedCustomer == null ){
            return new ResponseEntity<>(this.errorMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @RequestMapping( value = "/{customerId}", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Customer> deleteCustomer( @PathVariable("customerId") String customerId ) {
        this.customerCommand.deleteCustomerCommand(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    Map<String, String> errorMessage(){
        Map<String, String> errorMsg = new HashMap<>();
        errorMsg.put("code", "404");
        errorMsg.put("message", "Product not found");
        return errorMsg;
    }

}
