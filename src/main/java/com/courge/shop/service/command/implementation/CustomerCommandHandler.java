package com.courge.shop.service.command.implementation;

import com.courge.shop.dao.CustomerDao;
import com.courge.shop.model.Customer;
import com.courge.shop.service.command.CustomerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommandHandler implements CustomerCommand {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Customer createCustomerCommand(Customer newCustomer) {
        if ( this.customerDao.findByEmail(newCustomer.getEmail()) == null &&
                this.customerDao.findByTelephone(newCustomer.getTelephone()) == null ) {

            return this.customerDao.createCustomer(newCustomer);
        }
        return null;

    }

    @Override
    public Customer updateCustomerCommand(Customer updatedCustomer) {
        Customer customer = this.customerDao.updateCustomer(updatedCustomer);
        if ( customer != null ) {
            return customer;
        }
        return null;
    }

    @Override
    public void deleteCustomerCommand(String uuid) {
        this.customerDao.deleteCustomer(uuid);
    }

}
