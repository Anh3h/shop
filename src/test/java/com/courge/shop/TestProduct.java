package com.courge.shop;

import com.courge.shop.generators.ProductGenerator;
import com.courge.shop.model.Product;
import com.courge.shop.service.command.ProductCommand;
import com.courge.shop.service.query.ProductQuery;
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
public class TestProduct {

    @Autowired
    private ProductCommand productCommand;

    @Autowired
    private ProductQuery productQuery;

    @Test
    public void testCreateValidProduct() {
        Product newProduct = ProductGenerator.generateRandomProduct();
        Product createdProduct = productCommand.createProductCommand(newProduct);
        Assert.assertNotNull(createdProduct);
    }

    @Test
    public void testReadProducts() {
        Stream.of(
                ProductGenerator.generateRandomProduct(),
                ProductGenerator.generateRandomProduct(),
                ProductGenerator.generateRandomProduct()
        ).forEach(product -> productCommand.createProductCommand(product));
        Page<Product> products = productQuery.findAll(1, 1);
        Assert.assertTrue(products.getTotalElements()>3);
    }

    @Test
    public void testReadProduct() {
        Product newProduct = ProductGenerator.generateRandomProduct();
        productCommand.createProductCommand(newProduct);
        Product createdProdcut = productQuery.findById(newProduct.getUuid());
        Assert.assertEquals(newProduct.getName(), createdProdcut.getName());
    }

    @Test
    public void testValidUpdateProduct() {
        Product newProduct = ProductGenerator.generateRandomProduct();
        Product createdProduct = productCommand.createProductCommand(newProduct);
        createdProduct.setPrice(ProductGenerator.generatePrice());
        Product updatedProduct = productCommand.updateProductCommand(createdProduct);
        Assert.assertEquals(createdProduct.getPrice(), updatedProduct.getPrice());
    }

}
