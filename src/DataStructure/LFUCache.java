package DataStructure;

import java.util.HashMap;
import java.util.Map;

/*
https://leetcode.cn/problems/lfu-cache/

思路：
1. 缕清题意背后隐藏的需求：
    1. LFU = Least Frequent Use，那么一定需要一个数据结构记录频率
    2. 当容量满了以后，需要从最小的frequency中，根据LRU剔除一个节点，LRU = Least Recent Use，那么：
        1. 需要一个final变量记录capacity（判断容量满了这个条件）
        2. 需要一个变量记录当前最小的frequency
        3. 需要一个数据结构，根据不同的频率，对该频率上的节点，记录其LRU，即通过最近使用排序，这样就可以通过最小的frequency找到需要剔除的节点
        4. 需要一个逻辑，当从最小的frequency中根据LRU剔除一个节点以后，如果该频率没有节点了，需要找到新的最小frequency
    3. 需要通过key，直接找到value，那么一定需要一个数据结构记录key
    4. 所有的操作都要O(1)时间完成
2. 能做到O(1)时间的数据结构和其对应的操作为：
    1. 哈希表：get()，put()，remove()
    2. 栈：offer()，poll()
    3. 数组：通过下标查找值，通过下标修改值
    4. 队列：offerFirst()，pollFirst()，offerLast()，pollLast()，peekFirst()，peekLast()
    5. 链表：通过pre和next，能实现O(1)的删除和插入，通过记录某些节点，可以实现O(1)的查找
3. 结合需求和拥有的工具：
    1. 对于1.2.3，记录LRU最好的方式为双端链表，所以肯定需要链表和节点。而对于1.1，肯定需要一个哈希表
        所以结合二者：用一个哈希表保存频率和双端链表。可双端链表该如何保存到哈希表中呢？
        双端链表是一群对象通过某种方式连接在一起的一个多对象数据结构，可哈希表只能保存单对单的映射关系
        深入思考：我们用双端链表主要是为了保存LRU的信息，而我们只会在两个场景使用LRU：
            1. 当有节点被使用或者新的节点被插入的时候，将其插入双端链表的头，表示最近使用过
            2. 当需要将最近没使用过的节点移除时，从双端链表的尾端删除
            总结：所以其实我们需要的是双端链表的一头一尾（中间的节点的插入和删除可以通过该节点自己的pre和next而完成）
            如果我们将一头一尾通过哨兵节点连接在一起，形成一个环，哨兵节点的pre代表尾，哨兵节点的next代表头，就可以实现上面的需求
        最终方案：用一个哈希表保存频率和一个链环的哨兵节点（频率 -> 哨兵节点dummy），
            dummy.pre代表尾，即LRU
            dummy.next代表头，即最近使用过
    2. 对于1.3，用一个哈希表保存key和对应的节点
    3. 对于1.2.1，用一个类变量来记录
    4. 对于1.2.2和1.2.4，用一个类变量来记录，其逻辑为
        每次删除节点以后，判断当前频率上，是否有其他非dummy的节点：
            1. 如果有，不操作
            2. 如果没有，再判断被删除的节点的frequency是否为最小的frequency，如果是，那么最小的frequency加一
                因为本题没有主动删除的操作，所以当某个节点被删除时，只有两种情况：
                1. 该节点因为被使用，需要从当前频率去下一个频率（即frequency加一的频率）
                2. 该节点因为是最小frequency中的LRU，需要在容量满了的时候被剔除
                对于情况1，因为我们上面已经判断过，当前频率没有其他节点了，又知道当前频率就是最小频率，
                所以这个被删除的节点的新频率就是新的频率
                而对于情况2，容量满了的时候，说明一定有新的节点加入，所以哪怕当前频率没有其他节点且当前频率就是最小频率
                但新节点的频率一定是1（插入也算一次操作），所以新的最小频率一定是新插入的节点的频率，即1
        每次有新节点加入的时候，更新最小频率为1
 */

class LFUCache {

    /*
    keyMap：key -> 节点
     */
    private final Map<Integer, Node> keyMap;
    /*
    freqMap：频率 -> 哨兵节点
    每个节点，根据其频率的数值，会有一个自己的链表。我们对每个链表都设置一个哨兵节点，并将链表的头和尾都连接到哨兵节点，形成环
    例如：频率 = 1 -> 链表1，频率 = 2 -> 链表2，...
    我们通过删除和插入，维护链表，使链表的头代表 距离上次使用时间最短 的节点，链表的尾代表 距离上次使用时间最长的节点 的节点
    这样一来，dummy.next即为链表的头，dummy.pre即为链表的尾
    每次插入新的节点时，因为put和get都视为一次使用，每次使用，都代表该node为 距离上次使用时间最短 的节点，
    所以我们只会在链表的头插入节点，通过dummy.next可以实现O(1)的插入操作
    而删除节点时，有两种情况：
        1. 容量满载了（等于capacity），此时需要删除最小频率中，距离上次使用时间最长的节点，即链表的尾，通过dummy.pre可以找到，
            从而实现O(1)的删除操作
        2. 某个节点需要更新到更大的频率，此时因为有keyMap，我们可以直接定位到该节点node，然后通过node.pre和node.next，
            实现O(1)的删除操作
     */
    private final Map<Integer, Node> freqMap;
    private final int capacity;
    // 用一个静态变量保存所有node的最小频率
    private int minFreq = 1;
    public LFUCache(int capacity) {
        this.keyMap = new HashMap<>(capacity, 1);
        this.freqMap = new HashMap<>(capacity, 1);
        this.capacity = capacity;
    }

