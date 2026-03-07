package Interview.Booking;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static void main(String[] args) {
        FindMinPrice findMinPrice = new FindMinPrice();

        // Test Case 1: Basic case from the problem description
        List<FindMinPrice.Hotel> hotels1 = new ArrayList<>();
        hotels1.add(createHotel(0, 180, 1));
        hotels1.add(createHotel(1, 230, 2));
        hotels1.add(createHotel(2, 220, 2));
        hotels1.add(createHotel(3, 310, 3));
        int guests1 = 5;
        int expected1 = 530; // 220 (2 guests) + 310 (3 guests)
        runTest(findMinPrice, "Test Case 1: Basic Scenario", hotels1, guests1, expected1);

        // Test Case 2: A single hotel can accommodate the guests
        int guests2 = 3;
        int expected2 = 310; // Single hotel with capacity 3 is cheaper than 180+220 or 180+230
        runTest(findMinPrice, "Test Case 2: Single Hotel Is Cheapest", hotels1, guests2, expected2);

        // Test Case 3: Each hotel can be used only once.
        List<FindMinPrice.Hotel> hotels3 = new ArrayList<>();
        hotels3.add(createHotel(0, 100, 1));
        hotels3.add(createHotel(1, 400, 5));
        int guests3 = 3;
        // Must use hotel with capacity 5, as the capacity-1 hotel can only be used once.
        int expected3 = 400;
        runTest(
                findMinPrice,
                "Test Case 3: Use-Once Rule - Must take larger hotel",
                hotels3,
                guests3,
                expected3);

        // Test Case 3b: Cheaper to take the larger capacity hotel
        int guests3b = 4;
        // Must use hotel with capacity 5.
        int expected3b = 400;
        runTest(
                findMinPrice,
                "Test Case 3b: Larger capacity is the only option",
                hotels3,
                guests3b,
                expected3b);

        // Test Case 4: Empty hotel list
        List<FindMinPrice.Hotel> hotels4 = new ArrayList<>();
        int guests4 = 5;
        int expected4 = Integer.MAX_VALUE; // No solution
        runTest(findMinPrice, "Test Case 4: Empty Hotel List", hotels4, guests4, expected4);

        // Test Case 5: Zero guests
        int guests5 = 0;
        int expected5 = 0; // No cost for 0 guests
        runTest(findMinPrice, "Test Case 5: Zero Guests", hotels1, guests5, expected5);

        // Test Case 6: No solution as total capacity is not enough
        List<FindMinPrice.Hotel> hotels6 = new ArrayList<>();
        hotels6.add(createHotel(0, 100, 2));
        hotels6.add(createHotel(1, 120, 4));
        int guests6 = 7;
        // No solution, total capacity of all hotels is 6, which is less than 7.
        int expected6 = Integer.MAX_VALUE;
        runTest(
                findMinPrice,
                "Test Case 6: No Solution - Insufficient Total Capacity",
                hotels6,
                guests6,
                expected6);

        // Test Case 7: Explicitly test the "use-once" rule
        List<FindMinPrice.Hotel> hotels7 = new ArrayList<>();
        hotels7.add(createHotel(0, 100, 2));
        int guests7 = 3;
        // No solution, because the single hotel cannot be used twice.
        int expected7 = Integer.MAX_VALUE;
        runTest(
                findMinPrice,
                "Test Case 7: Explicit Use-Once-Rule Check",
                hotels7,
                guests7,
                expected7);
    }

    private static FindMinPrice.Hotel createHotel(int id, int price, int capacity) {
        FindMinPrice.Hotel hotel = new FindMinPrice.Hotel();
        hotel.id = id;
        hotel.price = price;
        hotel.capacity = capacity;
        return hotel;
    }

    private static void runTest(
            FindMinPrice finder,
            String testName,
            List<FindMinPrice.Hotel> hotels,
            int guests,
            int expected) {
        System.out.println("--- " + testName + " ---");
        System.out.println("Hotels: " + hotels.size() + " | Guests: " + guests);
        int result = finder.minPrice(hotels, guests);
        System.out.println(
                "Expected: " + (expected == Integer.MAX_VALUE ? "No solution" : expected));
        System.out.println("Actual:   " + (result == Integer.MAX_VALUE ? "No solution" : result));
        System.out.println("Result: " + (result == expected ? "PASSED" : "FAILED"));
        System.out.println();
    }
}
