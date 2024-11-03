package org.fawry.products;

import java.math.BigDecimal;

import org.fawry.interfaces.Shippable;
import org.fawry.models.Product;

public class ShippableProduct extends Product implements Shippable {
    private double weight;

    public ShippableProduct(String name, BigDecimal price, int quantity, double weight) {
        super(name, price, quantity);
        setWeight(weight);
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
