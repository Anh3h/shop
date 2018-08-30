package com.courge.shop.generators;

import com.courge.shop.model.Customer;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor
public final class CustomerGenerator {

    public static Customer generateRandomCustomer() {
        final Customer customer = new Customer();
        customer.setFirstName(RandomStringUtils.randomAlphabetic(8));
        customer.setLastName(RandomStringUtils.randomAlphabetic(9));
        customer.setTelephone(RandomStringUtils.randomNumeric(9));
        customer.setEmail(generateEmail());

        return customer;
    }

    public static String generateEmail(){
        return RandomStringUtils.randomAlphanumeric(15) + "@" +
                RandomStringUtils.randomAlphabetic(5) + "com";
    }

}