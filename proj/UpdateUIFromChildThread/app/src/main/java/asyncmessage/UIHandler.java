package asyncmessage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.InvalidParameterException;

/**
 * Create by robin On 21-3-4
 */
public class UIHandler extends Handler {
    public static final int MSG_WORKTHREAD_DOWE = 1000;
    public static final int MSG_WORKTHREAD_START = 1001;

    private Context context;

    public UIHandler(Context context, @NonNull Looper looper) {
        super(looper);

        if (!looper.equals(Looper.getMainLooper())) {
            throw new InvalidParameterException("Looper is not MainLooper");
        }

        this.context = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_WORKTHREAD_DOWE:
                Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        super.handleMessage(msg);
    }

}
