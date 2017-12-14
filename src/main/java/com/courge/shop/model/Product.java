package com.courge.shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Product {

    private String uuid;
    @NonNull private String name;
    @NonNull private Double price;
    @NonNull private Long amount;

}
