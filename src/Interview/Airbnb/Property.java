package Interview.Airbnb;

/*
假设我们有一个Property列表，每个 Property(id, neighborhood, capacity)，我们有一个目标Neighborhood和GroupSize
请在neighborhood范围内找出推荐的property组合的id列表
按照先后顺序满足
1. capacity大于等于groupSize
2. property数量更少
3. capacity尽可能接近groupSize
请输出一个这样的组合，如果没有这样的组合存在，输出空数组。

输入格式: List<Property> properties, String neighbourhood, int groupSize
输出格式：List<Integer>（或者int[]也可以）
示例1：
输入：
properties = [Property(1, "downtown", 5), Property(2, "downtown", 3), Property(3, "downtown", 1), Property(4, "uptown", 4), Property(5, "uptown", 2)]
neighbourhood = "downtown"
groupSize = 4
输出：
[1]
示例2：
输入：
properties = [Property(1, "downtown", 5), Property(2, "downtown", 3), Property(3, "downtown", 1), Property(4, "uptown", 4), Property(5, "uptown", 2)]
neighbourhood = "downtown"
groupSize = 10
输出：
[]
示例3：
输入：
properties = [Property(1, "downtown", 5), Property(2, "downtown", 3), Property(3, "downtown", 1), Property(4, "uptown", 4), Property(5, "uptown", 2)]
neighbourhood = "downtown"
groupSize = 6
输出：
[1, 3]
示例4：
输入：
properties = [Property(1, "downtown", 5), Property(2, "downtown", 3), Property(3, "downtown", 1), Property(4, "uptown", 4), Property(5, "uptown", 2)]
neighbourhood = "downtown"
groupSize = 3
输出：
[2]
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Property {
    public static class PropertyInfo {
        int id;
        String neighborhood;
        int capacity;

        public PropertyInfo(int id, String neighborhood, int capacity) {
            this.id = id;
            this.neighborhood = neighborhood;
            this.capacity = capacity;
        }
    }

    public List<Integer> recommendProp(List<PropertyInfo> properties, int groupSize, String neighborhood) {
        List<Integer> ans = new ArrayList<>();
        // Find all target neighborhood properties and largest capacity among all properties
        List<PropertyInfo> filtered = new ArrayList<>();
        int maxPropCap = 0;
        for (PropertyInfo prop: properties) {
            if (neighborhood.equalsIgnoreCase(prop.neighborhood)) {
                filtered.add(prop);
                maxPropCap = Math.max(maxPropCap, prop.capacity);
            }
        }
        if (maxPropCap == 0 || filtered.isEmpty()) return ans;

        int upperBound = groupSize + maxPropCap;
        int[] minPropNum = new int[upperBound + 1];
        minPropNum[0] = 0;
        Arrays.fill(minPropNum, Integer.MAX_VALUE);
        int[] prevCap = new int[upperBound + 1];
        Arrays.fill(prevCap, -1);
        int[] prevIdx = new int[upperBound + 1];
        Arrays.fill(prevIdx, -1);

        for (int i = 0; i < filtered.size(); i++) {
            PropertyInfo prop = filtered.get(i);
            int cap = prop.capacity;
            for (int j = upperBound; j >= cap; j--) {
                if (minPropNum[j - cap] != Integer.MAX_VALUE && minPropNum[j - cap] + 1 < minPropNum[j]) {
                    minPropNum[j] = minPropNum[j - cap] + 1;
                    prevCap[j] = j - cap;
                    prevIdx[j] = i;
                }
            }
        }

        int targetCap = -1;
        for (int i = groupSize; i <= upperBound; i++) {
            if (minPropNum[i] != Integer.MAX_VALUE) {
                targetCap = i;
                break;
            }
        }
        if (targetCap == -1) return ans;

        while (targetCap > 0) {
            int propIdx = prevIdx[targetCap];
            if (propIdx == -1) break;
            ans.add(filtered.get(propIdx).id);
            targetCap = prevCap[targetCap];
        }

        return ans;
    }
}