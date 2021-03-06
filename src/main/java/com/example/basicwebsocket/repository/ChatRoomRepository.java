package com.example.basicwebsocket.repository;

import com.example.basicwebsocket.dto.ChatRoom;
import com.example.basicwebsocket.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.nio.channels.Channel;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    //채팅방 (topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;

    //구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    //Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,String,ChatRoom> opsHashChatRoom;
    //find Topic by roomId
    private Map<String, ChannelTopic> topics;
    @PostConstruct
    private void init() {

        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS,id);
    }

    //채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS,chatRoom.getRoomId(),chatRoom);
        return chatRoom;
    }

    //채팅방 입장 : reids에 topic을 만들고 pub.sub 통신을 하기 위해 리스너를 설정한다.
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber,topic);
            topics.put(roomId,topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}

