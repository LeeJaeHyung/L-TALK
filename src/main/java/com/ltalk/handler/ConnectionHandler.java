package com.ltalk.handler;

import com.ltalk.controller.SocketController;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ConnectionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private final AsynchronousSocketChannel channel;

    public ConnectionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel channel) {
        System.out.println("서버에 연결됨!");
        SocketController.isConnected = true;
        new ReadHandler(channel); // 서버 응답 수신 시작

    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
        System.err.println("서버 연결 실패: " + exc.getMessage());
    }
}

