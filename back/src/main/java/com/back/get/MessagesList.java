package com.back.get;

import com.back.index.Message;
import com.back.repository.MessageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessagesList {
    @Resource
    private MessageRepository messageRepository;
    public List< Message > getMessages(int userId){
        List<Message> messageList=messageRepository.findMessagesByMessageRecipientIdAndRead(userId, false);
        //Sort by creation time from newest to oldest
        messageList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        List<Message> result=messageList;
        for(Message message:messageList){
            message.setRead(true);
            messageRepository.save(message);
        }
        return result;
    }
}
