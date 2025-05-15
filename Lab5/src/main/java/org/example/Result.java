package org.example;

import java.util.ArrayList;

public class Result {
    private ArrayList<Item> items;

    public Result() {
        items = new ArrayList<>();
    }

    public void addToResult(int itemId, Item item) {
        items.add(item);
    }

    public int getTotalWeight() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getWeight();
        }
        return sum;
    }

    public int getTotalValue() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getPrice();
        }
        return sum;
    }
    public ArrayList<Item> getItems() {
        return items;
    }
    @Override
    public String toString() {
        String result = "Contents of the backpack:\n";
        for (int i = 0; i < items.size(); i++) {
            result +=  items.get(i).toString() + "\n";
        }
        result += "Total weight: " + getTotalWeight() + "\n";
        result += "Total value: " + getTotalValue() + "\n";
        return result;
    }
}
