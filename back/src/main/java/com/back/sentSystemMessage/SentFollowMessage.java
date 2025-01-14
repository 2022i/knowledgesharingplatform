package com.back.sentSystemMessage;

import com.back.get.LastIdOperation;
import com.back.index.Message;
import com.back.index.UserData;
import com.back.repository.MessageRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SentFollowMessage {
    @Resource
    private MessageRepository messageRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    protected Message message=new Message();

    public void sentMessage(int messageGeneratorId, int messageRecipientId){
        message.setTitle("follow");
        message.setId(lastIdOperation.getMessageId());
        message.setMessageRecipientId(messageRecipientId);
        message.setCreateTime(LocalDateTime.now());
        message.setMessageGeneratorId(messageGeneratorId);
        message.setMessageGeneratorName(userDataRepository.findUserDataById(messageGeneratorId).getUsername());
        message.setRead(false);
        messageRepository.save(message);
        UserData userData=userDataRepository.findUserDataById(messageRecipientId);
        List<Integer> messageList= userData.getMessageIds();
        messageList.add(message.getId());
        userData.setMessageIds(messageList);
        userDataRepository.save(userData);
    }
}
