package com.cw.productconsume2;

public class ConsumeModule {
    public static void action(CmdRequest request) {
        System.out.println(">>>>>>>> exec action start: " + request.getCommandName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("<<<<<<<< exec action end: " + request.getCommandName() + "\n");

    }
}
