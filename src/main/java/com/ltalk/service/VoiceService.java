package com.ltalk.service;

import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.JoinVoiceChatRequest;
import com.ltalk.response.VoiceServerIPResponse;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;

import static com.ltalk.controller.MainController.*;
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

    public void connectVoiceServer(VoiceServerIPResponse voiceServerIPResponse) throws IOException {
        voiceServerIP = voiceServerIPResponse.getIp();
        voiceServerPort = voiceServerIPResponse.getPort();
        System.out.println(voiceServerIPResponse.getPort());
        System.out.println("voiceServerIP : "+voiceServerIP+" voiceServerPort :"+voiceServerPort);
        //voiceServer IP, Port 셋팅
        receiveSocket = new DatagramSocket(0);// OS가 가용한 포트 자동 선택
        System.out.println(receivePort);
        sendSocket = new DatagramSocket(0);
        // 2. 자신의 IP 확인
        URL url = new URL("https://checkip.amazonaws.com/");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String publicIP = in.readLine();
//        String publicIP = "localhost";
        // 내가 열은 포트의 정보데이터 전송

        //1. 먼저 receiveSocket 을 통해서 채팅방 정보 등록 요청
        //2. 충분한 크기에 ByteBuffer 생성
        ByteBuffer sendBuffer = ByteBuffer.allocate(20); // 4+8+8
        sendBuffer.clear();
        sendBuffer.putInt(1);
        sendBuffer.putLong(voiceChatRoomId);
        sendBuffer.putLong(member.getId());
        sendBuffer.flip(); // position → 0, limit → 현재 위치

        byte[] sendBytes = new byte[sendBuffer.remaining()];
        sendBuffer.get(sendBytes);
        DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, InetAddress.getByName(voiceServerIP), voiceServerPort);
        receiveSocket.send(sendPacket);// 받을 소켓의 udp 통신을 위해서 데이터 먼저 전송

        System.out.println("핸드 쉐이크 데이터 수신중");
        byte[] recvBuf = new byte[4];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        receiveSocket.receive(recvPacket);
        ByteBuffer receiveBuffer = ByteBuffer.wrap(recvPacket.getData(), 0, recvPacket.getLength());

        int receiveStatus = receiveBuffer.getInt();

        if(receiveStatus == 1){
            System.out.println("핸드 쉐이크 성공");
            startingVoiceChat();
        }

    }

    public void startingVoiceChat(){
        voiceSendThread = new Thread(() -> {
            try {
                send(voiceServerIP, voiceServerPort);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        voiceRecieveThread = new Thread(()->{
            try {
                receive();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        voiceSendThread.start();
        voiceRecieveThread.start();
    }

    public static void send(String voiceServerIP, int voiceServerPort) throws IOException, LineUnavailableException {
        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);

        // ✅ 기본 입력 장치 (주 사운드 캡처 드라이버) 선택
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Mixer inputMixer = null;
        for (Mixer.Info mixerInfo : mixers) {
            if (mixerInfo.getName().contains("주 사운드 캡처 드라이버")) {
                inputMixer = AudioSystem.getMixer(mixerInfo);
                break;
            }
        }
        if (inputMixer == null) {
            throw new LineUnavailableException("기본 입력 장치(주 사운드 캡처 드라이버)를 찾을 수 없습니다.");
        }

        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine mic = (TargetDataLine) inputMixer.getLine(targetInfo);
        mic.open(format);
        mic.start();

        byte[] audioBuffer = new byte[640]; // 320 byte = 20ms (8000Hz, 16bit, mono)
        ByteBuffer sendBuffer = ByteBuffer.allocate(660); // 8 + 8 + 320

        System.out.println("전송 시작");
        while (true) {
            System.out.println("전송중 -> IP :"+voiceServerIP+" port :"+voiceServerPort);
            int count = mic.read(audioBuffer, 0, audioBuffer.length);
            if (count > 0) {
                sendBuffer.clear();
                sendBuffer.putInt(0);
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
        // 둘 다 같은 format 써야 함
        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(info);
        speaker.open(format);
        speaker.start();

        byte[] buffer = new byte[660]; // 8 + 8 + 320
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        System.out.println("수신 대기 중...");
        while (true) {
            System.out.println("수신중");
            receiveSocket.receive(packet);
            ByteBuffer receiveBuffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
            receiveBuffer.getInt();//type 데이터 날려버림
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
