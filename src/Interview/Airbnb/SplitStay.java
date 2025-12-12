package Interview.Airbnb;

/*
When booking a trip on Airbnb, if we do not have a place available for all the requested dates,
we will suggest a Split Stay where you stay at two different Airbnbs, one for the first part of your trip and the other for the remainder.
Given a list of Airbnbs with availability, make an api endpoint that will return all possible combinations of Split Stays across two listings for a given date range
For this interview question, we will give each listing a name ("A", "B", etc) and our availability will just be a list of day numbers.

For example:
Airbnb A - [1,2,3,6,7,10,11]
Airbnb B - [3,4,5,6,8,9,10,13]
Airbnb C - [7,8,9,10,11]
Given the date range [3-11], the expected result would be all sets of two Airbnbs that could form a split stay:
Results：[ [B, C] ]
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SplitStay {

    public static class Airbnb {
        String id;
        int moveInDate;
        int moveOutDate;
        Set<Integer> availableDates;

        public Airbnb(String id, List<Integer> availableDates) {
            this.id = id;
            this.availableDates = new HashSet<>();
            this.availableDates.addAll(availableDates);
            this.moveInDate = -1;
            this.moveOutDate = -1;
        }
    }

    public List<List<String>> findSplitStay(Map<String, List<Integer>> airbnbMap, int start, int end) {
        // Use 2 maps to store moveInDate/moveOutDate -> List of airbnb
        TreeMap<Integer, List<String>> moveInMap = new TreeMap<>();
        TreeMap<Integer, List<String>> moveOutMap = new TreeMap<>();
        // Create Airbnb for each and calculate their earliest move-in and last move-out date
        for (Map.Entry<String, List<Integer>> entry: airbnbMap.entrySet()) {
            Airbnb airbnb = new Airbnb(entry.getKey(), entry.getValue());
            getMoveInDate(airbnb, start, end);
            getMoveOutDate(airbnb, start, end);
            if (airbnb.moveOutDate != -1) moveOutMap.computeIfAbsent(airbnb.moveOutDate, k -> new ArrayList<>()).add(airbnb.id);
            if (airbnb.moveInDate != -1) moveInMap.computeIfAbsent(airbnb.moveInDate, k -> new ArrayList<>()).add(airbnb.id);
        }
        /*
        Traverse all elements in moveOutMap, and get corresponding moveInDate
        moveOutMap: this map is a sorted map (sort by key, which is from the earliest date to latest date), and
        it represents that, if our first stay ends on key, what airbnbs we can choose
        moveInMap: this map is a sorted map (sort by key, which is from the earliest date to latest date), and
        it represents that, if our second stay starts on key, what airbnbs we can choose

        So, if moveOutMap = moveInMap + 1, this 2 stays can cover all dates
         */
        Set<String> unique = new HashSet<>();
        for (Map.Entry<Integer, List<String>> entry: moveOutMap.entrySet()) {
            int moveOutDate = entry.getKey();
            StringBuilder sb = new StringBuilder();
            for (String firstStay: entry.getValue()) {
                // Reset sb
                sb.setLength(0);
                // Add firstStay
                sb.append(firstStay).append(",");
                // Traverse moveInMap on the same date and get the list of airbnbs
                if (moveInMap.get(moveOutDate + 1) == null) continue;
                for (String secondStay: moveInMap.get(moveOutDate + 1)) {
                    if (!firstStay.equals(secondStay)) {
                        // Add secondStay
                        sb.append(secondStay);
                        // Add to dedup
                        unique.add(sb.toString());
                        // Remove secondStay
                        sb.deleteCharAt(sb.length() - 1);
                    }
                }
            }
        }
        List<List<String>> result = new ArrayList<>();
        for (String solution: unique) {
            result.add(Arrays.asList(solution.split(",")));
        }
        return result;
    }

    /*
    Move-out date means if we want to use this airbnb as the first stay, what's the latest date to move out.
    In other words, we need to:
    1. Search from start date and from earliest to latest
    2. Find the first non-consecutive number
     */
    private void getMoveOutDate(Airbnb airbnb, int start, int end) {
        // This airbnb has to be available on start date, otherwise we cannot treat it as the first stay
        if (!airbnb.availableDates.contains(start++)) return;
        // Calculate and find the first non-consecutive number
        while (start <= end && airbnb.availableDates.contains(start)) start++;
        airbnb.moveOutDate = start - 1;
    }

    /*
    Move-in date means if we want to use this airbnb as the second stay, what's the earliest date to move in.
    In other words, we need to:
    1. Search from end date and from latest to earliest
    2. Find the first non-consecutive number
    */
    private void getMoveInDate(Airbnb airbnb, int start, int end) {
        // This airbnb has to be available on start date, otherwise we cannot treat it as the first stay
        if (!airbnb.availableDates.contains(end--)) return;
        // Calculate and find the first non-consecutive number
        while (start <= end && airbnb.availableDates.contains(end)) end--;
        airbnb.moveInDate = end + 1;
    }
}