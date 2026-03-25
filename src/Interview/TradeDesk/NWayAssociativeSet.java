package Interview.TradeDesk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.*;
import java.util.*;

public class NWayAssociativeSet {

    /*
     * [改动说明：未做改动]
     * 作用：程序入口，直接调用测试框架的解析逻辑。
     */
    public static void main(String[] args) throws IOException {
        SetAssociativeCacheRunner.parseInput(System.in);
    }

    static class SetAssociativeCacheRunner {
        /*
         * [改动说明：未做改动]
         * 作用：读取控制台输入，解析命令并调用对应方法。
         */
        public static void parseInput(InputStream inputStream) throws IOException {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            int lineCount = 0;
            SetAssociativeCache<String, String> cache = null;

            while (!isNullOrEmpty(line = reader.readLine())) {
                lineCount++;
                OutParam<String> replacementAlgoName = new OutParam<>();

                if (lineCount == 1) {
                    cache = createCache(line, replacementAlgoName);
                } else {
                    Object retValue = SetAssociativeCacheFactory.invokeCacheMethod(line, cache);
                    if (retValue != null) {
                        System.out.println(retValue);
                    }
                }
            }
        }
    }

    /*
     * [改动说明：增加参数传递]
     * 原代码：return SetAssociativeCacheFactory.CreateStringCache(setCount, setSize, replacementAlgoName.value);
     * 改成代码：逻辑几乎未变，但为了满足要求 #3 (解耦替换算法)，确保 replacementAlgoName.value 被正确传递给 Cache 的构造函数。
     * 为什么改：我们需要将测试用例指定的算法名称（如 LRU 或 MRU）透传到底层的组中，以便动态生成对应的淘汰算法实例。
     */
    private static SetAssociativeCache<String, String> createCache(String inputLine, OutParam<String> replacementAlgoName) {
        String[] cacheParams = Arrays.stream(inputLine.split(","))
                .map(s -> s.trim())
                .toArray(n -> new String[n]);
        int setCount = Integer.parseInt(cacheParams[0]);
        int setSize = Integer.parseInt(cacheParams[1]);
        replacementAlgoName.value = cacheParams[2];

        return SetAssociativeCacheFactory.createStringCache(setCount, setSize, replacementAlgoName.value);
    }

    // ############################ BEGIN Solution Classes ############################

    public static class SetAssociativeCache<TKey, TValue> {
        int capacity;
        int setSize;
        int setCount;
        CacheSet<TKey, TValue>[] sets;

        /*
         * [改动说明：修改构造函数签名及初始化逻辑]
         * 原代码：
         * public SetAssociativeCache(int setCount, int setSize) { ... }
         * 改成代码：
         * public SetAssociativeCache(int setCount, int setSize, String algoName) { ... }
         * 为什么改：需要接收外层传进来的 algoName（算法名称），并在初始化内部的 CacheSet 数组时，把算法名称传给每一个 Set。
         */
        public SetAssociativeCache(int setCount, int setSize, String algoName) {
            this.setCount = setCount;
            this.setSize = setSize;
            this.capacity = this.setCount * this.setSize;

            this.sets = new CacheSet[this.setCount];
            for (int i = 0; i < this.setCount; i++) {
                sets[i] = new CacheSet<>(setSize, algoName);
            }
        }

        /*
         * [改动说明：未做改动] (除变量名外)
         * 作用：对外暴露的获取值接口。
         */
        public TValue get(TKey key) {
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.sets[setIndex];
            return set.get(key);
        }

        /*
         * [改动说明：未做改动] (除变量名外)
         * 作用：对外暴露的写入值接口。
         */
        public void set(TKey key, TValue value) {
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.sets[setIndex];
            set.set(key, value);
        }

        /*
         * [改动说明：未做改动] (除变量名外)
         * 作用：统计所有组中当前元素的总和。
         */
        public int getCount() {
            int currentCount = 0;
            for (int i = 0; i < this.sets.length; i++) {
                currentCount += this.sets[i].getCount();
            }
            return currentCount;
        }

        /*
         * [改动说明：未做改动] (除变量名外)
         * 作用：检查某 Key 是否存在。
         */
        public boolean containsKey(TKey key) {
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.sets[setIndex];
            return set.containsKey(key);
        }

        /*
         * [改动说明：重大核心逻辑修正 (响应 Requirement #1)]
         * 原代码：
         * private int getSetIndex(TKey key) {
         * int c = Integer.MAX_VALUE; int s = -1;
         * for (int i = 0; i < this.Sets.length; i++) {
         * if (this.Sets[i].containsKey(key)) return i;
         * if (this.Sets[i].Count < c) { c = this.Sets[i].Count; s = i; }
         * }
         * return s;
         * }
         * 改成代码：利用 key 的哈希值取模。
         * 为什么改：原代码是 O(N) 的线性遍历找“最空”的组，这是完全错误的机制。真正的组相联缓存必须通过哈希映射，保证同一个 Key 永远映射到固定的 Set 中，从而实现 O(1) 的路由。
         */
        private int getSetIndex(TKey key) {
            int hash = key.hashCode();
            if (hash == Integer.MIN_VALUE) {
                hash = 0; // 防止 Math.abs(MIN_VALUE) 溢出返回负数导致数组越界
            }
            return Math.abs(hash) % this.setCount;
        }
    }

