package org.fawry;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.fawry.models.Customer;
import org.fawry.products.ExpirableProduct;
import org.fawry.products.ExpirableShippableProduct;
import org.fawry.products.ShippableProduct;

public class Main {
    public static void main(String[] args) {
        // Shippable product
        ShippableProduct laptop = new ShippableProduct("Laptop", BigDecimal.valueOf(30000), 5, 5);

        // Expirable product
        ExpirableProduct bread = new ExpirableProduct("Bread", BigDecimal.valueOf(5), 20,
                LocalDate.now().plusDays(2));

        // Expirable Shippable product
        ExpirableShippableProduct cheese = new ExpirableShippableProduct("Cheese", BigDecimal.valueOf(20), 20,
                2.325,
                LocalDate.now().plusDays(10));

        Customer ahmed = new Customer("Ahmed", BigDecimal.valueOf(125000));
        ahmed.addToCart(laptop, 1);
        ahmed.addToCart(bread, 10);
        ahmed.addToCart(cheese, 3);

        ahmed.checkout();
    }
}
