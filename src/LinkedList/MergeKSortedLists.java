package LinkedList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/*
https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked

You are given the heads of two sorted linked lists list1 and list2.
Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
Return the head of the merged linked list.

Example 1:
Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]

Example 2:
Input: list1 = [], list2 = []
Output: []

Example 3:
Input: list1 = [], list2 = [0]
Output: [0]

思路1: 堆
1. 创建一个堆，因为ListNode是一个自定义的类，所以需要复写一个compare的方法
2. 将lists中的所有链表头放入堆，排除null的链表头或者链表头的值为null的
3. 遍历堆，直接推出堆头，用两个指针，将新的链表和堆头连起来，堆头判断一下下一个是否为null，不为null就加入堆里

思路2：分治
1. 将问题转化为merge 2 sorted lists

follow up：
如果某些链表太大以至于本地内存无法保存，该如何处理？
如果是文件，给每一个链表开启一个类似于Buffreader之类的I/O类，然后每次操作的时候只读取一个数

 */
public class MergeKSortedLists {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
     * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
     * this.next = next; } }
     */
    public ListNode mergeKLists(ListNode[] lists) {
        int n = lists.length;
        // 创建一个堆
        PriorityQueue<ListNode> heap =
                new PriorityQueue<>(
                        new Comparator<ListNode>() {
                            @Override
                            public int compare(ListNode o1, ListNode o2) {
                                return o1.val - o2.val;
                            }
                        });
        // 将每个链表的头都放进堆里
        for (ListNode node : lists) {
            // 跳过null
            if (node != null) {
                heap.offer(node);
            }
        }
        // 创建新的链表
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (!heap.isEmpty()) {
            // 将堆头推出
            ListNode curNode = heap.poll();
            // head指向这个node
            last.next = curNode;
            // last指向head
            last = curNode;
            // 如果curNode还有下一个
            if (curNode.next != null) {
                heap.offer(curNode.next);
            }
        }
        return dummy.next;
    }

    public ListNode mergeKLists2(ListNode[] lists) {
        return mergeKLists(lists, 0, lists.length);
    }

    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        int listsLength = end - start;
        // lists为空
        if (listsLength == 0) return null;
        // 只有一个链表
        if (listsLength == 1) return lists[start];
        ListNode left = mergeKLists(lists, start, start + listsLength / 2);
        ListNode right = mergeKLists(lists, start + listsLength / 2, end);
        return mergeTwoLists(left, right);
    }

    private ListNode mergeTwoLists(ListNode left, ListNode right) {
        // 创建一个新链表
        ListNode dummy = new ListNode();
        ListNode cur = dummy;
        while (left != null && right != null) {
            // 比大小
            if (left.val < right.val) {
                cur.next = left;
                left = left.next;
            } else {
                cur.next = right;
                right = right.next;
            }
            // 移动cur
            cur = cur.next;
        }
        // 拼接剩余的链表
        cur.next = left != null ? left : right;
        return dummy.next;
    }

    // Follow-up question
    // 辅助类：表示一个文件流中的当前元素
    static class Element implements Comparable<Element> {
        public int value; // 当前元素的值
        public BufferedReader reader; // 对应的文件读取器

        public Element(int value, BufferedReader reader) {
            this.value = value;
            this.reader = reader;
        }

        // 实现 Comparable 接口，用于优先队列排序（小顶堆）
        @Override
        public int compareTo(Element other) {
            return Integer.compare(this.value, other.value);
        }
    }

    public class ExternalMerger {

        // 核心合并方法
        public static void mergeKSortedFiles(List<String> inputFiles, String outputFile)
                throws IOException {
            // 1. 初始化优先队列（小顶堆）
            PriorityQueue<Element> minHeap = new PriorityQueue<>();
            List<BufferedReader> readers = new ArrayList<>();

            try {
                // 2. 打开所有输入文件，并读取第一个元素放入堆中
                for (String file : inputFiles) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    readers.add(reader);
                    String line = reader.readLine();
                    if (line != null) {
                        // 将第一个元素及对应的读取器封装成 Element 对象，加入堆
                        minHeap.add(new Element(Integer.parseInt(line), reader));
                    }
                }

                // 3. 打开输出文件写入结果
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

                // 4. 迭代合并过程
                while (!minHeap.isEmpty()) {
                    // 取出堆顶（当前所有文件中最小的元素）
                    Element currentMin = minHeap.poll();

                    // 将最小元素写入输出文件
                    writer.write(String.valueOf(currentMin.value));
                    writer.newLine();

                    // 5. 从取出元素对应的文件中读取下一个元素
                    String nextLine = currentMin.reader.readLine();
                    if (nextLine != null) {
                        // 如果文件还有数据，更新 Element 的值，并重新加入堆
                        currentMin.value = Integer.parseInt(nextLine);
                        minHeap.add(currentMin);
                    }
                    // 如果文件读取完毕，则不再将该 reader 对应的 Element 加入堆中
                }

                writer.close(); // 关闭输出流

            } finally {
                // 确保所有输入流都被关闭，防止资源泄露
                for (BufferedReader reader : readers) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 示例运行的主方法
        public static void main(String[] args) {
            // 1. 模拟生成三个有序的巨大输入文件
            try {
                generateSortedFile("file1.txt", 1, 100000, 3); // 1, 4, 7, ...
                generateSortedFile("file2.txt", 2, 100000, 4); // 2, 6, 10, ...
                generateSortedFile("file3.txt", 3, 100000, 5); // 3, 8, 13, ...
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            List<String> inputFiles = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
            String outputFile = "output.txt";

            System.out.println("开始合并文件...");
            try {
                mergeKSortedFiles(inputFiles, outputFile);
                System.out.println("合并完成！结果保存在 " + outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 辅助方法：生成一个包含有序整数的文件
        private static void generateSortedFile(String filename, int start, int count, int step)
                throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < count; i++) {
                writer.write(String.valueOf(start + i * step));
                writer.newLine();
            }
            writer.close();
        }
    }
}
