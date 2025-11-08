package DataStructure.Stack;

import java.util.ArrayDeque;
import java.util.Deque;

/*
https://leetcode.cn/problems/min-stack/description/?envType=study-plan-v2&envId=top-100-liked

Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

Implement the MinStack class:

MinStack() initializes the stack object.
void push(int val) pushes the element val onto the stack.
void pop() removes the element on the top of the stack.
int top() gets the top element of the stack.
int getMin() retrieves the minimum element in the stack.
You must implement a solution with O(1) time complexity for each function.

Example 1:
Input
["MinStack","push","push","push","getMin","pop","top","getMin"]
[[],[-2],[0],[-3],[],[],[],[]]

Output
[null,null,null,null,-3,null,0,-2]

Explanation
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin(); // return -3
minStack.pop();
minStack.top();    // return 0
minStack.getMin(); // return -2

思路: 栈
1. 首先需要知道，栈其实本身没有单调性，只有我们用特定条件让特定的值入栈的时候，才能构成单调栈，并且构成的单调栈也不是绝对单调，我们往往只能
    保证：栈首/栈尾是单调的，或者说，栈首一定比其他的大/小。
2. 这道题最主要的点在于，当某个当前最小的值被pop出去以后，如何找到上一个最小值。（有点类似于堆）
    比如[-10,-5,-20,20]，假设这是当前被push进去的数字。此刻的minValue是-20。
    当我们把20给推出栈时，[-10,-5,-20]，此时的minValue还是-20
    但如果我们把-20给推出栈时，[-10,-5]，此时的minValue是-10，所以如何保寸这个minValue就是这道题的考点
3. 思路就是用一个数来保存（或者也可以用一个list）来保存minValue，用一个int[]来同时保存当前值以及当前值被push进去的那一刻的minValue
4. 注意：在pop的时候，需要同时更新当前的minValue的值
    先pop出去，然后检查stack是否为空。
    如果stack不为空，那么就从stack的栈首去找他的minValue并且更新
    如果stack为空，那么设置成Integer.MAX_VALUE
    或者也可以在初始化的时候，额外push一个dummy进去，这个dummy的值是0，这个dummy对应的minValue是Integer.MAX_VALUE
 */

/**
 * Your MinStack object will be instantiated and called as such: MinStack obj = new MinStack();
 * obj.push(val); obj.pop(); int param_3 = obj.top(); int param_4 = obj.getMin();
 */
class MinStack {

    private Deque<int[]> stack = new ArrayDeque<>();
    private int minValue = Integer.MAX_VALUE;

    public MinStack() {}

    public void push(int val) {
        minValue = Math.min(val, minValue);
        int[] pair = {val, minValue};
        stack.addFirst(pair);
    }

    public void pop() {
        stack.removeFirst();
        if (stack.isEmpty()) {
            minValue = Integer.MAX_VALUE;
        } else {
            minValue = getMin();
        }
    }

    public int top() {
        return stack.peekFirst()[0];
    }

    public int getMin() {
        return stack.peekFirst()[1];
    }
}
