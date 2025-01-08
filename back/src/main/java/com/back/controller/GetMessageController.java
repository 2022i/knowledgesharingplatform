package com.back.controller;


import com.back.get.MessagesList;
import com.back.index.Message;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetMessageController {
    @Resource
    private MessagesList messagesList;
    @GetMapping("/messages")
    public List<Message> getMessages(int userId){
        return messagesList.getMessages(userId);
    }
}
