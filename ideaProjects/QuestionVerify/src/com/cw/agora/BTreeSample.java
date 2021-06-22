package com.cw.agora;

import java.util.*;

public class BTreeSample {

    /*
        // 二叉树
            10
          /   \
         3     5
        / \     \
       8   20    7
                / \
               2   4
    */
    //前序遍历 根左右 10 -> 3 -> 8 -> 20 -> 5 -> 7 -> 2 -> 4
    //中序遍历 左根右 8 -> 3 -> 20 -> 10 -> 5 -> 2 -> 7 -> 4
    //后序遍历 左右根 8 -> 20 -> 3 -> 2 -> 4 -> 7 -> 5 -> 10

    // 创建二叉树
    public Node createBtree() {
        Node deep1_1 = new Node(10);   // deep 1 (root)

        Node deep2_1 = new Node(3);    // deep 2
        Node deep2_2 = new Node(5);


        Node deep3_1 = new Node(8);    // deep 3
        Node deep3_2 = new Node(20);
        Node deep3_3 = new Node(7);

        Node deep4_1 = new Node(2);    // deep 4
        Node deep4_2 = new Node(4);


        deep1_1.mLeft = deep2_1;
        deep1_1.mRight = deep2_2;

        deep2_1.mLeft = deep3_1;
        deep2_1.mRight = deep3_2;

        deep2_2.mRight = deep3_3;

        deep3_3.mLeft = deep4_1;
        deep3_3.mRight = deep4_2;

        return deep1_1;
    }


    // 深度优先遍历-前序遍历-递归
 public void preorder_traversal_recursive(Node node) {
        if (null == node) {
            return;
        }

        System.out.printf(node.mValue + " -> ");

        if (null != node.mLeft) {
            preorder_traversal_recursive(node.mLeft);
        }

        if (null != node.mRight) {
            preorder_traversal_recursive(node.mRight);
        }
    }

    // 深度优先遍历-前序遍历-非递归
    public void preorder_traversal(Node node) {
        if (null == node) {
            return;
        }


        Stack<Node> stack = new Stack<>();
        stack.add(node);  // 加入头结点

        while (!stack.isEmpty()) {
            Node curNode = stack.peek();  // 访问第一个
            System.out.printf(curNode.mValue + " -> ");
            stack.pop(); // 移除第一个

            // 因为左子树要先处理，所以右子树先入栈，再左子树入栈，弹出时先处理左子树
            if (null != curNode.mRight) {
                // 右子树入队end
                stack.add(curNode.mRight);
            }

            if (null != curNode.mLeft) {
                // 左子树入队end
                stack.add(curNode.mLeft);
            }
        }

    }

    // 深度优先遍历-中序遍历-递归
    public void inorder_traversal_recursive(Node node) {
        if (null == node) {
            return;
        }

        if (null != node.mLeft) {
            inorder_traversal_recursive(node.mLeft);
        }

        System.out.printf(node.mValue + " -> ");

        if (null != node.mRight) {
            inorder_traversal_recursive(node.mRight);
        }

    }

    // 深度优先遍历-中序遍历-非递归
    public void inorder_traversal(Node node) {
        if (null == node) {
            return;
        }


        Stack<Node> stack = new Stack<>();

        while (!stack.isEmpty() || null != node) {
            while (null != node) {   // 如果左子树不为空，则一直入栈，并遍历下去
                stack.push(node);
                node = node.mLeft;
            }

            if (!stack.isEmpty()) { // 左子树为空，则出栈打印，并继续访问右子树
                node = stack.peek();
                System.out.printf(node.mValue + " -> ");
                stack.pop();

                node = node.mRight;
            }
        }

    }

    // 深度优先遍历-后序遍历-递归
    public void postorder_traversal_recursive(Node node) {
        if (null == node) {
            return;
        }

        if (null != node.mLeft) {
            postorder_traversal_recursive(node.mLeft);
        }

        if (null != node.mRight) {
            postorder_traversal_recursive(node.mRight);
        }

        System.out.printf(node.mValue + " -> ");

    }

    /*
    思路：要保证根结点在左孩子和右孩子访问之后才能访问，因此对于任一结点P，先将其入栈。
    如果P不存在左孩子和右孩子，则可以直接访问它；或者P存在左孩子或者右孩子，但是其左孩子和右孩子都已被访问过了，则同样可以直接访问该结点。
    若非上述两种情况，则将P的右孩子和左孩子依次入栈，这样就保证了每次取栈顶元素的时候，左孩子在右孩子前面被访问，左孩子和右孩子都在根结点前面被访问。
     */
    // 深度优先遍历-后序遍历--非递归
    public void postorder_traversal(Node root) {
        if (null == root) {
            return;
        }


        Stack<Node> stack = new Stack<>();
        stack.push(root);

        Node cur;                      //当前结点
        Node pre = null;               //前一次访问的结点

        while (!stack.isEmpty()) {
            cur = stack.peek();
            if((cur.mLeft == null && cur.mRight == null) ||
                    (pre != null && (pre == cur.mLeft || pre == cur.mRight))) {
                System.out.printf(cur.mValue + " -> ");  //如果当前结点没有孩子结点或者孩子节点都已被访问过
                stack.pop();
                pre = cur;
            } else {
                if (cur.mRight != null) {
                    stack.push(cur.mRight);
                }

                if (cur.mLeft != null) {
                    stack.push(cur.mLeft);
                }
            }
        }
    }

    // 广度优先遍历
    public void width_first_traversal(Node node) {
        if (null == node) {
            return;
        }
        System.out.println("\r\nMethod 1");

        // 实现方式一，列表
        List<Node> list = new LinkedList<>();
        list.add(node);

        while (!list.isEmpty()) {
            Node curNode = list.get(0);
            System.out.printf(curNode.mValue + " -> ");
            list.remove(0);

            if (null != curNode.mLeft) {
                list.add(curNode.mLeft);
            }

            if (null != curNode.mRight) {
                list.add(curNode.mRight);
            }
        }

        System.out.println("\r\nMethod 2");

        // 实现方式二，队列
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);  // 加入头结点

        while (!queue.isEmpty()) {
            Node curNode = queue.peek();  // 访问第一个
            System.out.printf(curNode.mValue + " -> ");
            ((LinkedList<Node>) queue).pop(); // 移除第一个

            if (null != curNode.mLeft) {  // 左子树入队
                ((LinkedList<Node>) queue).add(curNode.mLeft);
            }

            if (null != curNode.mRight) {  // 右子树入队
                ((LinkedList<Node>) queue).add(curNode.mRight);
            }
        }

    }

    // 左子树之和
    // 右子树之和

}
