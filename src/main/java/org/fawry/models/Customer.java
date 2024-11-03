package org.fawry.models;

import java.math.BigDecimal;
import java.util.List;

import org.fawry.exceptions.CartEmptyException;
import org.fawry.exceptions.InsufficientBalanceException;
import org.fawry.exceptions.InsufficientStockException;
import org.fawry.exceptions.InvalidProductException;
import org.fawry.interfaces.Shippable;
import org.fawry.services.ShippingService;

public class Customer {
    private String name;
    private BigDecimal balance;
    private Cart cart;

    public Customer(String name, BigDecimal balance) {
        setName(name);
        setBalance(balance);
        cart = new Cart();
    }

    public void addToCart(Product product, int quantity) {
        if (product == null) {
            throw new InvalidProductException("Product can't be null");
        }
        if (quantity <= 0) {
            throw new InvalidProductException("Quantity must be positive");
        }
        if (quantity > product.getQuantity()) {
            throw new InsufficientStockException("Not enough product in stock");
        }
        cart.addProduct(product, quantity);
    }

    public void checkout() {
        if (cart.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        cart.validateItems();

        BigDecimal subtotal = BigDecimal.valueOf(cart.getTotalPrice());
        List<Shippable> shippableItems = cart.getShippableItems();
        BigDecimal shippingFees = BigDecimal.valueOf(ShippingService.calculateShippingCost(shippableItems));
        BigDecimal total = subtotal.add(shippingFees);

        if (balance.compareTo(total) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Print checkout details
        System.out.println("\nCheckout Details:");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Shipping fees: $%.2f%n", shippingFees);
        System.out.printf("Total paid: $%.2f%n", total);

        // Process payment
        balance = balance.subtract(total);
        System.out.printf("Remaining balance: $%.2f%n", balance);

        // Process shipping
        if (!shippableItems.isEmpty()) {
            ShippingService.shipItems(shippableItems);
        }

        // Update product quantities
        cart.getProducts().forEach((product, quantity) -> product.setQuantity(product.getQuantity() - quantity));

        cart = new Cart();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
