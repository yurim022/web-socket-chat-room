package com.example.basicwebsocket.controller;

import com.example.basicwebsocket.dto.ChatMessage;
import com.example.basicwebsocket.dto.ChatRoom;
import com.example.basicwebsocket.redis.RedisPublisher;
import com.example.basicwebsocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /*
    @MessageMapping을 통해 Websocket으로 들어오는 "/pub/chat/message" 로 오는 메시지 발행 처리
     */
    @MessageMapping("/chat/message")   //   /pub/chat/message 클라이언트에서 prefix 붙여서 발행요청
    public void message(ChatMessage message){
        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        //publish
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()),message);
    }
}
