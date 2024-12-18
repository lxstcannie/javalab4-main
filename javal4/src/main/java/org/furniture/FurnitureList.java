package org.furniture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FurnitureList extends FurnitureCollection {

    public FurnitureList() {
    }
    private List<Furniture> furnitures = new ArrayList<>();

    @Override
    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
    }

    @Override
    public void removeFurniture(int id) {
        furnitures.removeIf(h -> h.getId() == id);
    }

    @Override
    public List<Furniture> getAllFurniture() {
        return furnitures;
    }
    public void sortByPrice() {
        furnitures.sort(Comparator.comparingDouble(Furniture::getPrice));
    }

    public void sortByWeight() {
        furnitures.sort(Comparator.comparingInt(Furniture::getWeight));
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
