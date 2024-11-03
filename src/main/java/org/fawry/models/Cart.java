package org.fawry.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public double getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            totalPrice += entry.getKey().getPrice().doubleValue() * entry.getValue();
        }
        return totalPrice;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void validateItems() {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (product instanceof Expirable && ((Expirable) product).isExpired()) {
                throw new IllegalStateException("Product " + product.getName() + " is expired");
            }

            if (quantity > product.getQuantity()) {
                throw new IllegalStateException("Product " + product.getName() + " is out of stock");
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