    /*
    1. 找keyMap中是否存在，不存在返回-1
    2. 如果存在node，由于get本身也算一次使用，所以需要更新node的数据
        1. 从当前频率的链表移除node
        2. 检查当前频率的链表是否只剩哨兵节点，如果是，则删除该频率，且更新minFreq
        3. 更新node频率
        4. 检查新的频率是否存在链表，如果不存在，则创建哨兵节点
        5. 将node加入新的频率所对应的链表
     */
    public int get(int key) {
        // 如果不存在，直接返回-1
        if (!keyMap.containsKey(key)) return -1;
        // 从keyMap中拿到key对应的node
        Node node = keyMap.get(key);
        // 先从当前频率对应的链表中移除自己，再更新频率，再加入新频率对应的链表
        updateFreq(node);
        return node.val;
    }

    /*
    1. 如果keyMap找不到，且满载了，则先通过minFreq删除该链表上的dummy.pre
    2. 获得或者创建node
    3. 更新node和map属性
        1. 从当前频率的链表移除node
        2. 检查当前频率的链表是否只剩哨兵节点，如果是，则删除该频率，且更新minFreq
        3. 更新node频率
        4. 检查新的频率是否存在链表，如果不存在，则创建哨兵节点
        5. 将node加入新的频率所对应的链表
     */
    public void put(int key, int value) {
        // 如果key不存在且容量满载了，需要先删除一个node
        if (!keyMap.containsKey(key) && keyMap.size() == capacity) {
            // 从当前最小的频率中找到该频率所对应的哨兵节点
            Node dummy = freqMap.get(minFreq);
            // 移除距离上次使用时间最长的节点，即dummy.pre
            removeNode(dummy.pre);
            // 更新minFreq = 1，因为新出现的key，一定是频率最低的
            minFreq = 1;
            // 如果key不存在，但容量没满
        } else if (!keyMap.containsKey(key)) {
            // 更新minFreq = 1，因为新出现的key，一定是频率最低的
            minFreq = 1;
        }
        // 拿到key对应的Node
        Node node = keyMap.getOrDefault(key, new Node(key, value));
        // 注意：一定记得更新value
        node.val = value;
        // 先从node当前频率对应的链表中移除自己，再更新频率，再加入新频率对应的链表
        updateFreq(node);
    }

    private void updateFreq(Node node) {
        // 如果node不是刚被创建出来的，则从链表中移除自己
        if (node.freq != 0) removeNode(node);
        // 更新node频率
        node.freq++;
        // 查询freqMap，看更新后的频率，是否有链表，如果有，拿到链表的哨兵节点，如果没有则创建
        Node dummy;
        if (freqMap.containsKey(node.freq)) {
            // 拿到链表的哨兵节点
            dummy = freqMap.get(node.freq);
        } else {
            // 创建新的哨兵节点
            dummy = new Node(-1, -1);
            // 设置dummy
            dummy.next = dummy;
            dummy.pre = dummy;
        }
        // 插入节点
        addNode(dummy, node);
    }

    // 从链表中移除的时候，一起移除keyMap和freqMap，更新minFreq
    /*
    1. 从链表移除
    2. 同时移除keyMap，注意：用node.key去移除
    3. 判断当前node.freq是否是空（即dummy.next ?= dummy或者dummy.pre ?= dummy）
        1. 如果为空，移除freqMap，注意：用node.freq去移除
        2. 如果为空，同时更新minFreq
     */
    private void removeNode(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.pre = null;
        node.next = null;
        keyMap.remove(node.key);
        /*
        检查当前频率是否还有其他节点，如果没有，则从freqMap中删除该频率
        即：从freqMap中获得哨兵节点，检查是否哨兵节点的next和pre都指向自己
        注意：不能用freqMap.get(node.freq).next.val == freqMap.get(node.freq).pre.val
        因为当链表只有一个哨兵节点和一个普通节点的时候，哨兵节点的pre和next都会指向普通节点，链表形成的环为：
        dummy.next = node
        dummy.pre = node
        node.next = dummy
        node.pre = dummy
        所以只能比较dummy.next == dummy或者dummy.pre == dummy
         */
        if (freqMap.get(node.freq).next == freqMap.get(node.freq)) {
            freqMap.remove(node.freq);
            /*
            如果被移除的node的频率，刚好是minFreq时，
            此时因为node.freq已经没有其他节点了，且node无论是get还是put，会进入下一个频率，即node.freq++，所以minFreq也可以一起++
             */
            if (node.freq == minFreq) minFreq++;
        }
    }

    /*
    1. 加入链表
    2. 同时加入keyMap，注意：用node.key去加入
    2. 同时加入freqMap，注意：用node.freq去加入
     */
    private void addNode(Node dummy, Node node) {
        dummy.next.pre = node;
        node.next = dummy.next;
        node.pre = dummy;
        dummy.next = node;
        // 将node放入keyMap
        keyMap.put(node.key, node);
        // 将哨兵节点放入freqMap
        freqMap.put(node.freq, dummy);
    }

    static class Node {
        int key;
        int val;
        int freq;
        Node pre;
        Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
            int freq = 0;
            this.pre = null;
            this.next = null;
        }
    }
}
