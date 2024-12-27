package com.back.add;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

public abstract class AddData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    protected SentMessage sentMessage;
    public void addData(int articleId, int userId){
        addArticleData( articleId,  userId);
        addUserData( articleId,  userId);
        sentMessage(articleId, userId);
    }
    protected abstract void addArticleData(int articleId, int userId);
    protected abstract void addUserData(int articleId, int userId);
    protected abstract void sentMessage(int articleId, int messageGenerator);
}
