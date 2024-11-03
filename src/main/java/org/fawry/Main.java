package org.fawry;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.fawry.models.Customer;
import org.fawry.models.Product;
import org.fawry.products.ExpirableProduct;
import org.fawry.products.ExpirableShippableProduct;
import org.fawry.products.ShippableProduct;

public class Main {
    public static void main(String[] args) {
        // Test basic checkout process
        testBasicCheckout();

        // Test invalid inputs, (null product, negative quantity, zero quantity)
        testInvalidInputs();

        // Test adding expired to the cart
        testExpiredProduct();

        // Test adding product with insufficient stock
        // only one product will be added to the cart
        testInsufficientStock();

        // Test cart operations (add, remove)
        testCartOperations();
    }

    private static void testBasicCheckout() {
        // Create products
        ShippableProduct laptop = new ShippableProduct("Laptop", BigDecimal.valueOf(30000), 5, 5);
        ExpirableProduct bread = new ExpirableProduct("Bread", BigDecimal.valueOf(5), 20, LocalDate.now().plusDays(2));
        ExpirableShippableProduct cheese = new ExpirableShippableProduct("Cheese", BigDecimal.valueOf(20), 20, 2.325,
                LocalDate.now().plusDays(10));

        // Test checkout process
        Customer customer = new Customer("Ahmed", BigDecimal.valueOf(125000));
        customer.addToCart(laptop, 1);
        customer.addToCart(bread, 10);
        customer.addToCart(cheese, 3);

        // Test successful checkout
        customer.checkout();
    }

    private static void testInvalidInputs() {
        Customer customer = new Customer("Ahmed", BigDecimal.valueOf(1000));
        ExpirableProduct bread = new ExpirableProduct("Bread", BigDecimal.valueOf(5), 20, LocalDate.now().plusDays(2));
        Product nullProduct = null;

        // Test invalid scenarios
        customer.addToCart(nullProduct, 1); // Null product
        customer.addToCart(bread, -5); // Negative quantity
        customer.addToCart(bread, 0); // Zero quantity

        customer.checkout();
    }

    private static void testExpiredProduct() {
        Customer customer = new Customer("Ahmed", BigDecimal.valueOf(1000));
        ExpirableProduct expiredMilk = new ExpirableProduct("Milk", BigDecimal.valueOf(10), 10,
                LocalDate.now().minusDays(1));

        customer.addToCart(expiredMilk, 1); // Should fail
        customer.checkout();
    }

    private static void testInsufficientStock() {
        Customer customer = new Customer("Sara", BigDecimal.valueOf(100));
        ShippableProduct laptop = new ShippableProduct("Laptop", BigDecimal.valueOf(1), 5, 1);

        customer.addToCart(laptop, 5); // Should work
        customer.checkout();

        customer.addToCart(laptop, 1); // Should fail (not enough stock)
        customer.checkout();
    }

    private static void testCartOperations() {
        Customer customer = new Customer("Sara", BigDecimal.valueOf(50000));
        ShippableProduct laptop = new ShippableProduct("Laptop", BigDecimal.valueOf(30000), 5, 5);

        customer.addToCart(laptop, 2);
        customer.getCart().removeProduct(laptop);
        customer.checkout(); // Cart is empty
    }
}
