package SortAlgorithm;
/*
https://www.lintcode.com/problem/49/
https://www.jiuzhang.com/problem/sort-letters-by-case/

思路1: 
跟快排一模一样
时间复杂度: O(N)
空间复杂度: O(1)

思路2: 
跟SortColors一模一样, 双指针
时间复杂度: O(N)
空间复杂度: O(1)
 */

public class QuickSort_SortLettersByCase {
    public void solution1(char[] chars) {
        int left = 0, right = chars.length - 1;

        while (left <= right) {
            while (left <= right && Character.isLowerCase(chars[left])) {
                left++;
            }
            while (left <= right && Character.isUpperCase(chars[right])) {
                right--;
            }
            if (left <= right) {
                char tmp = chars[left];
                chars[left] = chars[right];
                chars[right] = tmp;
                left++;
                right--;
            }
        }
    }

    // 双指针
    public void solution2(char[] chars) {
        int n = chars.length;
        int left = 0, right = n - 1;
        while (left <= right) {
            if (Character.isLowerCase(chars[left])) {
                left++;
            } else {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                right--;
            }
        }
    }
}
