package com.back.add;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class AddDataAndMessage {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    public void addDataAndSentMessage(int articleId, int userId){
        if(isAvailable(articleId, userId)){
            addArticleData( articleId,  userId);
            addUserData( articleId,  userId);
            sentMessage(articleId, userId);
        }
    }
    protected abstract void addArticleData(int articleId, int userId);
    protected abstract void addUserData(int articleId, int userId);
    protected abstract void sentMessage(int articleId, int messageGenerator);
    protected abstract boolean isAvailable(int articleId, int userId);
}
