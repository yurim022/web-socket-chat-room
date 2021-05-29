package com.example.basicwebsocket.controller;

import com.example.basicwebsocket.repository.ChatRoomRepository;
import com.example.basicwebsocket.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms(Model model)
    {
        return "/chat/room";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room(){
        return chatRoomRepository.findAllRoom();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom creatRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId",roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId){
        return chatRoomRepository.findRoomById(roomId);
    }
}
