package org.furniture;

import java.util.Objects;

public abstract class Furniture {
    protected int id;
    protected String type;
    protected int weight;
    protected int amount;
    protected double price;

    public Furniture(int id, String type, int weight, int amount, double price) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    Furniture furniture = (Furniture) obj;
    return id == furniture.id &&
           Double.compare(furniture.weight, weight) == 0 &&
           amount == furniture.amount &&
           Double.compare(furniture.price, price) == 0 &&
           type.equals(furniture.type); // Проверяем ключевые поля
    }

    @Override
    public int hashCode() {
    return Objects.hash(id, weight, amount, price, type);
    }

}
