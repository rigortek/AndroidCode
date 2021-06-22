package com.cw;

import com.cw.agora.BTreeSample;
import com.cw.agora.Node;

public class Main {

    public static void main(String[] args) {
	// write your code here


        Money income = new Money(55, "USD");
        Money expenses = new Money(55, "USD");
        System.out.println(income + " equals " + expenses + " ? " + income.equals(expenses));  // false

        testBtree();
    }

    private static void testBtree() {
        BTreeSample bTreeSample = new BTreeSample();
        Node tree = bTreeSample.createBtree();

        System.out.println("前序遍历递归");
        bTreeSample.preorder_traversal_recursive(tree);
        System.out.println("");

        System.out.println("前序遍历非递归");
        bTreeSample.preorder_traversal(tree);
        System.out.println("\r\n----------------------------------------");

        System.out.println("中序遍历递归");
        bTreeSample.inorder_traversal_recursive(tree);
        System.out.println("");

        System.out.println("中序遍历非递归");
        bTreeSample.inorder_traversal(tree);
        System.out.println("\r\n----------------------------------------");

        System.out.println("后序遍历递归");
        bTreeSample.postorder_traversal_recursive(tree);
        System.out.println("");

        System.out.println("后序遍历非递归");
        bTreeSample.postorder_traversal(tree);
        System.out.println("\r\n----------------------------------------");


        System.out.println("广度优先遍历");
        bTreeSample.width_first_traversal(tree);
        System.out.println("");
    }
}
