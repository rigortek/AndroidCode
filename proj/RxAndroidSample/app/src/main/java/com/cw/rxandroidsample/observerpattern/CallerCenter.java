package com.cw.rxandroidsample.observerpattern;

/**
 * Create by robin On 20-11-27
 * Tester class
 */
public class CallerCenter {
    public static void test() {
        // 被观察者
        ConcreteObserverable concreteObserverable = new ConcreteObserverable();

        // 多个观察者
        ConcreteObserver concreteObserver1 = new ConcreteObserver("first");
        ConcreteObserver concreteObserver2 = new ConcreteObserver("second");

         // 将观察者注册到被观察者中
        concreteObserverable.registerObserver(concreteObserver1);
        concreteObserverable.registerObserver(concreteObserver1);
        concreteObserverable.registerObserver(concreteObserver2);

        // 发送更新能通知.
        concreteObserverable.notifyObservers("dummy");
        concreteObserverable.notifyObservers("dummy2");
        concreteObserverable.notifyObservers("dummy3");
        concreteObserverable.notifyObservers("dummy4");
    }
}
