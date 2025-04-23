package com.ltalk.service;

import com.ltalk.controller.LTalkController;
import com.ltalk.controller.MainController;
import com.ltalk.controller.SignUpController;
import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomMemberDTO;
import com.ltalk.dto.FriendDTO;
import com.ltalk.dto.MemberDTO;
import com.ltalk.entity.Data;
import com.ltalk.entity.ServerResponse;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.FriendRequest;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.ltalk.controller.SocketController.sendData;

public class DataService {

    public void interpret(ServerResponse serverResponse) throws NoSuchAlgorithmException, IOException {
        switch(serverResponse.getProtocolType()){
            case LOGIN -> login(serverResponse);
            case NEW_CHAT -> newChat(serverResponse);
            case SIGNUP -> signup(serverResponse);
            case READ_CHAT -> readChat(serverResponse);
            case GET_VOICE_SERVER_IP -> joinVoiceServer(serverResponse);
            case RESPONSE_CREATE_CHATROOM_MEMBER -> startingVoiceChat(serverResponse);
        }
    }

    private void startingVoiceChat(ServerResponse serverResponse) {
        new VoiceService().startingVoiceChat();
    }

    private void joinVoiceServer(ServerResponse serverResponse) throws SocketException, UnknownHostException {
        new VoiceService().connectVoiceServer(serverResponse.getVoiceServerIPResponse());
    }

    private void readChat(ServerResponse serverResponse) throws IOException {
        ChatService chatService = new ChatService();
        System.out.println("누군가 채팅 읽음");
        ChatRoomMemberDTO chatRoomMemberDTO = serverResponse.getReadChatResponse().getRoomMember();
        chatService.updateChatRoomMember(chatRoomMemberDTO);
    }



    private void newChat(ServerResponse serverResponse) throws IOException {
        ChatService chatService = new ChatService();
        ChatDTO chatDTO = serverResponse.getNewChatResponse().getDto();
        chatService.newChat(chatDTO);
        System.out.println("newChat 서버로 부터 전송받음");
        System.out.println("getChatRoomId : "+chatDTO.getChatRoomId());
        if(!MainController.getMember().getId().equals(chatDTO.getSenderId())&& chatService.checkOpenRoom(chatDTO.getChatRoomId())){
            //내가 보낸 채팅이 아니고 채팅방이 열려있는 경우 읽었음을 서버로 전송
            System.out.println("채팅방 열려있어서 데이터 전송");
            chatService.readChat(chatDTO.getChatRoomId(),chatDTO.getChatId(), true);
        }

    }

    private void signup(ServerResponse serverResponse) {
        System.out.println(serverResponse.getStatus()+"<<<");
        if (serverResponse.getStatus()) {
            Platform.runLater(() -> {
                SignUpController.singUpStage.close();
            });
        }else{
            System.out.println(serverResponse.getSignupResponse().getMsg());
            //회원가입 실패 사유 알려주기
        }
    }

    private void login(ServerResponse serverResponse) throws NoSuchAlgorithmException, IOException {
        System.out.println(serverResponse.getLoginResponse().getMsg());
        if(serverResponse.getStatus()){
            //메인 화면 보여주기
            MemberDTO member = serverResponse.getLoginResponse().getMember();
            MainController.setMember(member);
            List<FriendDTO> freindList = member.getFriends();
            for (FriendDTO friend : freindList) {
                System.out.println(friend);
            }
            Platform.runLater(() ->{
                LTalkController.getPrimaryStage().close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
                Scene scene = null;
                MainController.setMember(serverResponse.getLoginResponse().getMember());
                MainController.setFriendList(member.getFriends());
                MainController.setChatRoomList(member.getChatRoom());
                Stage stage = new Stage();
                MainController.setStage(stage);
                try {
                    scene = new Scene(fxmlLoader.load(), 360, 590);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("L-Talk");
                stage.setScene(scene);
                StageUtil.setStageUtil(stage);
                stage.show();
            });
            System.out.println("로그인 성공");
        }else{
            //실패 사유 보여주기
            System.out.println("로그인 실패");
        }
    }

    public void requestFriend(){
        Data data = new Data(ProtocolType.REQUEST_FRIEND, new FriendRequest(MainController.getMember().getUsername(), "asd"));
        sendData(data);
    }


}
