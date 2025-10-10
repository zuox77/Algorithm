package DataStructure;

import java.util.HashMap;

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
    2. 如果有, 先用removeFromList方程删除此node, 再用addtoHead方程插入此node, 以此表示这个node被调用过, 会放在最前端
4. put方程: 
    在哈希字典里面找key: 
        1. 如果没有: 先检查map.size(), removeFromList(tail.prev)方程, 再创建新的node
        2. 如果有: 先将已有node的value更新, removeFromList
    最后统一addtoHead方程
5. addtoHead方程: 
    1. 主要负责将node插入head
    2. 加入哈希字典
6. removeFromList: 
    1. 主要负责将node移出链表
    2. 删去哈希字典

需要注意的点: 
1. removeFromList方程不是将node从尾端(tail)移走, 只是将node移出链表, 因为需要移出链表的node不一定都在tail前面
2. addtoHead方程一定是将node加入头端(head), 表示最近使用过
 */

class Node {
    int val;
    int key;
    Node next;
    Node prev;

    public Node() {
    }

    public Node(int val, int key) {
        this.val = val;
        this.key = key;
        this.next = null;
        this.prev = null;
    }
}

class LRUCache {
    private int capcacity;
    private Node head = new Node();
    private Node tail = new Node();
    private HashMap<Integer, Node> map = new HashMap<>();

    public LRUCache(int capacity) {
        this.capcacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            removeFromList(node);
            addToHead(node);
            return node.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.val = value;
            removeFromList(node);
            addToHead(node);
        } else {
            Node node = new Node(value, key);
            if (map.size() == capcacity) {
                removeFromList(tail.prev);
            }
            addToHead(node);
        }
    }

    public void addToHead(Node node) {
        Node tmp = head.next;
        head.next = node;
        node.prev = head;
        node.next = tmp;
        tmp.prev = node;

        int key = node.key;
        map.put(key, node);
    }

    public void removeFromList(Node node) {
        /*
        这里不能写成: 
        tail.prev = node.prev;
        node.prev.next = tail;
        因为不是从tail移除！是从链表任意位置移除！
         */
        node.prev.next = node.next;
        node.next.prev = node.prev;
        int key = node.key;
        map.remove(key);
    }
}

