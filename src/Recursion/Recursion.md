递归回溯的时候, 对于需要回溯的List/ArrayList的方法: 
1. 因为回溯肯定是回溯最后一个, 所以用remove(index)的方法: path.remove(path.size() - 1)
-----
递归回溯的时候, 对于需要回溯的int[]的方法: 
1. 因为int[]是Array, 是固定长度数组, 所以只需要将其最后一位变为默认值就好: int[] path[path.length - 1] = 0;
-----
递归到出口或者需要将现有结果添加到最终结果时: 
1. 对于List/ArrayList不能直接用add, 因为添加的是一个引用指针, 指向现有结果, 所以现有结果发生改变, 也会影响最终结果里面的元素
2. result.add(new ArrayList<Integer>(path);