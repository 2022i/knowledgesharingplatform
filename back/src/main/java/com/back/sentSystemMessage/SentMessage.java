package com.back.sentSystemMessage;

import com.back.get.LastIdOperation;
import com.back.index.Message;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.MessageRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class SentMessage {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    protected Message message=new Message();
    public void sentMessage(int articleId, int messageGeneratorId){
        setMessage(articleId, messageGeneratorId);
    }
    abstract protected void setMessageTitle();
    protected UserData findAuthorDataByArticleId(int articleId){
        int uerId=articleRepository.findArticleById(articleId).getAuthorId();
        return userDataRepository.findUserDataById(uerId);
    }
    protected void setMessage(int articleId, int messageGeneratorId){
        setMessageTitle();
        message.setId(lastIdOperation.getMessageId());
        message.setAuthorId(findAuthorIdByArticleId(articleId));
        message.setCreateTime(LocalDateTime.now());
        message.setMessageGeneratorId(messageGeneratorId);
        message.setMessageGeneratorName(userDataRepository.findUserDataById(messageGeneratorId).getUsername());
        message.setArticleId(articleId);
        message.setArticleTitle(articleRepository.findArticleById(articleId).getTitle());
        message.setRead(false);
        messageRepository.save(message);
        UserData userData=findAuthorDataByArticleId(articleId);
        List<Integer> messageList= userData.getMessageIds();
        messageList.add(message.getId());
        userData.setMessageIds(messageList);
        userDataRepository.save(userData);
    }
    private int findAuthorIdByArticleId(int articleId){
        return articleRepository.findArticleById(articleId).getAuthorId();
    }
}
