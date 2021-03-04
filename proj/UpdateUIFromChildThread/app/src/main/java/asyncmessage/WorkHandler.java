package asyncmessage;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cw.updateuifromchildthread.FullscreenActivity;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Create by robin On 21-3-4
 */

public class WorkHandler extends android.os.Handler {
    private Handler uiHandler;

    public WorkHandler(@NonNull Looper looper, Handler uihandler) {
        super(looper);
        if (looper.equals(Looper.getMainLooper())) {
            throw new InvalidParameterException("Looper is MainLooper");
        }

        this.uiHandler = uihandler;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case UIHandler.MSG_WORKTHREAD_START:
            {
                Log.d(FullscreenActivity.TAG, "handleMessage: work handler excute task start..." + msg.obj);
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(FullscreenActivity.TAG, "handleMessage: work handler excute task done..." + msg.obj);

                Message endMessage = Message.obtain();
                endMessage.what = UIHandler.MSG_WORKTHREAD_DOWE;
                endMessage.obj = "work handler excute task done...";
                uiHandler.sendMessage(endMessage);

                break;
            }

            default:
                break;
        }
    }
}
