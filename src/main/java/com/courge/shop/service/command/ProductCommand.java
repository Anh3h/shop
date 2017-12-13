package com.courge.shop.service.command;

import com.courge.shop.model.Product;

public interface ProductCommand {

    Product createProductCommand( Product newProduct );
    Product updateProductCommand( Product updatedProduct );
    void deleteProductCommand( String uuid );
}
