package com.cw.thirdsystemapp;


import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InputHelper {
    private static final String TAG = "Input";
    private static final String INVALID_ARGUMENTS = "Error: Invalid arguments for command: ";
    private InputManager mInputManager;
    private Method inject;

    private static final Map<String, Integer> SOURCES = new HashMap<String, Integer>() {{
        put("keyboard", InputDevice.SOURCE_KEYBOARD);
        put("dpad", InputDevice.SOURCE_DPAD);
        put("gamepad", InputDevice.SOURCE_GAMEPAD);
        put("touchscreen", InputDevice.SOURCE_TOUCHSCREEN);
        put("mouse", InputDevice.SOURCE_MOUSE);
        put("stylus", InputDevice.SOURCE_STYLUS);
        put("trackball", InputDevice.SOURCE_TRACKBALL);
        put("touchpad", InputDevice.SOURCE_TOUCHPAD);
        put("touchnavigation", InputDevice.SOURCE_TOUCH_NAVIGATION);
        put("joystick", InputDevice.SOURCE_JOYSTICK);
    }};

    public InputHelper() {
        try {
            Class inputManager = Class.forName("android.hardware.input.InputManager");
            Method getInstance = inputManager.getDeclaredMethod("getInstance");
            mInputManager = (InputManager) getInstance.invoke(null);
            inject = inputManager.getDeclaredMethod("injectInputEvent", InputEvent.class, Integer.TYPE);
            inject.setAccessible(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void test(String[] args) {
        if (args.length < 1) {
            showUsage();
            return;
        }

        int index = 0;
        String command = args[index];
        int inputSource = InputDevice.SOURCE_UNKNOWN;
        if (SOURCES.containsKey(command)) {
            inputSource = SOURCES.get(command);
            index++;
            command = args[index];
        }
        final int length = args.length - index;

        try {
            if (command.equals("text")) {
                if (length == 2) {
                    inputSource = getSource(inputSource, InputDevice.SOURCE_KEYBOARD);
                    sendText(inputSource, args[index+1]);
                    return;
                }
            } else if (command.equals("keyevent")) {
                if (length >= 2) {
                    final boolean longpress = "--longpress".equals(args[index + 1]);
                    final int start = longpress ? index + 2 : index + 1;
                    inputSource = getSource(inputSource, InputDevice.SOURCE_KEYBOARD);
                    if (args.length > start) {
                        for (int i = start; i < args.length; i++) {
                            int keyCode = KeyEvent.keyCodeFromString(args[i]);
                            if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                                keyCode = KeyEvent.keyCodeFromString("KEYCODE_" + args[i]);
                            }
                            sendKeyEvent(inputSource, keyCode, longpress);
                        }
                        return;
                    }
                }
            } else if (command.equals("tap")) {
                if (length == 3) {
                    inputSource = getSource(inputSource, InputDevice.SOURCE_TOUCHSCREEN);
                    sendTap(inputSource, Float.parseFloat(args[index+1]),
                            Float.parseFloat(args[index+2]));
                    return;
                }
            } else if (command.equals("swipe")) {
                int duration = -1;
                inputSource = getSource(inputSource, InputDevice.SOURCE_TOUCHSCREEN);
                switch (length) {
                    case 6:
                        duration = Integer.parseInt(args[index+5]);
                    case 5:
                        sendSwipe(inputSource,
                                Float.parseFloat(args[index+1]), Float.parseFloat(args[index+2]),
                                Float.parseFloat(args[index+3]), Float.parseFloat(args[index+4]),
                                duration);
                        return;
                }
            } else if (command.equals("draganddrop")) {
                int duration = -1;
                inputSource = getSource(inputSource, InputDevice.SOURCE_TOUCHSCREEN);
                switch (length) {
                    case 6:
                        duration = Integer.parseInt(args[index+5]);
                    case 5:
                        sendDragAndDrop(inputSource,
                                Float.parseFloat(args[index+1]), Float.parseFloat(args[index+2]),
                                Float.parseFloat(args[index+3]), Float.parseFloat(args[index+4]),
                                duration);
                        return;
                }
            } else if (command.equals("press")) {
                inputSource = getSource(inputSource, InputDevice.SOURCE_TRACKBALL);
                if (length == 1) {
                    sendTap(inputSource, 0.0f, 0.0f);
                    return;
                }
            } else if (command.equals("roll")) {
                inputSource = getSource(inputSource, InputDevice.SOURCE_TRACKBALL);
                if (length == 3) {
                    sendMove(inputSource, Float.parseFloat(args[index+1]),
                            Float.parseFloat(args[index+2]));
                    return;
                }
            } else {
                System.err.println("Error: Unknown command: " + command);
                showUsage();
                return;
            }
        } catch (NumberFormatException ex) {
        }
        System.err.println(INVALID_ARGUMENTS + command);
        showUsage();
    }

    /**
     * Convert the characters of string text into key event's and send to
     * device.
     *
     * @param text is a string of characters you want to input to the device.
     */
    public void sendText(int source, String text) {

        StringBuffer buff = new StringBuffer(text);

        boolean escapeFlag = false;
        for (int i=0; i<buff.length(); i++) {
            if (escapeFlag) {
                escapeFlag = false;
                if (buff.charAt(i) == 's') {
                    buff.setCharAt(i, ' ');
                    buff.deleteCharAt(--i);
                }
            }
            if (buff.charAt(i) == '%') {
                escapeFlag = true;
            }
        }

        char[] chars = buff.toString().toCharArray();

        KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
        KeyEvent[] events = kcm.getEvents(chars);
        for(int i = 0; i < events.length; i++) {
            KeyEvent e = events[i];
            if (source != e.getSource()) {
                e.setSource(source);
            }
            injectKeyEvent(e);
        }
    }

    private void sendKeyEvent(int inputSource, int keyCode, boolean longpress) {
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, inputSource));
        if (longpress) {
            injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 1, 0,
                    KeyCharacterMap.VIRTUAL_KEYBOARD, 0, KeyEvent.FLAG_LONG_PRESS,
                    inputSource));
        }
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, inputSource));
    }

    public void sendTap(int inputSource, float x, float y) {
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x, y, 1.0f);
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
    }

    private void sendSwipe(int inputSource, float x1, float y1, float x2, float y2, int duration) {
        if (duration < 0) {
            duration = 300;
        }
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        long startTime = now;
        long endTime = startTime + duration;
        while (now < endTime) {
            long elapsedTime = now - startTime;
            float alpha = (float) elapsedTime / duration;
            injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
            now = SystemClock.uptimeMillis();
        }
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x2, y2, 0.0f);
    }

    private void sendDragAndDrop(int inputSource, float x1, float y1, float x2, float y2,
                                 int dragDuration) {
        if (dragDuration < 0) {
            dragDuration = 300;
        }
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        try {
            Thread.sleep(ViewConfiguration.getLongPressTimeout());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        now = SystemClock.uptimeMillis();
        long startTime = now;
        long endTime = startTime + dragDuration;
        while (now < endTime) {
            long elapsedTime = now - startTime;
            float alpha = (float) elapsedTime / dragDuration;
            injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
            now = SystemClock.uptimeMillis();
        }
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x2, y2, 0.0f);
    }

    /**
     * Sends a simple zero-pressure move event.
     *
     * @param inputSource the InputDevice.SOURCE_* sending the input event
     * @param dx change in x coordinate due to move
     * @param dy change in y coordinate due to move
     */
    private void sendMove(int inputSource, float dx, float dy) {
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, dx, dy, 0.0f);
    }

    public void injectKeyEvent(KeyEvent event) {
        Log.i(TAG, "injectKeyEvent: " + event);
        try {
            inject.invoke(mInputManager, event, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getInputDeviceId(int inputSource) {
        final int DEFAULT_DEVICE_ID = 0;
        int[] devIds = InputDevice.getDeviceIds();
        for (int devId : devIds) {
            InputDevice inputDev = InputDevice.getDevice(devId);
            if (inputDev.supportsSource(inputSource)) {
                return devId;
            }
        }
        return DEFAULT_DEVICE_ID;
    }

    /**
     * Builds a MotionEvent and injects it into the event stream.
     *
     * @param inputSource the InputDevice.SOURCE_* sending the input event
     * @param action the MotionEvent.ACTION_* for the event
     * @param when the value of SystemClock.uptimeMillis() at which the event happened
     * @param x x coordinate of event
     * @param y y coordinate of event
     * @param pressure pressure of event
     */
    private void injectMotionEvent(int inputSource, int action, long when, float x, float y, float pressure) {
        final float DEFAULT_SIZE = 1.0f;
        final int DEFAULT_META_STATE = 0;
        final float DEFAULT_PRECISION_X = 1.0f;
        final float DEFAULT_PRECISION_Y = 1.0f;
        final int DEFAULT_EDGE_FLAGS = 0;
        MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, DEFAULT_SIZE,
                DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y,
                getInputDeviceId(inputSource), DEFAULT_EDGE_FLAGS);
        event.setSource(inputSource);
        Log.i(TAG, "injectMotionEvent: " + event);
        try {
            inject.invoke(mInputManager, event, 2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static final float lerp(float a, float b, float alpha) {
        return (b - a) * alpha + a;
    }

    private static final int getSource(int inputSource, int defaultSource) {
        return inputSource == InputDevice.SOURCE_UNKNOWN ? defaultSource : inputSource;
    }

    private void showUsage() {
        System.err.println("Usage: input [<source>] <command> [<arg>...]");
        System.err.println();
        System.err.println("The sources are: ");
        for (String src : SOURCES.keySet()) {
            System.err.println("      " + src);
        }
        System.err.println();
        System.err.println("The commands and default sources are:");
        System.err.println("      text <string> (Default: touchscreen)");
        System.err.println("      keyevent [--longpress] <key code number or name> ..."
                + " (Default: keyboard)");
        System.err.println("      tap <x> <y> (Default: touchscreen)");
        System.err.println("      swipe <x1> <y1> <x2> <y2> [duration(ms)]"
                + " (Default: touchscreen)");
        System.err.println("      draganddrop <x1> <y1> <x2> <y2> [duration(ms)]"
                + " (Default: touchscreen)");
        System.err.println("      press (Default: trackball)");
        System.err.println("      roll <dx> <dy> (Default: trackball)");
    }
}