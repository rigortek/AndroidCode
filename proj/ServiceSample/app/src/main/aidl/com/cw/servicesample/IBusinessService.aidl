// IBusinessService.aidl
package com.cw.servicesample;

// Declare any non-default types here with import statements

oneway interface IBusinessService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    oneway void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}