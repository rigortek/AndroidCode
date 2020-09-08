package com.cw;

public class Main {

    public static void main(String[] args) {
	// write your code here
        MyReadWriteData myReadWriteData = new MyReadWriteData();

        for (int i = 0; i < 5; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    myReadWriteData.setCache2("" + index, index);
                }
            });
            thread.setName("Thread_" + i);
            thread.start();
        }

        for (int i = 0; i < 5; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    myReadWriteData.getCache2("" + index);
                }
            });
            thread.setName("Thread_" + i);
            thread.start();
        }
    }
}
