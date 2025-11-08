# BFS总结

https://leetcode.cn/problems/number-of-distinct-islands/solutions/1252714/dfs-suan-fa-miao-sha-dao-yu-xi-lie-ti-mu-m7k1/

## Queue的用法

### 类关系图

```
Collection (interface)
|
Queue (interface)
|
Deque (interface)
|
ArrayDeque (class), LinkedList (class), Priority Queue (class)

```

### 常用两种声明方式

```
Queue<Integer> queue = new ArrayDeque<>();
Queue<Integer> queue = new LinkedList<>();
```

### 队列

```
Queue<Integer> queue = new ArrayDeque<>();
// 不会抛出异常
queue.offer(); // 从tail加入新元素 = offerLast()
queue.poll();  // 从head移除尾部元素 = pollFirst()
queue.peek();  // 从head读取 = peekFirst()
```

### 双端队列

```
Deque<Integer> deque = new ArrayDeque<>();
// 不会抛出异常
deque.offerFirst(); // 从head加入新元素
deque.offerLast();  // 从tail加入新元素
deque.pollFirst();  // 从head移除尾部元素
deque.pollLast();   // 从tail移除尾部元素
deque.peekFirst();  // 从head读取
deque.peekLast();   // 从tail读取
```

## BFS公式

### 单点式

单点式, 即只有一个入口, 比如像树类结构, 往往我们都从根节点开始, 此类问题不需要额外的循环

```
// 以树类结构举例
// 声明队列
Queue<TreeNode> queue = new ArrayDeque<>();
// 声明哈希集合
Set<TreeNode> visited = new HashSet<>();
// 将第一个点加入队列
queue.offer(root);
// 如果有需要, 要在把元素添加进队列的时候, 也立马将其标记为已访问
visited.add(root);
// 遍历队列, 并不断根据情况, 将合适的节点加入队列
while (!queue.isEmpty()) {
    // 将节点弹出
    TreeNode node = queue.poll();
    // 对节点做判断
    一些判断代码
    // 将合适的节点加入队列
    queue.offer(合适的节点)
    // 如果有需要, 要在把元素添加进队列的时候, 也立马将其标记为已访问
    visited.add(合适的节点);
}
```

### 多点式

多点式, 即有多个入口, 比如像矩阵和图类结构, 往往需要通过一个循环, 判断情况, 将合适的节点加入队列

```
// 以矩阵举例
// 声明队列
Queue<TreeNode> queue = new ArrayDeque<>();
// 声明哈希集合
Set<TreeNode> visited = new HashSet<>();
// 遍历矩阵
for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
        // 做判断
        if (一些判断) {
            queue.add(坐标);
            // 一定记得每次加入队列以后, 一定要立马标记为已访问
            // 矩阵类型的问题, 可以通过改变矩阵的值来标记
            matrix[i][j] = 一个不可能的值, 比如负值
            // 也可以用哈希集合, 不过如果是矩阵, 用这个办法可能麻烦一些, 后面细讲
            visited.add(合适的坐标);
            // 遍历队列, 并不断根据情况, 将合适的节点加入队列
            while (!queue.isEmpty()) {
                // 将节点弹出
                TreeNode node = queue.poll();
                // 对节点做判断
                一些判断代码
                // 将合适的节点加入队列
                queue.offer(合适的坐标)
                // 如果有需要, 要在把元素添加进队列的时候, 也立马将其标记为已访问
                visited.add(合适的节点); // 或者matrix[i][j] = 一个不可能的值, 比如负值
            }
        }
    }
}
```

### 需要注意的点

#### 标记已访问

1. 这是BFS中最需要注意的问题, 主要是对于多点式的结构,因为多点式的结构往往可以重复访问一个节点,
   不像树类结构, 一般只能从根节点遍历到子节点, 子节点无法遍历回去
2. 还需要注意的是, 每次将元素加入队列以后, 需要立马将加入队列的元素标记为已访问, 原因是, 如果在其他时间标记,
   那么在队列中的其他元素, 可能会访问到这个已访问的元素, 会导致重复访问, 轻则超时, 重则结果错误

##### 对于图类结构

用哈希集合来记录

```
Set<TreeNode> visited = new HashSet<>();
...
// 将节点加入队列
queue.offer(node);
// 立马将其标记为已访问
visited.add(node)
```

##### 对于矩阵

1. 通过直接改变矩阵的值

```
// 将节点加入队列
queue.offer(坐标);
// 立马将其标记为已访问
matrix[i][j] = 一个不可能的值, 比如负值
```

2. 通过将矩阵看成一个一维数组, 比如

```
[0,0,1,0,0,0,0]
[0,0,0,5,0,0,0]
[0,1,1,0,1,0,0]
这是一个 3*7 的矩阵
对于坐标(1, 3), 即数字5, 可以将其看成第11个数
即将矩阵看成
[0,0,1,0,0,0,0,0,0,0,5,0,0,0,0,1,1,0,1,0,0]
则数字5是下标10

转化公式, 对于坐标(i, j): 
坐标->第几个数: 1 * 7 + 3 = 10 -> i * n + j
第几个数->坐标: 10 / 7 = 1 = i, 10 % 7 = 3 = j -> i = num / n, j = num % n
```

##### 对于树类结构

一般不需要, 因为树类结构无法做到重复访问, 子节点一般无法访问根节点