package com.cw.updateuifromchildthread.io;

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

    private Selector mSelector;

    public void initClient(String serverIP, int serverPort) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        mSelector = Selector.open();

        socketChannel.connect(new InetSocketAddress(serverIP, serverPort));
        socketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        Log.d(TAG, "listen: client started");

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
                } else if (selectionKey.isReadable()) {
                    read(selectionKey);
                }
            }
        }
    }

    public void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(50);
        channel.read(byteBuffer);

        String msg = new String(byteBuffer.array()).trim();
        Log.d(TAG, "read: " + msg + " from server");
    }
}
