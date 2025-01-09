package com.back.sentSystemMessage;

import com.back.repository.ArticleRepository;
import com.back.repository.MessageRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SentFollowMessage extends SentMessage {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private UserDataRepository userDataRepository;
    @Override
    protected void setMessageTitle() {
        message.setTitle("Follow");
    }

    public void sentFollowMessage(int messageGeneratorId,int messageRecipientId){
        String messageGeneratorName = userDataRepository.findUserDataById(messageGeneratorId).getUsername();
        message.setContent(messageGeneratorName + " 关注了你");
        setMessage(messageRecipientId, messageGeneratorId);
    }

}
