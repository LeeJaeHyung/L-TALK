package com.ltalk.handler;

import com.ltalk.controller.SocketController;
import com.ltalk.entity.ServerResponse;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel channel;
    private ByteBuffer lengthBuffer = ByteBuffer.allocate(4); //  4바이트 길이 버퍼
    private ByteBuffer dataBuffer;
    private boolean readingLength = true;

    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
        receiveResponse();
    }

    private void receiveResponse() {
        if (readingLength) {
            lengthBuffer.clear();
            channel.read(lengthBuffer, lengthBuffer, this);
        } else {
            dataBuffer.clear();
            channel.read(dataBuffer, dataBuffer, this);
        }
    }

    @Override
    public void completed(Integer bytesRead, ByteBuffer buffer) {
        if (bytesRead == -1) {
            System.out.println("서버가 연결을 종료했습니다.");
            return;
        }

        if (readingLength) {
            buffer.flip();
            if (buffer.remaining() >= 4) {
                int messageLength = buffer.getInt();
                System.out.println("수신할 데이터 크기: " + messageLength);

                dataBuffer = ByteBuffer.allocate(messageLength);
                readingLength = false;

                channel.read(dataBuffer, dataBuffer, this);  // 처음 본문 읽기
            } else {
                channel.read(lengthBuffer, lengthBuffer, this); // 부족 시 다시 읽기
            }
        } else {
            if (dataBuffer.hasRemaining()) {
                channel.read(dataBuffer, dataBuffer, this); // 아직 다 못 받았으면 계속
            } else {
                dataBuffer.flip();
                byte[] receivedData = new byte[dataBuffer.remaining()];
                dataBuffer.get(receivedData);
                String responseJson = new String(receivedData);
                System.out.println("서버 응답 JSON: " + responseJson);

                try {
                    ServerResponse responseData = SocketController.gson.fromJson(responseJson, ServerResponse.class);
                    System.out.println("서버 응답 객체: " + responseData);
                    SocketController.getInstance().interpret(responseData);
                } catch (Exception e) {
                    System.err.println("❌ JSON 파싱 실패:");
                    e.printStackTrace();
                }

                readingLength = true;
                lengthBuffer.clear();
                channel.read(lengthBuffer, lengthBuffer, this); // 다음 메시지 준비
            }
        }
    }


    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.err.println("서버 응답 수신 실패: " + exc.getMessage());
    }
}
