package org.furniture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FurnitureMap extends FurnitureCollection {
    private Map<Integer, Furniture> furnitureMap = new HashMap<>();

    @Override
    public void addFurniture(Furniture furniture) {
        furnitureMap.put(furniture.getId(), furniture);
    }

    @Override
    public void removeFurniture(int id) {
        furnitureMap.remove(id);
    }

    @Override
    public List<Furniture> getAllFurniture() {
        return new ArrayList<>(furnitureMap.values());
    }

    public void sortById() {
        furnitureMap = new TreeMap<>(furnitureMap);
    }
}
