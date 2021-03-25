package com.cw.updateuifromchildthread.io;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Create by robin On 21-3-24
 */
public class NIOClient extends Thread {
    public static final String TAG = "jcw";

    private SocketChannel mSocketChannel;
    private Selector mSelector;
    private String serverIP;
    private int serverPort;
    private boolean bInited;

    private void initClient(String serverIP, int serverPort) throws IOException {
        if (!bInited) {
            mSocketChannel = SocketChannel.open();

            mSocketChannel.configureBlocking(false);

            mSelector = Selector.open();

            mSocketChannel.connect(new InetSocketAddress(serverIP, serverPort));
            mSocketChannel.register(mSelector, SelectionKey.OP_CONNECT);
            bInited = true;
        }
    }

    public NIOClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void sendMsg(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocketChannel.write(ByteBuffer.wrap((TextUtils.isEmpty(msg) ? "new msg to server" : (msg)).getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            initClient(serverIP, serverPort);
            listen();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finish() throws IOException {
        bInited = false;
        mSocketChannel.close();
        mSelector.close();
    }

    public void listen() throws IOException {
        Log.d(TAG, "listen: client started");
        String response = null;

        while (true) {
            mSelector.select();
            Iterator iterator = mSelector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();

                if (selectionKey.isConnectable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    if (socketChannel.isConnectionPending()) {
                        socketChannel.finishConnect();
                    }

                    socketChannel.configureBlocking(false);

                    socketChannel.write(ByteBuffer.wrap("msg to server".getBytes()));
                    socketChannel.register(mSelector, SelectionKey.OP_READ);
                }

                if (selectionKey.isReadable()) {
                    response = read(selectionKey);
                }
            }

            if ("QUIT".equals(response)) {
                break;
            }
        }

        Log.d(TAG, "listen: client end");
    }

    public String read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        channel.read(byteBuffer);

        String msg = new String(byteBuffer.array()).trim();
        Log.d(TAG, "read: " + msg);

        return msg;
    }

  }
