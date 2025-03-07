package com.ltalk.handler;

import com.ltalk.controller.SocketController;
import com.ltalk.entity.ServerResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.security.NoSuchAlgorithmException;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel channel;

    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
        receiveResponse();
    }

    private void receiveResponse() {
        ByteBuffer readBuffer = ByteBuffer.allocate(2048);
        channel.read(readBuffer, readBuffer, this);
    }

    @Override
    public void completed(Integer bytesRead, ByteBuffer buffer) {
        if (bytesRead == -1) {
            System.out.println("서버가 연결을 종료했습니다.");
            return;
        }

        buffer.flip();
        String responseJson = new String(buffer.array(), 0, bytesRead);
        System.out.println("서버 응답 JSON: " + responseJson);
        ServerResponse responseData = SocketController.gson.fromJson(responseJson, ServerResponse.class);
        System.out.println("서버 응답 객체: " + responseData);
        try {
            SocketController.getInstance().interpret(responseData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        receiveResponse();
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.err.println("서버 응답 수신 실패: " + exc.getMessage());
    }
}

