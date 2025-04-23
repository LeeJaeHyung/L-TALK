package com.ltalk.service;

import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.JoinVoiceChatRequest;
import com.ltalk.response.VoiceServerIPResponse;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

import static com.ltalk.controller.MainController.member;
import static com.ltalk.controller.SocketController.sendData;

public class VoiceService {

    public static String voiceServerIP = null;
    public static int voiceServerPort = -1;
    public static DatagramSocket receiveSocket;
    public static DatagramSocket sendSocket;
    public static Long voiceChatRoomId;
    public static int receivePort;

    public VoiceService(){
    }


    public void getVoiceServerIP(Long chatRoomId){
        sendData(new Data(ProtocolType.GET_VOICE_SERVER_IP, new JoinVoiceChatRequest(chatRoomId)));
        voiceChatRoomId = chatRoomId;
    }

    public void connectVoiceServer(VoiceServerIPResponse voiceServerIPResponse) throws SocketException, UnknownHostException {
        voiceServerIP = voiceServerIPResponse.getIp();
        voiceServerPort = voiceServerIPResponse.getPort();
        System.out.println(voiceServerIPResponse.getPort());
        System.out.println("voiceServerIP : "+voiceServerIP+" voiceServerPort :"+voiceServerPort);
        //voiceServer IP, Port 셋팅
        receiveSocket = new DatagramSocket(0);// OS가 가용한 포트 자동 선택
        receivePort = receiveSocket.getLocalPort();
        System.out.println(receivePort);
        sendSocket = new DatagramSocket(0);
        // 2. 자신의 IP 확인
        String localIp = InetAddress.getLocalHost().getHostAddress();
        // 내가 열은 포트의 정보데이터 전송
        sendData(new Data(ProtocolType.JOIN_VOICE_CHAT, new JoinVoiceChatRequest(voiceServerIPResponse.getChatRoomId(),member.getId(), member.getUsername(), localIp, receivePort)));

    }

    public void startingVoiceChat(){
        new Thread(() -> {
            try {
                send(voiceServerIP, voiceServerPort);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(()->{
            try {
                receive();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void send(String voiceServerIP, int voiceServerPort) throws IOException, LineUnavailableException {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
        mic.open(format);
        mic.start();

        byte[] audioBuffer = new byte[320]; // 320 byte = 20ms (8000Hz, 16bit, mono)
        ByteBuffer sendBuffer = ByteBuffer.allocate(336); // 8 + 8 + 320

        System.out.println("전송 시작");
        while (true) {
            System.out.println("전송중 -> IP :"+voiceServerIP+" port :"+voiceServerPort);
            int count = mic.read(audioBuffer, 0, audioBuffer.length);
            if (count > 0) {
                sendBuffer.clear();
                sendBuffer.putLong(voiceChatRoomId);
                sendBuffer.putLong(member.getId());
                sendBuffer.put(audioBuffer, 0, count);
                InetAddress address = InetAddress.getByName(voiceServerIP);
                DatagramPacket packet = new DatagramPacket(sendBuffer.array(), sendBuffer.position(), address, voiceServerPort);
                sendSocket.send(packet);
            }
        }
    }

    public static void receive() throws IOException, LineUnavailableException {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(info);
        speaker.open(format);
        speaker.start();

        byte[] buffer = new byte[336]; // 8 + 8 + 320
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        System.out.println("수신 대기 중...");
        while (true) {
            System.out.println("수신중");
            receiveSocket.receive(packet);
            ByteBuffer receiveBuffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());

            long receivedChatRoomId = receiveBuffer.getLong();
            long receivedMemberId = receiveBuffer.getLong();
            byte[] audioData = new byte[receiveBuffer.remaining()];
            receiveBuffer.get(audioData);

            // 로그 출력 (누가 보냈는지)
            System.out.println("받음: chatRoomId=" + receivedChatRoomId + ", memberId=" + receivedMemberId);

            speaker.write(audioData, 0, audioData.length);
        }
    }

}
