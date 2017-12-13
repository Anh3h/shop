package com.courge.shop.service.command.implementation;

import com.courge.shop.dao.ProductDao;
import com.courge.shop.model.Product;
import com.courge.shop.service.command.ProductCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandHandler implements ProductCommand {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product createProductCommand(Product newProduct) {
        if ( this.productDao.findByName(newProduct.getName()) == null ) {
            return this.productDao.createProduct(newProduct);
        }
        return null;
    }

    @Override
    public Product updateProductCommand(Product updatedProduct) {
        Product product = this.productDao.updateProduct(updatedProduct);
        if ( product != null ) {
            return product;
        }
        return null;
    }

    @Override
    public void deleteProductCommand(String uuid) {
        this.productDao.deleteProduct(uuid);
    }
}
