package org.fawry.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.fawry.exceptions.CartEmptyException;
import org.fawry.exceptions.InsufficientBalanceException;
import org.fawry.exceptions.InsufficientStockException;
import org.fawry.exceptions.InvalidProductException;
import org.fawry.interfaces.Shippable;
import org.fawry.products.ExpirableProduct;
import org.fawry.services.ShippingService;

public class Customer {
    private String name;
    private BigDecimal balance;
    private final Cart cart;

    public Customer(String name, BigDecimal initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.name = name;
        this.balance = initialBalance;
        this.cart = new Cart();
    }

    public void addToCart(Product product, int quantity) {
        try {
            if (product == null) {
                throw new InvalidProductException("Product can't be null");
            }
            if (quantity <= 0) {
                throw new InvalidProductException("Quantity must be positive");
            }
            if (quantity > product.getQuantity()) {
                throw new InsufficientStockException("Not enough product in stock");
            }
            // If product is expirable, check if it's expired
            if (product instanceof ExpirableProduct) {
                ExpirableProduct expirableProduct = (ExpirableProduct) product;
                if (expirableProduct.isExpired()) {
                    throw new InvalidProductException("Product is expired");
                }
            }
        } catch (InvalidProductException | InsufficientStockException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        cart.addProduct(product, quantity);
    }

    public void checkout() {
        try {
            if (cart.isEmpty()) {
                throw new CartEmptyException("Cart is empty");
            }
        } catch (CartEmptyException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        cart.validateItems();

        BigDecimal subtotal = cart.getTotalPrice();
        List<Shippable> shippableItems = cart.getShippableItems();
        BigDecimal shippingFees = ShippingService.calculateShippingCost(shippableItems);
        BigDecimal total = subtotal.add(shippingFees);

        try {
            if (balance.compareTo(total) < 0) {
                throw new InsufficientBalanceException(
                        "Insufficient balance: Need $" + total + " but only have $" + balance);
            }
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Print shipping notice if there are shippable items
        if (!shippableItems.isEmpty()) {
            ShippingService.shipItems(shippableItems);
        }

        // Subtract total from balance
        deductFromBalance(total);

        // Print checkout receipt
        printReceipt(cart.getProducts(), subtotal, shippingFees, total, balance);

        // Update product quantities
        updateProductQuantities();

        cart.clear();
    }

    private void printReceipt(Map<Product, Integer> products, BigDecimal subtotal,
            BigDecimal shippingFees, BigDecimal total, BigDecimal remainingBalance) {
        System.out.println("** Checkout receipt **");

        products.forEach((product, quantity) -> System.out.printf("%dx %-14s %8.0f%n",
                quantity,
                product.getName(),
                product.getPrice().multiply(BigDecimal.valueOf(quantity)).doubleValue()));

        System.out.println("----------------------");
        System.out.printf("Subtotal         %8.0f%n", subtotal.doubleValue());
        System.out.printf("Shipping         %8.0f%n", shippingFees.doubleValue());
        System.out.printf("Amount           %8.0f%n", total.doubleValue());
        System.out.printf("Remaining balance: $%.2f%n", remainingBalance);
    }

    private void updateProductQuantities() {
        cart.getProducts().forEach((product, quantity) -> product.setQuantity(product.getQuantity() - quantity));
    }

    private void deductFromBalance(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        balance = balance.subtract(amount);
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
        return (Cart) cart;
    }
}
