package com.example.basicwebsocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String name;
//    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roodId,String name) {
        this.roomId = roodId;
        this.name = name;
    }
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
    /*
    pub/sub 방식을 이하면 웹소켓 세션 관리가 필요 없다.


    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender()+" 님이 입장했습니다. ");
        }
        sendMessage(chatMessage,chatService);
    }

    public  <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session,message));
    }

    *
     */


}
