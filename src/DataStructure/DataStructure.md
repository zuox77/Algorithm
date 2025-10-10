# Data Structure 总结

## 哈希表 HashMap

### 哈希表的key和values的类型

```
map.keyset()   --> Set<String>
map.values()   --> Collection<List<String>>
map.entrySet() --> Set<Entry<String,List<String>>> --> for (HashMap.Entry<String, Integer> entry: map.entrySet())
```

### 自建哈希表

1. 通过数组实现
2. 利用数组的下标和数组的值来代替哈希表中的key-value关系
