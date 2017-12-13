package com.courge.shop.service.query.implementation;

import com.courge.shop.dao.ProductDao;
import com.courge.shop.model.Product;
import com.courge.shop.service.query.ProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryImpl implements ProductQuery {

    @Autowired
    private ProductDao productDao;

    @Override
    public Page<Product> findAll(int pageNumber, int pageSize) {
        return this.productDao.findAll( new PageRequest(pageNumber, pageSize));
    }

    @Override
    public Product findById(String uuid) {
        return this.productDao.findById(uuid);
    }

}
