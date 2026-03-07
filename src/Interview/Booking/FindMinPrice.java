package Interview.Booking;

import java.util.Arrays;
import java.util.List;

/*
陈述一堆背景后，给出一个数组，里面是map, hash, dict, you name it,
然后，给一个需要入住的客人总数，比如5，
[
    {id: 0, price: 180, guests: 1},
    {id: 1, price: 230, guests: 2},
    {id: 2, price: 220, guests: 2},
    {id: 3, price: 310, guests: 3},
]

求能满足客人入住所需要的最低价格，房间容量，是可能会超过客人总数的，比如有一个房间 {price: 1, guests:1000} （真是尼玛睡大街啊哈哈哈。）

 */
public class FindMinPrice {

    public int minPrice(List<Hotel> hotels, int guest) {
        // 遍历所有酒店找到某个酒店的最大容纳人数
        int hotelCount = hotels.size();
        int maxCap = 0;
        for (Hotel hotel : hotels) {
            maxCap = Math.max(maxCap, hotel.capacity);
        }
        // 用DP数组来记录不同容纳人数时的最少价格
        int upperBound = hotelCount + maxCap;
        int[] minPrice = new int[upperBound + 1];
        // 初始化0,因为容纳0个人的时候不需要任何酒店,其余的都为最大值
        Arrays.fill(minPrice, Integer.MAX_VALUE);
        minPrice[0] = 0;
        // 外层遍历所有的酒店
        for (int i = 0; i < hotelCount; i++) {
            Hotel hotel = hotels.get(i);
            int cap = hotel.capacity;
            // 内层从大到小遍历DP数组,通过本次循环得到的hotel的cap去计算
            for (int j = upperBound; j >= cap; j--) {
                // 如果DP[j - cap]不是Integer.MAX_VALUE或者价格更低的话,更新DP[j]
                if (minPrice[j - cap] != Integer.MAX_VALUE
                        && minPrice[j - cap] + hotel.price < minPrice[j]) {
                    minPrice[j] = minPrice[j - cap] + hotel.price;
                }
            }
        }
        // 找到最小值
        int ans = Integer.MAX_VALUE;
        for (int i = guest; i < minPrice.length; i++) {
            ans = Math.min(ans, minPrice[i]);
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static class Hotel {
        int id;
        int price;
        int capacity;
    }
}
