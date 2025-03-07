package com.ltalk.service;

import com.ltalk.controller.LTalkController;
import com.ltalk.controller.MainController;
import com.ltalk.controller.SignUpController;
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
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.ltalk.controller.SocketController.sendData;

public class DataService {

    public void interpret(ServerResponse serverResponse) throws NoSuchAlgorithmException, IOException {
        switch(serverResponse.getProtocolType()){
            case LOGIN -> login(serverResponse);
//            case CHAT -> chat();
            case SIGNUP -> signup(serverResponse);
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
