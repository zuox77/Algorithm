package Interview.Airbnb;

/*
Build a function that lets the user determine the most cost-effective order.
Input includes the menu of the restaurant that contains the item name and its price.
And the item the user wants to order.
Except for single items, the menu also offers combo items, which are groups of several items at a discounted price.
Return the lowest price at which they can get all of their desired items and the item the user needs to order on the menu.
Constraint: The user can want a maximum of 3 unique items.
e.g.
Menu:
[
    [5.00, "pizza"],
    [8.00, "sandwich,coke"],
    [4.00, "pasta"],
    [2.00, "coke"],
    [6.00, "pasta,coke,pizza"],
    [8.00, "burger,coke,pizza"],
    [5.00, "sandwich"]
]
user_wants: ["sandwich", "pasta", "coke"]
Expected answer: lowest cost = 11, order path = [ [ 2, 3, 6], [4,6] ]
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Menu {
    public static class MenuItem {
        int bitMask;
        double price;
        int menuIndex;

        public MenuItem (int bitMask, double price) {
            this.bitMask = bitMask;
            this.price = price;
        }

        public MenuItem (int bitMask, double price, int menuIndex) {
            this.bitMask = bitMask;
            this.price = price;
            this.menuIndex = menuIndex;
        }
    }

    public double findMinPrice (Object[][] menu, String[] userWants) {
        int n = userWants.length;
        // Assign all wanted items with an index and store in a map
        Map<String, Integer> wantedMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            // All in lowercase
            wantedMap.put(userWants[i].toLowerCase(), i); // Assume no duplicate
        }
        // Create items and calculate their bitMask
        List<MenuItem> menuItems = new ArrayList<>();
        for (Object[] menuItem: menu) {
            double price = (Double) menuItem[0];
            String items = (String) menuItem[1];
            // Create menu item
            int menuBitMask = 0;
            for (String item: items.split(",")) {
                item = item.trim().toLowerCase();
                // Skip if user don't want this item
                if (!wantedMap.containsKey(item)) continue;
                menuBitMask |= 1 << wantedMap.get(item);
            }
            if (menuBitMask != 0) {
                menuItems.add(new MenuItem(menuBitMask, price));
            }
        }
        // Create an array to calculate the lowest price
        double[] minPrice = new double[1 << n];
        int targetState = (1 << n) - 1;
        Arrays.fill(minPrice, Double.POSITIVE_INFINITY);
        minPrice[0] = 0.0;
        // Iterate the menuItems array to update the lowest price
        for (MenuItem menuItem: menuItems) {
            // Traverse reversely to update
            for (int i = targetState; i >= 0; i--) {
                // If it's Double.POSITIVE_INFINITY, meaning we cannot reach position i yet, so skip
                if (minPrice[i] == Double.POSITIVE_INFINITY) continue;
                // Use bitMask to update
                int newBitMask = i | menuItem.bitMask;
                double newPrice = minPrice[i] + menuItem.price;
                minPrice[newBitMask] = Math.min(minPrice[newBitMask], newPrice);
            }
        }
        return minPrice[targetState];
    }

    // If unique path is also required
    public static class Answer {
        double price;
        List<String> solutionList;

        public Answer(double price, List<String> solutionList) {
            this.price = price;
            this.solutionList = solutionList;
        }
    }

    public static class StateInfo {
        double price;
        Set<String> uniquePaths;

        public StateInfo (double price) {
            this.price = price;
            uniquePaths = new HashSet<>();
        }
    }

    public Answer findMinPriceAndPath (Object[][] menu, String[] userWants) {
        int n = userWants.length;
        // Assign all wanted items with an index and store in a map
        Map<String, Integer> wantedMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            // All in lowercase
            wantedMap.put(userWants[i].toLowerCase(), i); // Assume no duplicate
        }
        // Create items and calculate their bitMask
        List<MenuItem> menuItems = new ArrayList<>();
        for (int i = 0; i < menu.length; i++) {
            Object[] menuItem = menu[i];
            double price = (Double) menuItem[0];
            String items = (String) menuItem[1];
            // Create menu item
            int menuBitMask = 0;
            for (String item: items.split(",")) {
                item = item.trim().toLowerCase();
                // Skip if user don't want this item
                if (!wantedMap.containsKey(item)) continue;
                menuBitMask |= 1 << wantedMap.get(item);
            }
            if (menuBitMask != 0) {
                menuItems.add(new MenuItem(menuBitMask, price, i));
            }
        }
        // Create an array to calculate the lowest price
        int totalState = (1 << n);
        StateInfo[] stateInfos = new StateInfo[totalState];
        for (int i = 0; i < totalState; i++) {
            stateInfos[i] = new StateInfo(Double.POSITIVE_INFINITY);
            stateInfos[i].uniquePaths.add("");
            if (i == 0) stateInfos[i].price = 0.0;
        }
        // Iterate the menuItems array to update the lowest price
        for (MenuItem menuItem: menuItems) {
            // Traverse reversely to update
            for (int currentState = totalState - 1; currentState >= 0; currentState--) {
                // If it's Double.POSITIVE_INFINITY, meaning we cannot reach position i yet, so skip
                if (stateInfos[currentState].price == Double.POSITIVE_INFINITY) continue;
                // Use this menuItem's bitMask along with currentState to find the new state
                int newState = currentState | menuItem.bitMask;
                double newPrice = stateInfos[currentState].price + menuItem.price;
                // If found better solution
                if (newPrice < stateInfos[newState].price) {
                    // Clear old path
                    stateInfos[newState].uniquePaths.clear();
                    // For each current state path, create new path, new path = current state path + current menu index
                    for (String path: stateInfos[currentState].uniquePaths) {
                        String newSolution = addItemToPath(path, menuItem.menuIndex); // index is 0-based, so add 1
                        stateInfos[newState].uniquePaths.add(newSolution);
                    }
                    // Update price
                    stateInfos[newState].price = newPrice;
                /*
                If same price but different paths
                Note: float(double) cannot use == because it's not precise
                 */
                } else if (Math.abs(newPrice - stateInfos[newState].price) < 1e-4) {
                    // For each current state path, create new path, new path = current state path + current menu index
                    for (String path: stateInfos[currentState].uniquePaths) {
                        String newSolution = addItemToPath(path, menuItem.menuIndex);
                        stateInfos[newState].uniquePaths.add(newSolution);
                    }
                }
            }
        }
        StateInfo finalState = stateInfos[totalState - 1];
        List<String> solutionList = new ArrayList<>();
        // No solution
        if (finalState.price == Double.POSITIVE_INFINITY) return new Answer(Double.POSITIVE_INFINITY, solutionList);
        solutionList.addAll(finalState.uniquePaths);
        return new Answer(finalState.price, solutionList);
    }

    private String addItemToPath(String path, int itemNum) {
        String itemStr = String.valueOf(itemNum);
        if (path.isEmpty()) return itemStr;
        String newPath = path + "," + itemStr;
        String[] strList = newPath.split(",");
        Arrays.sort(strList);
        return String.join(",", strList);
    }
}
