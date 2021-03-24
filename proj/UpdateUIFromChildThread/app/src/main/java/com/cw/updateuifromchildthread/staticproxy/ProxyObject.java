package com.cw.updateuifromchildthread.staticproxy;

/**
 * Create by robin On 21-3-23
 */
public class ProxyObject extends AbstrctObject {

    private RealObject realObject;

    public ProxyObject(RealObject object) {
        realObject = object;
    }

    @Override
    protected void operation() {
        if (null == realObject) {
            realObject = new RealObject();
        }

        realObject.operation();
    }
}
