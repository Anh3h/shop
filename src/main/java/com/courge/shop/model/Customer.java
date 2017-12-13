package com.courge.shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 4678852901357132238L;

    @NonNull private String uuid;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @NonNull private String telephone;

}
