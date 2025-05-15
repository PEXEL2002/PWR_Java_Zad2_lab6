package org.example;

import java.util.Random;

public class Problem {
    Random rand;
    int lowerBound;
    int upperBound;
    Item [] items;

    Problem(int seed, int lowerBound, int upperBound, int size){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.rand = new Random(seed);
        this.items = new Item[size];
        int weight, price;
        for (int i = 0; i < size; i++) {
            weight = rand.nextInt(upperBound - lowerBound + 1) + lowerBound;
            price = rand.nextInt(upperBound - lowerBound + 1) + lowerBound;
            items[i] = new Item(i, weight, price);
        }
    }
    public Result Solve(int capacity) {
        Item[] sortedItems = items.clone();
        java.util.Arrays.sort(sortedItems, (a, b) -> {
            return Float.compare(b.getPriceToWeightRatio(), a.getPriceToWeightRatio());
        });
        Result result = new Result();
        int remainingCapacity = capacity;
        for (Item item : sortedItems) {
            while (item.getWeight() <= remainingCapacity) {
                result.addToResult(item.getId(), item);
                remainingCapacity -= item.getWeight();
            }
        }
        return result;
    }
    public Item[] getItems() {
        return items;
    }
    @Override
    public String toString() {
        String result = "Problem - " + items.length + " available items:\n";
        for (int i = 0; i < items.length; i++) {
            result += items[i] + "\n";
        }
        return result;
    }
}
