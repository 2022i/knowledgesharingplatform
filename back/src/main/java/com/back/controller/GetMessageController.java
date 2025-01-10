package com.back.controller;


import com.back.get.MessagesList;
import com.back.index.Message;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetMessageController {
    @Resource
    private MessagesList messagesList;
    @Description("获取用户的消息列表")
    @GetMapping("/messages")
    public List<Message> getMessages(@RequestParam int userId){
        return messagesList.getMessages(userId);
    }
}
