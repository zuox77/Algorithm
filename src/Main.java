import Interview.Airbnb.Property;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws ParseException {
        Map<Property, Integer> map = Map.of(
                Property.weight, 10,
                Property.volume, 20,
                Property.crewSize, 30
        );
        Shipment shipment = new Shipment(map);
        Limit limit1 = new Limit(Property.weight, 0, 0, 10);
        Limit limit1_1 = new Limit(Property.crewSize, 5, 0, 10);
        Limit limit1_2 = new Limit(Property.crewSize, 10, 10, 15);
        Limit limit1_3 = new Limit(Property.crewSize, 20, 15, -1);
        Limit limit2 = new Limit(Property.weight, 0, 10, -1);
        Limit limit2_1 = new Limit(Property.crewSize, 15, 0, -1);
        limit1.innerLimits.add(limit1_1);
        limit1.innerLimits.add(limit1_2);
        limit1.innerLimits.add(limit1_3);
        limit2.innerLimits.add(limit2_1);
        List<Limit> input = new ArrayList<>();
        input.add(limit1);
        input.add(limit2);
        System.out.println(calculateCost(input, shipment));
    }

    public static int calculateCost(List<Limit> limitList, Shipment shipment) {
        Limit limit = null;
        for (Limit element : limitList) {
            if (shipment.map.get(element.type) <= element.end) {
                limit = element;
                break;
            }
        }
        if (limit == null) return -1;
        int cost = 0;
        int amount = shipment.map.get(limit.innerLimits.getFirst().type);
        for (Limit childLimit: limit.innerLimits) {
            if (childLimit.end == -1 || childLimit.end >= amount) {
                cost += amount * childLimit.unitCost;
                return cost;
            } else if (childLimit.end < amount) {
                cost += childLimit.unitCost * childLimit.end;
                amount -= childLimit.end;
            }
        }
        return cost;
    }

    public static class Limit {
        Property type;
        int unitCost;
        int start;
        int end;
        List<Limit> innerLimits;

        public Limit(Property type, int unitCost, int start, int end) {
            this.type = type;
            this.unitCost = unitCost;
            this.start = start;
            this.end = end;
            this.innerLimits = new ArrayList<>();
        }
    }

    public static class Shipment {
        Map<Property, Integer> map;

        public Shipment(Map<Property, Integer> map) {
            this.map = map;
        }
    }

    public enum Property {
        weight("1"), volume("2"), crewSize("3");

        Property(String s) {}
    }

    public int calculateCost(Limit limit, Shipment shipment) {

    }

    public static String randomWriter(String s, int n) {
        Map<String, List<String>> map = new HashMap<>();
        String[] wordList = s.split(" ");
        int len = wordList.length;
        for (int i = 0; i < len; i++) {
            String word = wordList[i];
            String nextWord;
            if (i == len - 1) {
                nextWord = wordList[0];
            } else {
                nextWord = wordList[i + 1];
            }
            map.computeIfAbsent(word, k -> new ArrayList<>()).add(nextWord);
        }
        String[] ans = new String[n];
        ans[0] = wordList[(int) (Math.random() * (len - 1))];
        for (int i = 1; i < n; i++) {
            List<String> nextWords = map.get(ans[i - 1]);
            String nextWord = nextWords.get((int) (Math.random() * (nextWords.size() - 1)));
            ans[i] = nextWord;
        }
        return String.join(" ", ans);
    }

    public static String guess(String target, String guess) {
        int n = target.length();
        Map<Character, Integer> targetFreqMap = new HashMap<>();

        char[] ans = new char[n];
        for (int i = 0; i < n; i++) {
            char ch = target.charAt(i);
            targetFreqMap.merge(ch, 1, Integer::sum);
        }
        for (int i = 0; i < n; i++) {
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);
            if (!targetFreqMap.containsKey(guessChar)) {
                ans[i] = 'B';
                continue;
            }
            if (guessChar == targetChar) {
                ans[i] = 'G';
                if (targetFreqMap.get(targetChar) > 0) {
                    targetFreqMap.put(targetChar, targetFreqMap.get(targetChar) - 1);
                } else {
                    targetFreqMap.remove(targetChar);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (ans[i] == 'G') continue;
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);
            if (!targetFreqMap.containsKey(guessChar)) {
                ans[i] = 'B';
            } else if (guessChar != targetChar){
                ans[i] = 'Y';
            }
            if (targetFreqMap.get(targetChar) > 1) {
                targetFreqMap.put(targetChar, targetFreqMap.get(targetChar) - 1);
            } else {
                targetFreqMap.remove(targetChar);
            }
        }
        return new String(ans);
    }
}

/*
set_value("A", 5): Sets A=5
set_value("B", 10): Sets B=10
set_sum("C", ["A", "B"]): C depends on A and B, so C=15
set_sum("D", ["C", "C", "A"]): D depends on C and A, so D=15+15+5=35
get_value("A"): Returns 5
set_value("B", 10): B already 10, no change needed
get_value("C"): Returns 15
get_value("D"): Returns 35
set_value("A", 100): A changes to 100, triggers updates:
C updates to 100+10=110
D updates to 110+110+100=320
get_value("D"): Returns 320
 */