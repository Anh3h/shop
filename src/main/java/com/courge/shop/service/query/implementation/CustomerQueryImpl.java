package com.courge.shop.service.query.implementation;

import com.courge.shop.dao.CustomerDao;
import com.courge.shop.model.Customer;
import com.courge.shop.service.query.CustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerQueryImpl implements CustomerQuery {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Page<Customer> findAll(int pageNumber, int pageSize) {
        return this.customerDao.findAll(new PageRequest(pageNumber, pageSize));
    }

    @Override
    public Customer findById(String uuid) {
        return customerDao.findByUUID(uuid);
    }

}