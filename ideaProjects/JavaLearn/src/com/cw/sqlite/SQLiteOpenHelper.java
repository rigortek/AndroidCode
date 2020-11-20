package com.cw.sqlite;

public class SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;

    public SQLiteDatabase getWritableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(true);
        }
    }

    public SQLiteDatabase getReadableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(false);
        }
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (mDatabase != null) {
            return mDatabase;
        }


        mDatabase = SQLiteDatabase.openDatabase();
        return mDatabase;
    }

    public static void main(String[] args) {
        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper();
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

        Thread firstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    sqLiteDatabase.testFun(i);
                }
            }
        });
        firstThread.setName("first thread");
        firstThread.start();

        // second thread
        Thread secondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    sqLiteDatabase.testFun(i);
                }
            }
        });
        secondThread.setName("second thread");
        secondThread.start();
    }
}
