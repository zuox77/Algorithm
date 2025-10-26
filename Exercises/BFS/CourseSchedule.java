package BFS;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/*
刷题次数: 1

There are a total of numCourses courses you have to take, labeled from 0 to numCourses-1.
You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return true if you can finish all courses. Otherwise, return false.

Example 1:

Input: numCourses = 2, prerequisites = [[1,0]]
Output: true
Explanation: There are a total of 2 courses to take.
To take course 1 you should have finished course 0. So it is possible.
Example 2:

Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false
Explanation: There are a total of 2 courses to take.
To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.

思路1: BFS
1. 经典BFS和图论题
2. 利用拓扑排序来做：
    1. 创建一个与总课程数量相等的List（degree），List的index代表每个课程，数字代表degree（即当前课程有几个前置课程）
    2. 创建一个与总课程数量相等的List<List<Integer>>（relationships）：
        1. 外侧List的index代表每个课程
        2. 内侧List代表每个课程具体有哪几个前置课程
    3. 通过遍历，先找到每个课程的degree和对应的前置课程，即完善第一第二步
    4. 将degree为0的课程加入queue（即完全没有前置课程的课程）
    5. 通过while循环，将queue的第一个数poll出来，同时减少numCourse（每次poll出来就要减少一个numCourse）
    6. 在relationships的List找到后置可学课程，并将其对应的degree减少1，然后重复第四部，将degree为0的加入queue

    例子：
    numCourse = 5，
    int[][] prerequisites =
    [
        [3, 0],
        [1, 0],
        [3, 1],
        [2, 1],
        [4, 3],
        [4, 2]
    ]
    第一步：degree = [0, 0, 0, 0, 0]
    第二步：relationships = [
                                [],
                                [],
                                [],
                                [],
                                []
                            ]
    第三步：degree = [0, 1, 1, 2, 2]
           relationships = [
                                [3, 1],  （通过prerequisites的[3, 0]和[1, 0]得到）
                                [3, 2],  （通过prerequisites的[3, 1]和[2, 1]得到）
                                [4],     （通过prerequisites的[4, 2]得到）
                                [4],     （通过prerequisites的[4, 3]得到）
                                []
                            ]
    第四步：因为degree中，只有index=0的值为0，所以将0加入queue
    第五步：从queue中poll出第一个数（即0），numCourse-1（=4）
    第六步：relationships.get(0) = [3, 1]，所以将3和1的degree分别减少1，degree = [0, 0, 1, 1, 2]
           在减少1的同时去判断degree是否为0，这样可以避免重复访问，此时将1加入queue
    第五步：从queue中poll出第一个数（即1），numCourse-1（=3）
    第六步：relationships.get(1) = [3, 2]，所以将3和2的degree减少1，degree = [0, 0, 0, 0, 2]
           在减少1的同时去判断degree是否为0，这样可以避免重复访问，此时将3和2加入queue
    第五步：从queue中poll出第一个数（即3），numCourse-1（=2）
    第六步：relationships.get(3) = [4]，所以将4的degree减少1，degree = [0, 0, 0, 0, 1]
           在减少1的同时去判断degree是否为0，这样可以避免重复访问，此时没有新的degree为0的数，所以不加入任何数进入queue
    第五步：从queue中poll出第一个数（即2），numCourse-1（=1）
    第六步：relationships.get(2) = [4]，所以将4的degree减少1，degree = [0, 0, 0, 0, 0]
           在减少1的同时去判断degree是否为0，这样可以避免重复访问，此时将4加入queue
    第五步：从queue中poll出第一个数（即4），numCourse-1（=0）
    第六步：relationships.get(4) = []，所以不做任何事，也不加入任何数进入queue
    退出循环，返回：numCourse == 0

 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 创建一个与总课程数量相等的List
        int[] degrees = new int[numCourses];
        // 创建一个与总课程数量相等的List<List<Integer>>
        List<List<Integer>> relationships = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            relationships.add(new ArrayList<>());
        }
        // 创建Queue
        Queue<Integer> queue = new ArrayDeque<>();

        // Iterate to get degrees and prerequisites
        for (int[] cp : prerequisites) {
            degrees[cp[0]]++;
            relationships.get(cp[1]).add(cp[0]);
        }

        // Get all 0 degree courses and add to queue
        for (int i = 0; i < numCourses; i++) {
            if (degrees[i] == 0) {
                queue.offer(i);
            }
        }

        // Iterate to find ring
        while (!queue.isEmpty()) {
            int course = queue.poll();
            numCourses--;
            for (int nextCourse : relationships.get(course)) {
                if (--degrees[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }

        return numCourses == 0;
    }
}
