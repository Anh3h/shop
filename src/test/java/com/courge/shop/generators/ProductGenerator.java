package com.courge.shop.generators;

import com.courge.shop.model.Product;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class ProductGenerator {

    public static Product generateRandomProduct() {
        final Product product = new Product();
        product.setName(RandomStringUtils.randomAlphabetic(10));
        product.setAmount(RandomUtils.nextLong(0, 50));
        product.setPrice(generatePrice());

        return product;
    }

    public static Double generatePrice() {
        return RandomUtils.nextDouble(5, 100);
    }

}
