package com.example.basicwebsocket.dto;

import com.example.basicwebsocket.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roodId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roodId,String name){
        this.roodId = roodId;
        this.name = name;
    }

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
}
