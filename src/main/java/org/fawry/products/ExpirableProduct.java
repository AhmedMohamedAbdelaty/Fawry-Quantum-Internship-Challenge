package org.fawry.products;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.fawry.interfaces.Expirable;
import org.fawry.models.Product;

public class ExpirableProduct extends Product implements Expirable {
    private LocalDate expirationDate;

    public ExpirableProduct(String name, BigDecimal price, int quantity, LocalDate expirationDate) {
        super(name, price, quantity);
        setExpirationDate(expirationDate);
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        try {
            if (expirationDate == null) {
                throw new IllegalArgumentException("Expiration date can't be null");
            }

            // if (expirationDate.isBefore(LocalDate.now())) {
            //     throw new IllegalArgumentException("Expiration date can't be in the past");
            // }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        this.expirationDate = expirationDate;
    }
}
