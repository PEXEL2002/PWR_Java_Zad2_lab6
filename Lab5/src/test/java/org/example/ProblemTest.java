package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProblemTest {

    @Test
    public void testAtLeastOneItemFits() {
        Problem problem = new Problem(123, 1, 10, 5);
        Result result = problem.Solve(5);
        // Test: czy zwrócono co najmniej jeden element
        assertTrue(result.getTotalWeight() > 0, "At least one item should be included");
    }

    @Test
    public void testNoItemFits() {
        // Wszystkie przedmioty mają wagę w przedziale [6, 10], więc żaden nie zmieści się w plecaku o pojemności 5
        Problem problem = new Problem(42, 6, 10, 5);
        Result result = problem.Solve(5);
        // Test: czy rozwiązanie jest puste
        assertEquals(0, result.getTotalWeight(), "No items should be included");
        assertEquals(0, result.getTotalValue(), "Total value should be 0");
    }

    @Test
    public void testItemRanges() {
        int lower = 1;
        int upper = 10;
        Problem problem = new Problem(55, lower, upper, 100);
        for (Item item : problem.getItems()) {
            assertTrue(item.getWeight() >= lower && item.getWeight() <= upper, "Item weight out of bounds");
            assertTrue(item.getPrice() >= lower && item.getPrice() <= upper, "Item price out of bounds");
        }
    }

    @Test
    public void testCorrectnessOfResult() {
        Problem problem = new Problem(1, 1, 10, 5);
        Result result = problem.Solve(50);
        int expectedWeight = result.getTotalWeight();
        int expectedValue = result.getTotalValue();
        int calculatedWeight = 0;
        int calculatedValue = 0;
        for (Item item : result.getItems()) {
            calculatedWeight += item.getWeight();
            calculatedValue += item.getPrice();
        }
        assertEquals(expectedWeight, calculatedWeight, "Total weight mismatch");
        assertEquals(expectedValue, calculatedValue, "Total value mismatch");
    }
}
