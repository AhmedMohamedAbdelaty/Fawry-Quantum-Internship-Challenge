package org.fawry.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fawry.interfaces.Shippable;

public final class ShippingService {
    private static final BigDecimal SHIPPING_RATE_PER_KG = BigDecimal.valueOf(10.0);

    private ShippingService() {
        // Prevent instantiation of utility class
    }

    public static BigDecimal calculateShippingCost(List<Shippable> items) {
        return items.stream()
                .map(item -> BigDecimal.valueOf(item.getWeight())
                        .multiply(SHIPPING_RATE_PER_KG))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static void shipItems(List<Shippable> items) {
        if (items.isEmpty()) {
            return;
        }

        System.out.println("**   Shipment notice   **");
        Map<String, Long> itemCounts = getItemCounts(items);
        Map<String, Double> itemWeights = getItemWeights(items);
        printShipmentDetails(itemCounts, itemWeights);
    }

    private static Map<String, Long> getItemCounts(List<Shippable> items) {
        return items.stream()
                .collect(Collectors.groupingBy(Shippable::getName, Collectors.counting()));
    }

    private static Map<String, Double> getItemWeights(List<Shippable> items) {
        return items.stream()
                .collect(Collectors.groupingBy(Shippable::getName,
                        Collectors.averagingDouble(Shippable::getWeight)));
    }

    private static void printShipmentDetails(Map<String, Long> itemCounts, Map<String, Double> itemWeights) {
        double totalWeight = 0;
        for (Map.Entry<String, Long> entry : itemCounts.entrySet()) {
            String name = entry.getKey();
            long quantity = entry.getValue();
            double weight = itemWeights.get(name);
            System.out.printf("%dx %-14s %8.0fg%n", quantity, name, weight * 1000);
            totalWeight += weight * quantity;
        }
        System.out.printf("Total weight %.1fkg%n%n", totalWeight);
    }
}
