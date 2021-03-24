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
public class NIOServer {
    public static final String TAG = "jcw";

    private Selector mSelector;

    public void initClient(int serverPort) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        mSelector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
        serverSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws IOException {
        Log.d(TAG, "listen: server started");

        while (true) {
            mSelector.select();
            Iterator iterator = mSelector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    serverSocketChannel.configureBlocking(false);

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.write(ByteBuffer.wrap("msg to client".getBytes()));
                    socketChannel.register(mSelector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    readResponse(selectionKey);
                }
            }
        }
    }

    public void readResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(50);
        channel.read(byteBuffer);

        String msg = new String(byteBuffer.array()).trim();
        Log.d(TAG, "read: " + msg + " from client");

        channel.write(ByteBuffer.wrap((msg + " plus response from server").getBytes()));
    }

    private void write() throws IOException {

    }

}
