package DataStructure;

import java.util.HashMap;
import java.util.Map;

/*
https://leetcode.cn/problems/lru-cache/description/
https://www.lintcode.com/problem/134/
https://www.jiuzhang.com/problem/lru-cache/

思路:
1. 建立双向链表
2. 构造方程里面需要有:
    1. cache上限: capacity
    3. 一头一尾, 并且头尾互连, 作为dummy nodes
    4. 一个哈希字典, key是node的value, value是node本身, 方便查找
3. get方程:
    1. 在哈希字典里面找key, 如果没有返回默认值
    2. 如果有, 先用removeNode方程删除此node, 再用addNode方程插入此node, 以此表示这个node被调用过, 会放在最前端
4. put方程:
    在哈希字典里面找key:
        1. 如果没有: 先检查map.size(), removeNode(tail.prev)方程, 再创建新的node
        2. 如果有: 先将已有node的value更新, removeNode
    最后统一addNode方程
5. addNode方程:
    1. 主要负责将node插入head
    2. 加入哈希字典
6. removeNode:
    1. 主要负责将node移出链表
    2. 删去哈希字典

需要注意的点:
1. removeNode方程不是将node从尾端(tail)移走, 只是将node移出链表, 因为需要移出链表的node不一定都在tail前面
2. addNode方程一定是将node加入头端(head), 表示最近使用过
 */

class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> keyMap;
    private final Node head;
    private final Node tail;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyMap = new HashMap<>();
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.pre = head;
    }

    public void put(int key, int val) {
        // 检查是否需要先删除一个节点
        if (!keyMap.containsKey(key) && keyMap.size() == capacity) {
            // 删除一个节点
            removeNode(tail.pre);
        }
        // 通过key和value，获取或者创建节点
        Node node;
        if (keyMap.containsKey(key)) {
            node = keyMap.get(key);
            // 更新value
            node.val = val;
            removeNode(node);
        } else {
            node = new Node(key, val);
        }
        addNode(node);
    }

    public int get(int key) {
        if (!keyMap.containsKey(key)) return -1;
        // 获取节点
        Node node = keyMap.get(key);
        // 更新节点在链表中的位置
        removeNode(node);
        addNode(node);
        return node.val;
    }

    private void removeNode(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.next = null;
        node.pre = null;
        keyMap.remove(node.key);
    }

    private void addNode(Node node) {
        head.next.pre = node;
        node.next = head.next;
        head.next = node;
        node.pre = head;
        keyMap.put(node.key, node);
    }
    ;

    static class Node {
        int key;
        int val;
        Node pre;
        Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
            this.pre = null;
            this.next = null;
        }
    }
}
