package org.furniture.utils;

import org.furniture.Furniture;

public class ConcreteFurniture extends Furniture {

    public ConcreteFurniture(int id, String type, int weight, int amount, double price) {
        super(id, type, weight, amount, price);
    }

    @Override
    public String toString() {
        return "Furniture ID: " + getId() + ", Type: " + getType() + ", Weight: " + getWeight() +
               ", Amount: " + getAmount() + ", Price: " + getPrice();
    }
}
