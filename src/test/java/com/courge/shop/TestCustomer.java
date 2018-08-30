package com.courge.shop;

import com.courge.shop.generators.CustomerGenerator;
import com.courge.shop.model.Customer;
import com.courge.shop.service.command.CustomerCommand;
import com.courge.shop.service.query.CustomerQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCustomer {

    @Autowired
    private CustomerCommand customerCommand;

    @Autowired
    private CustomerQuery customerQuery;

    @Test
    public void testCreateValidCustomer() {
        Customer newCustomer = CustomerGenerator.generateRandomCustomer();
        Customer createdCustomer = customerCommand.createCustomerCommand(newCustomer);
        Assert.assertNotNull(createdCustomer);
    }

    @Test
    public void testReadCustomers() {
        Stream.of(CustomerGenerator.generateRandomCustomer(),
                CustomerGenerator.generateRandomCustomer(),
                CustomerGenerator.generateRandomCustomer())
                .forEach(
                        customerGenerator -> customerCommand.createCustomerCommand(customerGenerator)
                );

        Page<Customer> customers = customerQuery.findAll(1,1);
        Assert.assertTrue(customers.getTotalElements() >= 3);
    }

    @Test
    public void testReadCustomer() {
        Customer newCustomer = CustomerGenerator.generateRandomCustomer();
        newCustomer = customerCommand.createCustomerCommand(newCustomer);
        Customer createdCustomer = customerQuery.findById(newCustomer.getUuid());
        Assert.assertEquals( newCustomer.getEmail(), createdCustomer.getEmail());
    }

    @Test
    public void testValidUpdateCustomer() {
        Customer newCustomer = CustomerGenerator.generateRandomCustomer();
        Customer createdCustomer = customerCommand.createCustomerCommand(newCustomer);

        createdCustomer.setEmail(CustomerGenerator.generateEmail());
        Customer updateCustomer = customerCommand.updateCustomerCommand(createdCustomer);
        Assert.assertEquals(createdCustomer.getEmail(), updateCustomer.getEmail());
    }

}
