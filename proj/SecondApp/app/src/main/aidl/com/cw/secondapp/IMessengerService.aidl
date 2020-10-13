// IMessengerService.aidl
package com.cw.secondapp;

// Declare any non-default types here with import statements

interface IMessengerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String basicTypes(String aString);

    void transferBitMap(in Bitmap bitmap);
    void transferBitMapByBundle(in Bundle bundle);

    void transferRawData(in byte[] raw);
}
