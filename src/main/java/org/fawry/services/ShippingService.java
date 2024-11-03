package org.fawry.services;

import java.util.List;

import org.fawry.interfaces.Shippable;

public class ShippingService {
    private static final double SHIPPING_RATE = 10.0; // Cost per kg

    public static double calculateShippingCost(List<Shippable> items) {
        return items.stream().mapToDouble(Shippable::getWeight).sum() * SHIPPING_RATE;
    }

    public static void shipItems(List<Shippable> items) {
        System.out.println("\nShipping items:");
        items.forEach(item ->
            System.out.printf("- %s (%.2f kg)%n", item.getName(), item.getWeight()));
    }
}