    static class CacheSet<TKey, TValue> {
        int capacity;
        Map<TKey, TValue> store;
        IReplacementAlgo<TKey> replacementAlgo;

        /*
         * [改动说明：重构数据结构 (响应 Requirement #2 & #3)]
         * 原代码：
         * CacheItem<TKey, TValue>[] Store;
         * LinkedList<TKey> UsageTracker;
         * public CacheSet(int capacity) { ... }
         * 改成代码：使用了 HashMap 和 独立的 IReplacementAlgo 接口。
         * 为什么改：
         * 1. 原本的数组查询是 O(K) 的，替换为 HashMap 实现 O(1) 查询。
         * 2. 删除了 Java 原生的 LinkedList（其 remove(Object) 方法是 O(K) 遍历），把所有淘汰逻辑委托给 replacementAlgo。
         */
        public CacheSet(int capacity, String algoName) {
            this.capacity = capacity;
            this.store = new HashMap<>(capacity);
            this.replacementAlgo = ReplacementAlgoFactory.createReplacementAlgo(algoName);
        }

        /*
         * [改动说明：增加并发锁与委托机制 (响应 Requirement #5)]
         * 原代码：普通方法，在普通数组中查值，并自己管理 LinkedList。
         * 改成代码：增加了 synchronized 关键字；直接从 HashMap 拿值；通知算法记录使用情况。
         * 为什么改：加锁实现线程安全（分段锁思想，不同 Set 互不干扰）。查表和记录频率的逻辑分离。
         */
        public synchronized TValue get(TKey key) {
            if (!this.containsKey(key)) {
                throw new RuntimeException(String.format("The key '%s' was not found", key));
            }
            this.replacementAlgo.recordUsage(key);
            return this.store.get(key);
        }

        /*
         * [改动说明：重构淘汰逻辑与增加并发锁]
         * 原代码：包含了几十行复杂的逻辑，包括判断是否满、查找替换目标、调用 LinkedList 的 getLast() 等。
         * 改成代码：增加了 synchronized；直接判断 HashMap 容量；满时调用 replacementAlgo.chooseVictim() 获取被淘汰者并删除。
         * 为什么改：
         * 1. 线程安全。
         * 2. O(1) 复杂度。
         * 3. 彻底解耦。CacheSet 不再需要知道当前是 LRU 还是 MRU，它只管问算法“我该淘汰谁”。
         */
        public synchronized void set(TKey key, TValue value) {
            if (this.store.containsKey(key)) {
                this.store.put(key, value);
            } else {
                if (this.store.size() >= this.capacity) {
                    TKey victim = this.replacementAlgo.chooseVictim();
                    this.store.remove(victim);
                    this.replacementAlgo.removeKey(victim);
                }
                this.store.put(key, value);
            }
            this.replacementAlgo.recordUsage(key);
        }

        /*
         * [改动说明：增加并发锁，替换查找方式]
         * 原代码：return this.findIndexOfKey(key) >= 0; （O(K) 复杂度）
         * 改成代码：加 synchronized，利用 HashMap 的 containsKey。 （O(1) 复杂度）
         * 为什么改：线程安全与性能优化。
         */
        public synchronized boolean containsKey(TKey key) {
            return this.store.containsKey(key);
        }

        /*
         * [改动说明：原代码此逻辑为 public 属性 this.Count，现改为方法并加锁]
         * 为什么改：直接暴露 public 属性在多线程下不安全，改为受锁保护的 getCount() 方法获取 Map 大小。
         */
        public synchronized int getCount() {
            return this.store.size();
        }

        /*
         * [改动说明：删除了原代码的 removeKey, findIndexOfKey, recordUsage 方法]
         * 为什么改：这些 O(K) 的线性查找和数组操作代码已经被 HashMap 和专属的算法类完美取代，故完全删除。
         * CacheItem 类也一并删除了，因为不再需要自定义数组容器。
         */
    }

    public final static String lruAlgorithm = "LRUReplacementAlgo";
    public final static String mruAlgorithm = "MRUReplacementAlgo";

    /*
     * [改动说明：设计了全新的抽象接口 (响应 Requirement #3)]
     * 作用：定义了所有淘汰算法都必须遵循的三个标准动作：记录使用、选出牺牲者、移除键。
     */
    interface IReplacementAlgo<TKey> {
        void recordUsage(TKey key);
        TKey chooseVictim();
        void removeKey(TKey key);
    }

