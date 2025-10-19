package DataStructure;

import java.util.LinkedList;
import java.util.Queue;

/*
https://leetcode.cn/problems/implement-stack-using-queues/description/?orderBy=most_votes

思路:
1. 栈是先进后出, 队列是先进先出
2. 用两个队列, 每次加入新元素时, 先加入queue2, 然后将queue1里面的元素一个个poll出来加到queue2里面去, 从而人为形成先进后出的顺序
3. Queue<Integer> queue = new LinkedList<>();
 */

public class ImplementStackByQueue {
  class MyStack {
    private Queue<Integer> queue1;
    private Queue<Integer> queue2;

    public MyStack() {
      queue1 = new LinkedList<>();
      queue2 = new LinkedList<>();
    }

    public void push(int x) {
      queue2.add(x);
      while (!queue1.isEmpty()) {
        queue2.add(queue1.poll());
      }
      Queue<Integer> tmp = queue1;
      queue1 = queue2;
      queue2 = tmp;
    }

    public int pop() {
      return queue1.poll();
    }

    public int top() {
      return queue1.peek();
    }

    public boolean empty() {
      return queue1.isEmpty();
    }
  }
}
