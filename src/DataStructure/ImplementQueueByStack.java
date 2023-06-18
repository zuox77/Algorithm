package DataStructure;
import java.util.Stack;
/*
https://leetcode.cn/problems/implement-queue-using-stacks/description/
https://www.lintcode.com/problem/40/
https://www.jiuzhang.com/problem/implement-queue-by-two-stacks/

思路: 
1. queue是FIFO, stack是FILO, 二者其实pop的顺序刚好完全相反
    比如queue: 进1 -> 2 -> 3, 出1 -> 2 -> 3, stack: 进1 -> 2 -> 3, 出3 -> 2 -> 1
    所以只需要将所有数存进一个stack, 在需要pop的时候, 将其一个个pop出来, 另一个stack刚好一个个在push进去, 
    就完成了对所有数的reverse
2. 将所有数存入stack2
3. 如果需要pop或者top, 那么就将stack2里的所有元素pop出去, stack1接收, 完成reverse
4. reverse之前要查一下stack1是不是空, 因为可能出现连续pop或者top的情况, 这时候第一个pop或者top已经将所有stack2元素转移并reverse到stack1了, 
    stack1里不为空, 那么就说明所有元素都进去了
 */
public class ImplementQueueByStack {
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;

    public ImplementQueueByStack () {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    public void push(int x) {
        stack2.push(x);
    }

    public int pop() {
        if (stack1.isEmpty()) {
            convert();
        }
        return stack1.pop();
    }

    public int peek() {
        if (stack1.isEmpty()) {
            convert();
        }
        return stack1.peek();
    }

    public void convert() {
        while (!stack2.isEmpty()) {
            stack1.push(stack2.pop());
        }
    }

    public boolean empty() {
        return stack1.isEmpty() && stack2.isEmpty();
    }
}
