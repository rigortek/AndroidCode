package com.cw.deepintojava.basic;

public class LTChildClass extends LTBaseClass {
    @Override
    // 重写
    public void norStaticMethod() {
        System.out.println("norStaticMethod at " + LTChildClass.class.getName());
    }

    // 子类无法重写父类static方法
    public static void staticMethod() {
        System.out.println("staticMethod at " + LTChildClass.class.getName());
    }

    public static void main(String[] args) {
        LTBaseClass ltBaseClass = new LTBaseClass();

        ltBaseClass.norStaticMethod();
        ltBaseClass.staticMethod();

        /////////////////////////////////////////////////
        LTBaseClass ltChildClass = new LTChildClass();

        ltChildClass.norStaticMethod();
        ltChildClass.staticMethod();

        LTChildClass.staticMethod();
    }
}