    /*
     * [改动说明：全新增加的基类]
     * 作用：自己手写了一个 O(1) 的双向链表 (Doubly-Linked List) 加上 HashMap。
     * 为什么改：Java 自带的 LinkedList 删除指定元素需要遍历 O(N)。为了达到真正的 O(1) 性能，必须通过 Map 保存链表节点的引用，从而实现 O(1) 的断键重连。这是 LRU/MRU 的标准底层实现。
     */
    abstract static class BaseReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {
        static class Node<T> {
            T key;
            Node<T> prev, next;
            Node(T key) { this.key = key; }
        }

        Map<TKey, Node<TKey>> nodeMap = new HashMap<>();
        Node<TKey> head = new Node<>(null);
        Node<TKey> tail = new Node<>(null);

        public BaseReplacementAlgo() {
            head.next = tail;
            tail.prev = head;
        }

        @Override
        public void recordUsage(TKey key) {
            if (nodeMap.containsKey(key)) {
                removeNode(nodeMap.get(key));
            }
            Node<TKey> node = new Node<>(key);
            addLast(node);
            nodeMap.put(key, node);
        }

        @Override
        public void removeKey(TKey key) {
            if (nodeMap.containsKey(key)) {
                removeNode(nodeMap.get(key));
                nodeMap.remove(key);
            }
        }

        private void removeNode(Node<TKey> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addLast(Node<TKey> node) {
            node.prev = tail.prev;
            node.next = tail;
            tail.prev.next = node;
            tail.prev = node;
        }
    }

    /*
     * [改动说明：全新实现的 LRU 类 (响应 Requirement #3)]
     * 作用：继承自基类，只需要重写 chooseVictim 即可。LRU 淘汰链表头部（最久未访问）。
     */
    static class LRUReplacementAlgo<TKey> extends BaseReplacementAlgo<TKey> {
        @Override
        public TKey chooseVictim() {
            if (head.next == tail) return null;
            return head.next.key;
        }
    }

    /*
     * [改动说明：全新实现的 MRU 类 (响应 Requirement #4)]
     * 作用：继承自基类，只需要重写 chooseVictim 即可。MRU 淘汰链表尾部（刚刚才访问过）。
     */
    static class MRUReplacementAlgo<TKey> extends BaseReplacementAlgo<TKey> {
        @Override
        public TKey chooseVictim() {
            if (tail.prev == head) return null;
            return tail.prev.key;
        }
    }

    // ############################ BEGIN Helper Classes ############################

    static class OutParam<T> {
        public T value;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static class SetAssociativeCacheFactory {
        /*
         * [改动说明：透传算法名称]
         * 原代码：return new SetAssociativeCache<>(setCount, setSize);
         * 改成代码：传入了 replacementAlgoName
         * 为什么改：需要把字符串传递给缓存构造器，以便实例化策略模式。
         */
        public static SetAssociativeCache<String, String> createStringCache(int setCount, int setSize, String replacementAlgoName) {
            return new SetAssociativeCache<>(setCount, setSize, replacementAlgoName);
        }

        /*
         * [改动说明：未做改动] (仅修复了方法名驼峰和调用方式)
         * 作用：反射模拟，根据输入的字符串调用对应缓存 API。
         */
        public static Object invokeCacheMethod(String inputLine, SetAssociativeCache<String, String> cacheInstance) {
            String[] callArgs = Arrays.stream(inputLine.split(",", -1))
                    .map(a -> a.trim())
                    .toArray(n -> new String[n]);
            String methodName = callArgs[0].toLowerCase();

            switch (methodName) {
                case "get":
                    return cacheInstance.get(callArgs[1]);
                case "set":
                    cacheInstance.set(callArgs[1], callArgs[2]);
                    return null;
                case "containskey":
                    return cacheInstance.containsKey(callArgs[1]);
                case "getcount":
                    return cacheInstance.getCount();
                default:
                    throw new RuntimeException(String.format("Unknown method name '{%s}'", methodName));
            }
        }
    }

    public static class ReplacementAlgoFactory {
        /*
         * [改动说明：完善了工厂方法生成逻辑]
         * 原代码：留空并要求 TODO。
         * 改成代码：加入了 switch-case 生成我们刚才写的 LRU 和 MRU 实例，并添加泛型支持。
         * 为什么改：实现通过字符串动态创建算法策略类。
         */
        public static <TKey> IReplacementAlgo<TKey> createReplacementAlgo(String replacementAlgoName) {
            switch (replacementAlgoName) {
                case lruAlgorithm:
                    return new LRUReplacementAlgo<>();
                case mruAlgorithm:
                    return new MRUReplacementAlgo<>();
                default:
                    throw new RuntimeException(String.format("Unknown replacement algo '%s'", replacementAlgoName));
            }
        }
    }
}
