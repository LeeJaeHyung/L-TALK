package com.ltalk.service;

import com.ltalk.controller.ChatController;
import com.ltalk.controller.MainController;
import com.ltalk.controller.SocketController;
import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.dto.ChatRoomMemberDTO;
import com.ltalk.entity.Data;
import com.ltalk.enums.ChatRoomType;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.ChatRoomCreatRequest;
import com.ltalk.request.ReadChatRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ltalk.controller.MainController.chatControllerMap;
import static com.ltalk.controller.MainController.member;
import static com.ltalk.controller.SocketController.getInstance;
import static com.ltalk.controller.SocketController.sendData;

public class ChatService {
    SocketController channel = getInstance();
    ChatRoomDTO chatRoomDTO;
    public ChatService() throws IOException {
    }



//    public void getChatList() throws IOException {
//        SocketController.sendData(new Data(ProtocolType.CHAT_LIST, member.getUsername()));
//    }

    public void creatRoom() {
        ChatRoomType roomType = ChatRoomType.PRIVATE;
        String chatName = "테스트용";
        List<String> chatRoomMembers = new ArrayList<>();
        chatRoomMembers.add("asd");
        chatRoomMembers.add(MainController.getMember().getUsername());
        ChatRoomCreatRequest chatRoomCreatRequest = new ChatRoomCreatRequest(chatName, roomType ,chatRoomMembers);
        sendData(new Data(ProtocolType.CREATE_CHATROOM, chatRoomCreatRequest));
    }

    public void addChat(ChatRoomDTO chatRoomdto, String message) {
        ChatDTO chatDTO = new ChatDTO(message);
        chatRoomdto.getChats().add(chatDTO);
    }

    public void newChat(ChatDTO chatDTO) throws IOException {
        ChatController chatController = chatControllerMap.get(chatDTO.getChatRoomId());
        chatController.newChat(chatDTO);
    }

    public void newChat(Long chatRoomId){
        ChatController chatController = chatControllerMap.get(chatRoomId);
        chatController.newChat();
    }

    public void sortChatRoomMember(ChatRoomDTO chatRoomDTO){
        chatRoomDTO.getMembers().sort(Comparator.comparing(ChatRoomMemberDTO::getReadChatId).reversed());
    }

    public void readCount(ChatRoomDTO chatRoom) {
        int participantCount = chatRoom.getParticipantCount();
        Long[] readChatIdArr = new Long[participantCount];
        List<ChatRoomMemberDTO> chatRoomMember = chatRoom.getMembers();
        for(int i = 0; i < participantCount; i++){
            readChatIdArr[i] = chatRoomMember.get(i).getReadChatId();
        }
        List<ChatDTO> chats = chatRoom.getChats();
        int idx = 0;
        for(ChatDTO chatDTO : chats){
            System.out.println(chatDTO.getChatId());
            for(int i = idx; i < readChatIdArr.length; i++){
                System.out.println(idx+" : "+i+" : "+readChatIdArr[i]+" : "+chatDTO.getChatId());
                if(chatDTO.getChatId()<=readChatIdArr[i]){
                    chatDTO.setUnreadCount(idx);
                    break;
                }else{
                    idx++;
                }
            }
        }
    }

    public boolean checkOpenRoom(Long chatRoomId) throws IOException {
        ChatController chatController = chatControllerMap.get(chatRoomId);
        return chatController.getIsOpen();
    }

    public void readChat(Long chatRoomId, Long chatId) {
        sendData(new Data(ProtocolType.READ_CHAT, new ReadChatRequest(chatRoomId, chatId, member.getId())));
    }
}
