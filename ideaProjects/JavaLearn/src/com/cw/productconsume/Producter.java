package com.cw.productconsume;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Producter {
    List<String> messageQueue = Collections.synchronizedList(new LinkedList<>());
    int index;

    public void startWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    System.out.println("Producter generate message " + (index + 1));
                    messageQueue.add(String.valueOf(++index));
                    try {
                        TimeUnit.MILLISECONDS.sleep(Dispatcher.getRandom(2, 6));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index >= 50) {
                        System.out.println("Productor abort generate message for finish work");
                        break;
                    }
                } while (true);
            }
        }).start();
    }

    public String fetchFirstMessage() {
        return (messageQueue.isEmpty()) ?  null : messageQueue.get(0);
    }

    public void removeMessage(String key) {
        messageQueue.remove(key);
    }
}