package asyncmessage;

import android.os.HandlerThread;

/**
 * Create by robin On 21-3-4
 */
public class HandlerThreadTest extends HandlerThread {
    public static final String TAG = "jcw";


    public HandlerThreadTest(String name) {
        super(name);
    }

    public HandlerThreadTest(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
    }
}
