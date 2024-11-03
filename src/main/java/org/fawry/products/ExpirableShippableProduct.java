package org.fawry.products;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.fawry.interfaces.Expirable;
import org.fawry.interfaces.Shippable;
import org.fawry.models.Product;

public class ExpirableShippableProduct extends Product implements Expirable, Shippable {
    private double weight;
    private LocalDate expirationDate;

    public ExpirableShippableProduct(String name, BigDecimal price, int quantity, double weight,
            LocalDate expirationDate) {
        super(name, price, quantity);
        setWeight(weight);
        setExpirationDate(expirationDate);
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

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public void setExpirationDate(LocalDate expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date can't be null");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date can't be in the past");
        }

        this.expirationDate = expirationDate;
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}
