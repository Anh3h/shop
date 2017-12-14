package com.courge.shop.service.command.implementation;

import com.courge.shop.Exception.ConflictException;
import com.courge.shop.dao.ProductDao;
import com.courge.shop.model.Product;
import com.courge.shop.service.command.ProductCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductCommandHandler implements ProductCommand {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product createProductCommand(Product newProduct) {
        if ( this.productDao.findByName(newProduct.getName()) == null ) {
            newProduct.setUuid(UUID.randomUUID().toString());
            return this.productDao.createProduct(newProduct);
        }
        throw ConflictException.create("Product {0} already exist", newProduct);
    }

    @Override
    public Product updateProductCommand(Product updatedProduct) {
        Product product = this.productDao.updateProduct(updatedProduct);
        return product;
    }

    @Override
    public void deleteProductCommand(String uuid) {
        this.productDao.deleteProduct(uuid);
    }
}
