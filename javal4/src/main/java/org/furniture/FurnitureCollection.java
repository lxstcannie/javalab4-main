package org.furniture;

import java.util.ArrayList;
import java.util.List;

public class FurnitureCollection {
    private List<Furniture> furnitures = new ArrayList<>();

    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
    }

    public void removeFurniture(int id) {
        furnitures.removeIf(h -> h.getId() == id);
    }

    public List<Furniture> getAllFurniture() {
        return furnitures;
    }

    public Furniture getFurnitureById(int id) {
        for (Furniture furniture : furnitures) {
            if (furniture.getId() == id) {
                return furniture;
            }
        }
        return null; 
    }
}
