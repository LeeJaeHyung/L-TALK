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
import java.util.Iterator;
import java.util.List;

import static com.ltalk.controller.MainController.chatControllerMap;
import static com.ltalk.controller.MainController.member;
import static com.ltalk.controller.SocketController.getInstance;
import static com.ltalk.controller.SocketController.sendData;

public class ChatService {
    SocketController channel = getInstance();
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


    public void newChat(ChatDTO chatDTO) throws IOException { // 서버로 부터 새로운 채팅을 받았음
        ChatController chatController = chatControllerMap.get(chatDTO.getChatRoomId());
        ChatRoomDTO chatRoomDTO = chatController.getChatRoomdto();
        List<ChatDTO> chatDTOList = chatRoomDTO.getChats();
        synchronized (chatDTOList){
            chatDTOList.add(chatDTO);//채팅 추가
        }
        List<ChatRoomMemberDTO> chatRoomMemberDTOList = chatRoomDTO.getMembers();
        synchronized (chatRoomMemberDTOList) {
            for (ChatRoomMemberDTO chatRoomMemberDTO : chatRoomMemberDTOList) {
                if (chatRoomMemberDTO.getMemberId().equals(chatDTO.getSenderId())){
                    chatRoomMemberDTO.setReadChatId(chatDTO.getChatId());
                }
            }
        }

        sortChatRoomMember(chatRoomDTO);
        chatController.newChat();
    }

    public void newChat(Long chatRoomId){ // 서버로 부터 누가 읽었음을 받았음
        ChatController chatController = chatControllerMap.get(chatRoomId);
        chatController.newChat();
    }

    public void sortChatRoomMember(ChatRoomDTO chatRoomDTO){
        chatRoomDTO.getMembers().sort(Comparator.comparing(ChatRoomMemberDTO::getReadChatId));
        readCount(chatRoomDTO);
    }

    public void readCount(ChatRoomDTO chatRoom) {
        int participantCount = chatRoom.getParticipantCount();
        Long[] readChatIdArr = new Long[participantCount];
        List<ChatRoomMemberDTO> chatRoomMember = chatRoom.getMembers();
        System.out.print("ChatRoomMember");
        synchronized (chatRoomMember) {
            for(int i = 0; i < participantCount; i++){
                readChatIdArr[i] = chatRoomMember.get(i).getReadChatId();
                System.out.print(chatRoomMember.get(i).getMemberName()+" = " + readChatIdArr[i]);
            }
        }
        System.out.println();
        List<ChatDTO> chats = chatRoom.getChats();
        synchronized (chats){
            int idx = 0;
            for(ChatDTO chatDTO : chats){
                for(int i = idx; i < readChatIdArr.length; i++){
                    if(chatDTO.getChatId()<=readChatIdArr[i]){
                        chatDTO.setUnreadCount(idx);
                        break;
                    }else{
                        idx++;
                    }
                }
            }
        }
    }

    public boolean checkOpenRoom(Long chatRoomId) throws IOException {
        ChatController chatController = chatControllerMap.get(chatRoomId);
        return chatController.getIsOpen();
    }

    public void readChat(Long chatRoomId, Long chatId, boolean send) {
        ChatRoomDTO chatRoomDTO = chatControllerMap.get(chatRoomId).getChatRoomdto();
        List<ChatRoomMemberDTO> chatRoomMemberDTOList = chatRoomDTO.getMembers();
        ChatRoomMemberDTO targetMember = null;

        // 수정 대상 멤버 찾기 (리스트 수정 없이 순회)
        synchronized (chatRoomMemberDTOList) {
            for (ChatRoomMemberDTO memberDTO : chatRoomMemberDTOList) {
                if (memberDTO.getMemberId().equals(MainController.getMember().getId())) {
                    if (!memberDTO.getReadChatId().equals(chatId)) {
                        targetMember = memberDTO;
                    }
                    break;
                }
            }
        }
        // 순회가 끝난 후 수정 및 정렬 수행
        if (targetMember != null) {
            synchronized (chatRoomMemberDTOList) {
                targetMember.setReadChatId(chatId);
            }
            sortChatRoomMember(chatRoomDTO);
            newChat(chatRoomId);
            if(send){
                System.out.println("readChat에서 Dat send함");
                sendData(new Data(ProtocolType.READ_CHAT,
                        new ReadChatRequest(chatRoomId, chatId, member.getId(), member.getUsername())));
            }
        }
    }



    public void updateChatRoomMember(ChatRoomMemberDTO chatRoomMemberDTO) throws IOException {
        System.out.println("업데이트 챗룸멤버 실행"+ chatRoomMemberDTO.getMemberId() + chatRoomMemberDTO.getMemberName());
        ChatController chatController = chatControllerMap.get(chatRoomMemberDTO.getChatRoomId());
        ChatRoomDTO chatRoomDTO = chatController.getChatRoomdto();
        List<ChatRoomMemberDTO> chatRoomMemberList = chatRoomDTO.getMembers();

        synchronized (chatRoomMemberList) {
            Iterator<ChatRoomMemberDTO> iterator = chatRoomMemberList.iterator();
            while (iterator.hasNext()) {
                ChatRoomMemberDTO chatRoomMemberDTO2 = iterator.next();
                if (chatRoomMemberDTO2.getMemberId().equals(chatRoomMemberDTO.getMemberId())) {
                    iterator.remove(); // 안전한 삭제
                    break;
                }
            }
            chatRoomMemberList.add(chatRoomMemberDTO); // 새로운 객체 추가
        }

        sortChatRoomMember(chatRoomDTO);
        newChat(chatRoomDTO.getId());
    }

    public void chat(Long chatRoomId, Long memberId, String message) throws IOException {
        SocketController.getInstance().chat(chatRoomId, memberId, message);
    }
}
