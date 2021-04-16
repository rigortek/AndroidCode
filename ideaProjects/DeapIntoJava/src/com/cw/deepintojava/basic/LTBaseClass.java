package com.cw.deepintojava.basic;

public class LTBaseClass {

    public void norStaticMethod() {
        System.out.println("norStaticMethod at " + LTBaseClass.class.getName());
    }

    public static void staticMethod() {
        System.out.println("staticMethod at " + LTBaseClass.class.getName());
    }
}
