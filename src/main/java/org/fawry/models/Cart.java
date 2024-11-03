package org.fawry.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fawry.exceptions.InsufficientStockException;
import org.fawry.exceptions.ProductExpiredException;
import org.fawry.interfaces.Expirable;
import org.fawry.interfaces.Shippable;

public class Cart {
    private Map<Product, Integer> products = new HashMap<>();

    // Add a product to the cart with a specific quantity
    public void addProduct(Product product, int quantity) {
        products.put(product, quantity);
    }

    // Remove a product from the cart
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Get the total price of the cart
    public BigDecimal getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            totalPrice += entry.getKey().getPrice().doubleValue() * entry.getValue();
        }
        return BigDecimal.valueOf(totalPrice);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void validateItems() {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            try {
                if (product instanceof Expirable && ((Expirable) product).isExpired()) {
                    throw new ProductExpiredException("Product " + product.getName() + " is expired");
                }

                if (quantity > product.getQuantity()) {
                    throw new InsufficientStockException("Product " + product.getName() + " is out of stock");
                }
            } catch (ProductExpiredException | InsufficientStockException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public List<Shippable> getShippableItems() {
        List<Shippable> shippableItems = new ArrayList<>();
        products.forEach((product, quantity) -> {
            if (product instanceof Shippable) {
                for (int i = 0; i < quantity; i++) {
                    shippableItems.add((Shippable) product);
                }
            }
        });
        return shippableItems;
    }

    public Map<Product, Integer> getProducts() {
        return new HashMap<>(products);
    }
}
