package com.cw.updateuifromchildthread.io;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Create by robin On 21-3-24
 */
public class NIOServer extends Thread {
    public static final String TAG = "jcw";

    private ServerSocketChannel mServerSocketChannel;
    private Selector mSelector;
    private int mPort;
    private boolean bInited;

    public NIOServer(int port) {
        mPort = port;
    }

    public void initServer(int serverPort) throws IOException {
        if (!bInited) {
            mServerSocketChannel = ServerSocketChannel.open();
            mServerSocketChannel.configureBlocking(false);

            mSelector = Selector.open();
            mServerSocketChannel.socket().bind(new InetSocketAddress(serverPort));
            mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
            bInited = true;
        }
    }

    @Override
    public void run() {
        try {
            initServer(mPort);
            listen();
            finish();
        } catch (IOException e) {

        }
    }

    private void finish() throws IOException {
        bInited = false;
        mServerSocketChannel.close();
        mSelector.close();
    }

    public void listen() throws IOException {
        Log.d(TAG, "listen: server started");

        String response = null;
        while (true) {
            mSelector.select();
            Iterator iterator = mSelector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
//                    socketChannel.write(ByteBuffer.wrap("msg to client".getBytes()));
                    socketChannel.register(mSelector, SelectionKey.OP_READ);
                }

                if (selectionKey.isReadable()) {
                    response = readResponse(selectionKey);
                }
            }

            if ("QUIT".equals(response)) {
                break;
            }
        }

        Log.d(TAG, "listen: server end");
    }

    public String readResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        channel.read(byteBuffer);

        String msg = new String(byteBuffer.array()).trim();
        Log.d(TAG, "read: " + msg + " from client");

        if ("QUIT".equals(msg)) {
            channel.write(ByteBuffer.wrap(("QUIT").getBytes()));
        } else {
            channel.write(ByteBuffer.wrap((msg + " plus response from server").getBytes()));
        }

        return msg;
    }

}
