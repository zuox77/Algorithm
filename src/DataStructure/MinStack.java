package DataStructure;
import java.util.Stack;
/*
https://leetcode.cn/problems/min-stack/
https://www.lintcode.com/problem/12/
https://www.jiuzhang.com/problem/min-stack/

思路：
需要考虑的点：
1. 因为要求用O(1)的时间获得当前最小值，千万记得不能用一个int变量去存储，因为如果当前最小值被pop出去了，怎么获得次小值呢
2. 所以其实内部是两个栈，一个栈用来保存数，一个栈用来保存最小值
3. 为了方便，保存最小值的栈可以时刻与另一个栈的高度保持一致：当push进来一个更小的值的时候，两个栈都加入这个值，
    但当push进来一个大于当前最小值的时候，最小值的栈加入一个当前最小值进去，这样的话，在pop时，就不需要判断，直接将两个栈都pop出去一个就可以
 */
public class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack() {
        /*
         注意这里不能写成：
         Stack<Integer> stack = new Stack<>();
         Stack<Integer> minStack = new Stack<>();
         因为这样写表明你在申明和定义一个新的stack和minStack，而且这个新的stack和minStack是一个局部变量，
         仅属于MinStack()这个方法，所以当你使用MinStack这个类的时候：
         MinStack test = new MinStack();
         test.push(val);
         就会报错，因为new MinStack()以后，会定义和初始化两个新的局部变量stack和minStack，而后方法结束，这两个局部变量也随着方法结束而消失，
         当你test.push(val)的时候，调用stack.push(val)时，此时的stack是上方的类变量private Stack<Integer> stack，
         这个类变量并没有初始化：
         NullPointerException: Cannot invoke "java.util.Stack.push(Object)" because "this.stack" is null
         */
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty()) {
            minStack.push(val);
        } else {
            minStack.push(Math.min(val, minStack.peek()));
        }
    }

    public void pop() {
        minStack.pop();
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
