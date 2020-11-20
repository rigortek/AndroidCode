package com.cw.sqlite;

import java.util.concurrent.TimeUnit;

public class SQLiteDatabase {

    public static SQLiteDatabase openDatabase() {
        return new SQLiteDatabase();
    }

    public void testFun(int i) {
        System.out.println(Thread.currentThread().getName() + " : " + i + " entry");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " : " + i + " leave");
    }
}
