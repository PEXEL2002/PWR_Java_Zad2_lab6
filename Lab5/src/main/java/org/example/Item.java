package org.example;

public class Item {
    private int id;
    private int weight;
    private int price;
    private float priceToWeightRatio;

    public Item(int id, int weight, int price) {
        this.id = id;
        this.weight = weight;
        this.price = price;
        this.priceToWeightRatio = (float) price / (float) weight;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
        updateRatio();
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
        updateRatio();
    }
    public float getPriceToWeightRatio() {
        return priceToWeightRatio;
    }
    private void updateRatio() {
        this.priceToWeightRatio = (float) this.price / (float) this.weight;
    }
    @Override
    public String toString() {
        return  "ID:"+ this.id+" w=" + weight + ", p=" + price;
    }
}
