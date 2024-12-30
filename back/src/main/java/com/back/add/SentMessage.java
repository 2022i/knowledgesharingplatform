package com.back.add;

import com.back.dto.Message;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public abstract class SentMessage {
    @Resource
    protected Message message;
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private UserDataRepository userDataRepository;
    public void sentMessage(int articleId, int messageGeneratorId){
        setMessage(articleId, messageGeneratorId);
        findUserDataByArticleId(articleId).getMessage().add(message);
    }
    abstract void setMessageTitle();
    private UserData findUserDataByArticleId(int articleId){
        int uerId=articleRepository.findArticleById(articleId).getAuthorId();
        return userDataRepository.findUserDataById(uerId);
    }
    private void setMessage(int articleId, int messageGeneratorId){
        setMessageTitle();
        message.setGenerationTime(LocalDate.now());
        message.setMessageGeneratorId(messageGeneratorId);
        message.setArticleId(articleId);
        message.setRead(false);
    }
}
